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
import com.dao.TransactionRepo;
import com.entities.Customer;
import com.entities.Transactions;
import com.util.Email;
import com.util.ResponseCode;
import com.util.SendEmail;
import com.util.UserType;
import java.util.Date;
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
public class TransferMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public TransferMBean() {
    }
    
    boolean checked;
    private String username,purpose,amount,cUsername,desc;
    Customer customer,cust;    
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
    @Inject
    TransactionRepo tranxrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("username");
        customer=customerrepo.findByUsername(username);            
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
    
    public void transferCredit() throws ProcessingException{
        if(customer.getBalance()>Double.parseDouble(amount))
        {
        cust=customerrepo.findByUsername(cUsername);        
        if(cust!=null)
        {
            Transactions Stx=new Transactions();
            Transactions Rtx=new Transactions();
            Stx.setBalanceBefore(customer.getBalance());
            Rtx.setBalanceBefore(cust.getBalance());
            
            System.out.println("Old Balance Sender:"+customer.getBalance());
            System.out.println("Old Balance Reciever:"+cust.getBalance());
            
            cust.setBalance(cust.getBalance()+Double.parseDouble(amount));
            customer.setBalance(customer.getBalance()-Double.parseDouble(amount));
            
            customerrepo.edit(cust);
            customerrepo.edit(customer);
            
            System.out.println("New Balance Sender:"+customer.getBalance());
            System.out.println("New Balance Reciever"+cust.getBalance());
            
            loginMBean.Balance=String.valueOf(customer.getBalance().longValue());
            
            Stx.setBalanceAfter(customer.getBalance());
            Rtx.setBalanceAfter(cust.getBalance());                        
            Stx.setAmount(Double.parseDouble(amount));
            Rtx.setAmount(Double.parseDouble(amount));
            Rtx.setCreditType("Credit Transfer");
            Stx.setCreditType("Credit Transfer");
            Stx.setCustomer(customer.getUsername());
            Rtx.setCustomer(cust.getUsername());
            Rtx.setSource(customer.getUsername());
            Stx.setSource(cust.getUsername());
            Stx.setTransDate(new Date());
            Rtx.setTransDate(new Date());
            Stx.setDescription(purpose);
            Rtx.setDescription(purpose);
            
            tranxrepo.create(Stx);
            tranxrepo.create(Rtx);
            
            
            //SENDING SMS            
             String SMSMESSAGE="Dear "+customer.getUsername()+", You have transfered : "+amount+" Unit(s) to "+cust.getUsername()+". Thank you";
             String SMSMESSAGE2="Dear "+cust.getUsername()+", You have received : "+amount+" Unit(s) from "+customer.getUsername()+". Thank you";
             
             if(cust.getAccountType()!=UserType.ResellerCustomer.ordinal()){
             smsrepo.sendSMS(ResponseCode.HEADER, SMSMESSAGE, null, customer.getPhone(), new Date());
             smsrepo.sendSMS(ResponseCode.HEADER, SMSMESSAGE2, null, cust.getPhone(), new Date());
             }else{
                        smsrepo.sendSMS(customerrepo.findByUserId(cust.getReseller()).getUsername(), SMSMESSAGE, null, cust.getPhone(), new Date());
                        smsrepo.sendSMS(customerrepo.findByUserId(cust.getReseller()).getUsername(), SMSMESSAGE2, null, cust.getPhone(), new Date());
             }
             
            //END OF SENDING SMS
            
            
             //SENDING EMAIL
             SendEmail sendObject=new SendEmail();
             Email mail=new Email();
             mail.setEmailAddress(customer.getEmail());
             mail.setMessage("Dear "+customer.getUsername()+", You have transfered : "+amount+" Unit(s) to "+cust.getUsername()+". Thank you");
             mail.setSubject("SMS Unit Debit");
             if(customer.getAccountType()!=UserType.ResellerCustomer.ordinal()){
             sendObject.sendSimpleMail(mail);
             }
             
             Email mail1=new Email();
             mail1.setEmailAddress(cust.getEmail());
             mail1.setMessage("Dear "+cust.getUsername()+", You have received : "+amount+" Unit(s) from "+customer.getUsername()+". Thank you");
             mail1.setSubject("SMS Unit Credit");
             
             if(cust.getAccountType()!=UserType.ResellerCustomer.ordinal()){
             sendObject.sendSimpleMail(mail1);
             }
            //END OF EMAIL
            
            FacesMessage message = new FacesMessage("Successful", "Credit added successfully");
            FacesContext.getCurrentInstance().addMessage(null, message);
            try {
                auditrepo.audit(username, "Transfer Credit "+amount +" to "+cust.getUsername(), "IP");
            } catch (ProcessingException ex) {
                Logger.getLogger(TransferMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
          FacesMessage messge = new FacesMessage("Error", "Username does not exist");
        FacesContext.getCurrentInstance().addMessage(null, messge);
        }
        }
        else{
            FacesMessage messge = new FacesMessage("Error", "Insufficeint funds");
             FacesContext.getCurrentInstance().addMessage(null, messge);
        }
        
        cUsername=null;
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

    public String getcUsername() {
        return cUsername;
    }

    public void setcUsername(String cUsername) {
        this.cUsername = cUsername;
    }

    
    
     public void reset() {     
        cUsername=null;
        amount=null;
        purpose=null;        
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    
     
    
    
}
