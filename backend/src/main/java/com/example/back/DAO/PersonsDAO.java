package com.example.back.DAO;

import com.example.back.validator.ValidatorResult;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.example.back.converter.FieldConverter;
import com.example.back.entity.Coordinates;
import com.example.back.entity.Location;
import com.example.back.entity.Person;
import com.example.back.utils.*;

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
            predicates = params.getPredicates(criteriaBuilder, root, coordinatesJoin, locationJoin);

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

            query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            typedQuery = session.createQuery(query);
            long count = typedQuery.getResultList().size();

            result = new PersonsResults(count, persons);
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }

    public Optional<Person> getPerson(Long id){
        Transaction transaction;
        Person person = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            person = session.find(Person.class, id.intValue());
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
            long id = ((Number) session.save(person)).longValue();
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

    public boolean deletePerson(Long id, ValidatorResult validatorResult){
        Transaction transaction = null;
        boolean successful = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Person person = session.find(Person.class, id.intValue());
            if (person != null) {
                session.delete(person);
                session.flush();
                successful = true;
            } else {
                validatorResult.addMessage("No Person with such Id: " + id);
                validatorResult.setCode(404);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            validatorResult.addMessage("Server error, try again");
            validatorResult.setCode(500);
        }
        return successful;
    }
}
