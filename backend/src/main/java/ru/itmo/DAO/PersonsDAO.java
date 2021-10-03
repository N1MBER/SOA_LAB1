package ru.itmo.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.itmo.converter.FieldConverter;
import ru.itmo.entity.Coordinates;
import ru.itmo.entity.Location;
import ru.itmo.entity.Person;
import ru.itmo.utils.*;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public class PersonsDAO {
    private void applyPagination(TypedQuery<Person> labWorkQuery, PersonParams params){
        int startIndex = (params.getPageIdx() - 1) * params.getPageSize();
        labWorkQuery.setFirstResult(startIndex);
        labWorkQuery.setMaxResults(params.getPageSize());
    }

    public PersonsResults getAllPersons(PersonParams params){
        List<Person> persons = null;
        PersonsResults result = null;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQuery.from(Person.class);
            Join<Person, Coordinates> coordinatesJoin = root.join("coordinates");
            Join<Person, Location> locationJoin = root.join("location");
            List<Predicate> predicates;

            if (params.getLessThanHeightFlag()){
                predicates = params.getLessHeightPredicate(criteriaBuilder, root);
            } else {
                predicates = params.getPredicates(criteriaBuilder, root, coordinatesJoin, locationJoin);
            }

            if (params.getSortField() != null){
                if (params.getSortField().startsWith("coordinates")){
                    criteriaQuery.orderBy(criteriaBuilder.asc(coordinatesJoin.get(FieldConverter.removePrefixFieldConvert(params.getSortField(), "coordinates"))));
                } else if (params.getSortField().startsWith("location")){
                    criteriaQuery.orderBy(criteriaBuilder.asc(locationJoin.get(FieldConverter.removePrefixFieldConvert(params.getSortField(), "location"))));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(params.getSortField())));
                }
            }

            CriteriaQuery<Person> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Person> typedQuery = session.createQuery(query);
            applyPagination(typedQuery, params);
            persons = typedQuery.getResultList();

            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
            countQuery.select(criteriaBuilder.count(countQuery.from(Person.class)));
            countQuery.where(predicates.toArray(new Predicate[]{}));
            Long count = session.createQuery(countQuery).getSingleResult();

            result = new PersonsResults(count, persons);
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }

    public Optional<Person> getPerson(long id){
        Transaction transaction;
        Person person = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            person = session.find(Person.class, id);
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
        return Optional.ofNullable(person);
    }

    public long createPerson(Person person){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            Long id = (Long) session.save(person);
            transaction.commit();
            return id;
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void updatePerson(Person person){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.update(person);
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
