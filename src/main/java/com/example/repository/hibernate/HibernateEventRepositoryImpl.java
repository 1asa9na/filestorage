package com.example.repository.hibernate;

import com.example.model.Event;
import com.example.model.File;
import com.example.model.User;
import com.example.repository.EventRepository;
import com.example.repository.RepositoryException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.util.List;

/**
 * Hibernate implementation of event repository.
 */

public class HibernateEventRepositoryImpl extends HibernateRepository<Event, Integer> implements EventRepository {

    public HibernateEventRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, Event.class);
    }

    @Override
    public List<Event> findByUserId(Integer userId) throws RepositoryException {
        try (EntityManager em = getNewEntityManager()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Event> cq = cb.createQuery(Event.class);
            Root<Event> eventRoot = cq.from(Event.class);

            eventRoot.fetch("user", JoinType.INNER);
            eventRoot.fetch("file", JoinType.INNER);

            cq.select(eventRoot).where(cb.equal(eventRoot.get("user").get("id"), userId));

            return em.createQuery(cq).getResultList();
        } catch (IllegalStateException | IllegalArgumentException e) {
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public Event save(Integer userId, Integer fileId) {
        EntityManager em = getNewEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            File file = em.find(File.class, fileId);
            Event event = new Event();
            event.setUser(user);
            event.setFile(file);
            em.persist(event);
            tx.commit();
            return event;
        } catch (IllegalArgumentException
        | IllegalStateException
        | TransactionRequiredException
        | EntityExistsException e) {
            tx.rollback();
            throw new RepositoryException(e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
