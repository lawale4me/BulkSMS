/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.core.ProcessingException;
import com.dao.AdminRepo;
import com.dao.AuditRepo;
import com.dao.CustomerRepo;
import com.dao.SMSRepo;
import com.dao.TransactionRepo;
import com.entities.Adminusers;
import com.entities.Customer;
import com.entities.Transactions;
import com.util.Email;
import com.util.ResponseCode;
import com.util.SendEmail;
import java.util.Date;
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
public class CreditAdminMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public CreditAdminMBean() {
    }
    
    boolean checked;
    private String username,purpose,amount;
    Customer cust;
    Adminusers au;
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
    AdminRepo adminrepo;
    @Inject
    SMSRepo smsrepo;
    @Inject
    AuditRepo auditrepo;
    @Inject
    TransactionRepo tranxrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("adminadmin");
        au=adminrepo.findByUsername(username);     
        if(au!=null){
        customerList = customerrepo.findAll();
        }
    }                      
   
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    
    public void addCredit() throws ProcessingException{
        
        cust=customerrepo.findByUserId(customerId);        
        Transactions Rtx=new Transactions();
            
            
        if(cust!=null)
        {
            try {
            System.out.println("Old Balance"+cust.getBalance());
            Rtx.setBalanceBefore(cust.getBalance());
            cust.setBalance(cust.getBalance()+Double.parseDouble(amount));
            customerrepo.edit(cust);
            System.out.println("New Balance"+cust.getBalance());
            //loginMBean.Balance=cust.getBalance().toString();
            
            
            Rtx.setBalanceAfter(cust.getBalance());                       
            Rtx.setAmount(Double.parseDouble(amount));
            Rtx.setCreditType("Add Transfer");		      	    
            Rtx.setCustomer(cust.getUsername());
            Rtx.setSource(username);
            Rtx.setTransDate(new Date());
            Rtx.setDescription(purpose);                        
            tranxrepo.create(Rtx);
            
                //SENDING EMAIL
                SendEmail sendObject = new SendEmail();
                Email mail = new Email();
                mail.setEmailAddress(cust.getEmail());
                mail.setMessage("Dear " + cust.getUsername() + ", Your SMS account has been credited with " + amount + " units of SMS credit.\n"
                        + "Thank you for your patronage.");
                mail.setSubject("SMS Account Credited");
                sendObject.sendSimpleMail(mail);
                //END OF EMAIL
             
            String SMSMESSAGE="Dear " + cust.getUsername() + ", Your SMS account has been credited with" + amount + 
                    " units of SMS credit. Thank you for your patronage.";
            smsrepo.sendSMS(ResponseCode.HEADER, SMSMESSAGE, null, cust.getPhone(), new Date());

            
            FacesMessage message = new FacesMessage("Successful", "Credit added successfully");
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            auditrepo.audit(username, "Added Credit "+amount, "IP");
            } catch (Exception ex) {
                FacesMessage message = new FacesMessage("Error", ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, message);
                Logger.getLogger(CreditAdminMBean.class.getName()).log(Level.SEVERE, null, ex);
            }                        
        }
        else{
          FacesMessage messge = new FacesMessage("Error", "Credit not added");
          FacesContext.getCurrentInstance().addMessage(null, messge);
        }
        amount=null;
        purpose=null;
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

    public Adminusers getAu() {
        return au;
    }

    public void setAu(Adminusers au) {
        this.au = au;
    }

    
    
    
    
    
    
}
