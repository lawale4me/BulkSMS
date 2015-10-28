/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.dao.CustomerRepo;
import com.dao.SMSRepo;
import com.entities.Customer;
import com.entities.Outmessages;
import com.util.SMSStatus;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@RequestScoped
public class ReportMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public ReportMBean() {
    }
    
    boolean checked;
    private String username;
    Customer customer;
    private List<Customer> customerList ;
    private List<Outmessages> processedList ;
    private List<Outmessages> inprogressList ;
    private List<Outmessages> scheduledList ;
    
    String userName,phone,passwd,lname,fname,email,cpasswd;
        int accountType;
        int status;
        double reward;
    
    @Inject 
    CustomerRepo customerrepo;
    @Inject
    SMSRepo smsrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("username");
        customer=customerrepo.findByUsername(username);     
        if(customer!=null){
        customerList = (List<Customer>) customer.getCustomerCollection();
        processedList = customerrepo.getOutMsg(SMSStatus.SENT,customer.getCustomerId());
        inprogressList = customerrepo.getOutMsg(SMSStatus.PENDING,customer.getCustomerId());
        scheduledList = customerrepo.getOutMsg(SMSStatus.PENDING,customer.getCustomerId());
        }
    }                      
   
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public List<Outmessages> getProcessedList() {
        return processedList;
    }

    public void setProcessedList(List<Outmessages> processedList) {
        this.processedList = processedList;
    }

    public List<Outmessages> getInprogressList() {
        return inprogressList;
    }

    public void setInprogressList(List<Outmessages> inprogressList) {
        this.inprogressList = inprogressList;
    }

    public List<Outmessages> getScheduledList() {
        return scheduledList;
    }

    public void setScheduledList(List<Outmessages> scheduledList) {
        this.scheduledList = scheduledList;
    }

    
    
    
}
