package com.study_site.java_project.web.repository;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Repository
public class SomeHibernateRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    protected SessionFactory getSessionFactory() {
        return emf.unwrap(SessionFactory.class);
    }
}
