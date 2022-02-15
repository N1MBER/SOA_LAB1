package com.example.back.DAO;

import com.example.back.entity.*;
import com.example.back.validator.ValidatorResult;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.example.back.converter.FieldConverter;
import com.example.back.utils.*;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonsDAO {
    public Long countEyeColor(EyeColor eyeColor) {
        Transaction transaction = null;
        long count;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQuery.from(Person.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("eyeColor"), eyeColor));
            CriteriaQuery<Person> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Person> typedQuery = session.createQuery(query);
            count = typedQuery.getResultList().size();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return count;
    }

    public Long countEyeColorWithNationality(EyeColor eyeColor, Country nationality) {
        Transaction transaction = null;
        long count;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQuery.from(Person.class);
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("eyeColor"), eyeColor));
            predicates.add(criteriaBuilder.equal(root.get("nationality"), nationality));
            CriteriaQuery<Person> query = criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
            TypedQuery<Person> typedQuery = session.createQuery(query);
            count = typedQuery.getResultList().size();
        } catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw e;
        }
        return count;
    }
}
