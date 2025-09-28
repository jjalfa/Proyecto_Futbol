package org.uacm.futbol.persistencia;

import jakarta.persistence.*;
import java.util.List;

public class GenericJpaDao<T> {
    protected final EntityManager em;
    private final Class<T> clazz;

    public GenericJpaDao(EntityManager em, Class<T> clazz) {
        this.em = em;
        this.clazz = clazz;
    }

    public T find(Long id) {
        return em.find(clazz, id);
    }

    public List<T> findAll() {
        return em.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
    }

    public T save(T entity) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        T merged = em.merge(entity);
        tx.commit();
        return merged;
    }

    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        tx.commit();
    }
}

