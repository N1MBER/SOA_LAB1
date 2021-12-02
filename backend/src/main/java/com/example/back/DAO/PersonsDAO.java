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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonsDAO {
    private void applyPagination(TypedQuery<Person> personFilterQuery, PersonParams params){
        int page = params.getPageIdx();
        if (params.getPageIdx() == null || page <= 0) {
            page = 1;
        }
        int size = params.getPageSize();
        if (params.getPageSize() == null || size <= 0) {
            size = 500;
        }
        int startIndex = (page - 1) * size;
        personFilterQuery.setFirstResult(startIndex);
        personFilterQuery.setMaxResults(size);
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
            System.out.println(e.getMessage());
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return result;
    }

    public Person getMinNationality(){
        List<Person> persons;
        Person result = null;
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQuery.from(Person.class);
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("nationality")));

            CriteriaQuery<Person> query = criteriaQuery.select(root);
            TypedQuery<Person> typedQuery = session.createQuery(query);
            typedQuery.setFirstResult(0);
            typedQuery.setMaxResults(1);

            persons = typedQuery.getResultList();

            if (persons.size() > 0){
                result = persons.get(0);
            }
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

    public Long countMoreHeight(Integer height) {
        Transaction transaction = null;
        long count;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQuery.from(Person.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.greaterThan(root.get("height"), height));
            CriteriaQuery<Person> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Person> typedQuery = session.createQuery(query);
            count = typedQuery.getResultList().size();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return count;
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
