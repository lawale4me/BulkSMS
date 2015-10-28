/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.core.ProcessingException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ahmed
 */
public class AbstractDAO<T> implements Serializable {

    private static final Logger logger = Logger.getLogger(AbstractDAO.class.getName());
    private Class<T> entityClass;

    @PersistenceContext(unitName = "BulkSMSPU")
    private EntityManager entityManager;

    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * getter setter method for entityManager
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    
    public void create(T entity)  {
        EntityManager em = getEntityManager();//emf.createEntityManager();//
        try {
            em.persist(entity);
        } catch (Exception e) {
            try {
                logger.log(Level.SEVERE, "exception caught", e);
                throw new ProcessingException(e.getMessage());
            } catch (ProcessingException ex) {
                Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * edit entity
     *
     * @param entity
     */
    public void edit(T entity) {
        EntityManager em = getEntityManager();
        try {
          //  em.getTransaction().begin();
            em.persist(em.merge(entity));
           // em.getTransaction().commit();
        } catch (Exception e) {
           // em.getTransaction().rollback();
            System.out.println(" MY Error: " + e);
            e.printStackTrace();
        } 
        
//        finally {
//            em.close();
//        }

    }

    /**
     * remove entity
     *
     * @param entity
     */
    public void remove(T entity) {
        EntityManager em = getEntityManager();
        try {
            System.out.println("entity: " + entity);
           em.remove(em.merge(entity));
           System.out.println("Removed: " + entity);
        } catch (Exception e) {
            System.out.println("Error Removing: " + e);
            e.printStackTrace();
        } 
        
//        finally {
//            em.close();
//        }

    }

    /**
     * find entity by id
     *
     * @param id
     * @return
     */
    public T find(Object id) {
        EntityManager em = getEntityManager();//emf.createEntityManager();//
        try {
            return em.find(entityClass, id);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } 
//        finally {
//            em.close();
//        }
        return null;
    }

    /**
     * find all entity
     *
     * @return
     */
    public List<T> findAll() {
        EntityManager em = getEntityManager();//emf.createEntityManager();//
        try {
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } 
        
//        finally {
//            em.close();
//        }
        return null;
    }

    /**
     * find between some limits
     *
     * @param range
     * @return
     */
    public List<T> findRange(int[] range) {
        EntityManager em = getEntityManager();//emf.createEntityManager();//
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * count total numbers of entity
     *
     * @return
     */
    public int count() {
        EntityManager em = getEntityManager();//emf.createEntityManager();//
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();//getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getResultList().get(0)).intValue();
    }
}