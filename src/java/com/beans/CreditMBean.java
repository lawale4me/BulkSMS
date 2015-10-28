/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.core.ProcessingException;
import com.dao.AuditRepo;
import com.dao.CustomerRepo;
import com.dao.SMSRepo;
import com.entities.Customer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
public class CreditMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public CreditMBean() {
    }
    
    boolean checked;
    private String username,purpose,amount;
    Customer customer,cust;
    private List<Customer> customerList ;
    @ManagedProperty(value="#{loginMBean}")
    private LoginMBean loginMBean; 
        
        int accountType;
        int status;
        double reward;
        Integer customerId;
    
    @Inject 
    CustomerRepo customerrepo;
    @Inject
    SMSRepo smsrepo;
    @Inject
    AuditRepo auditrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("username");
        customer=customerrepo.findByUsername(username);     
        if(customer!=null){
        customerList = (List<Customer>) customer.getCustomerCollection();        
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    public void addCredit(){
        
        cust=customerrepo.findByUserId(customerId);
        double flatRate=cust.getRate();
        if(cust!=null)
        {
            System.out.println("Old Balance"+cust.getBalance());
            cust.setBalance(cust.getBalance()+(Double.parseDouble(amount)/flatRate));
            customerrepo.edit(cust);
            System.out.println("New Balance"+cust.getBalance());
            //loginMBean.Balance=cust.getBalance().toString();
            FacesMessage message = new FacesMessage("Successful", "Credit added successfully");
            FacesContext.getCurrentInstance().addMessage(null, message);
            try {
                auditrepo.audit(username, "Added Credit "+amount, "IP");
            } catch (ProcessingException ex) {
                Logger.getLogger(CreditMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
          FacesMessage messge = new FacesMessage("Error", "Credit not added");
        FacesContext.getCurrentInstance().addMessage(null, messge);
        }
    }

    public Customer getCust() {
        return cust;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LoginMBean getLoginMBean() {
        return loginMBean;
    }

    public void setLoginMBean(LoginMBean loginMBean) {
        this.loginMBean = loginMBean;
    }

    
    
    
    
    
    
}
