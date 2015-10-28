/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.entities.Customer;
import com.entities.Outmessages;
import com.entities.Rates;
import com.util.SMSStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author Ahmed
 */
@Stateless
public class CustomerRepo     extends AbstractDAO<Customer>{

    private static final Logger logger = Logger.getLogger(CustomerRepo.class.getName());

    public CustomerRepo() {
        super(Customer.class);
    }
    
    
    

    public Customer findByUsername(String username) {
        EntityManager em = getEntityManager();
        List<Customer> device = new ArrayList();
        try {
            device =  em.createNamedQuery("Customer.findByUsername").setParameter("username", username).getResultList();
            if (device != null && !device.isEmpty()) {
                return device.get(0);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return null;
    }
    
    public Customer findByUserId(Integer userId) {
        EntityManager em = getEntityManager();
        Customer user = null;
        try {
            user = (Customer) em.createNamedQuery("Customer.findByCustomerId").setParameter("customerId", userId).getResultList().get(0);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return user;
    }
    
    
    public Customer validate(String username,String passwd) {
        EntityManager em = getEntityManager();
        Customer user = null;
        try {
            String query = "SELECT u FROM Customer u WHERE u.username = :username and u.password = :password";
        List<Customer> adminUser = em.createQuery(query, Customer.class).setParameter("username", username).setParameter("password", passwd).getResultList();
        return adminUser.isEmpty() ? null : adminUser.get(0);          
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return user;
    }
    
    public List<Customer> findAllDesc() {
        EntityManager em = getEntityManager();
        List<Customer> device = new ArrayList();
        try {
            device =  em.createNamedQuery("Customer.findAll").getResultList();
            if (device != null && !device.isEmpty()) {
                return device;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return null;
    }

    public List<Customer> findMyCustomers(Integer customerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Customer findByEmail(String email) {
        EntityManager em = getEntityManager();
        List<Customer> device = new ArrayList();
        try {
            device =  em.createNamedQuery("Customer.findByEmail").setParameter("email", email).getResultList();
            if (device != null && !device.isEmpty()) {
                return device.get(0);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return null;
    }


    public List<Outmessages> getOutMsg(SMSStatus smsStatus, Integer customerId) {
        EntityManager em = getEntityManager();
        List<Outmessages> device = new ArrayList();
        try {
            device =  em.createNamedQuery("Outmessages.getOutMsg").setParameter("status", smsStatus.ordinal()).setParameter("senderId", customerId).getResultList();
            if (device != null && !device.isEmpty()) {
                return device;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return null;
    }

    public double getProperty(String name) {
        EntityManager em = getEntityManager();
        double value = 0d;
        List<Rates> rate=null;
        try {
            rate =  em.createNamedQuery("Rates.findByName").setParameter("name", name).getResultList();
            if (rate != null && !rate.isEmpty()) {
                return rate.get(0).getRate();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return value;
    }         

    public Customer findByPhone(String phone) {
        EntityManager em = getEntityManager();
        List<Customer> device = new ArrayList();
        try {
            device =  em.createNamedQuery("Customer.findByPhone").setParameter("phone", phone).getResultList();
            if (device != null && !device.isEmpty()) {
                return device.get(0);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "exception caught", e);
        }
        return null; 
    }
    
    
}
