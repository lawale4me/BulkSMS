/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.core.ProcessingException;
import com.dao.AdminRepo;
import com.dao.CustomerRepo;
import com.dao.SMSRepo;
import com.entities.Adminusers;
import com.entities.Customer;
import com.entities.Outmessages;
import com.util.CustomerStatus;
import com.util.Email;
import com.util.ResponseCode;
import com.util.SendEmail;
import com.util.UserType;
import com.util.Util;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@RequestScoped
public class CustomerAdminMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public CustomerAdminMBean() {
    }
    
    boolean checked;
    private String username,myusername;
    Customer customer;
    Adminusers au;
    private List<Customer> customerList ;
    
    String userName,phone,passwd,lname,fname,email,cpasswd,address;
    int accountType;
    int status;
    double reward;
    UserType usertype;
    
    @Inject 
    CustomerRepo customerrepo;
    @Inject 
    AdminRepo adminrepo;
    @Inject
    SMSRepo smsrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("adminadmin");
        au=adminrepo.findByUsername(username);             
        customerList = customerrepo.findAllDesc();
        
    }           
           
 
    public String addAction() throws ProcessingException {
        
        if(StringUtils .containsWhitespace(userName))
        {
            FacesMessage msg = new FacesMessage("Username contains white spaces");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        if(customerrepo.findByUsername(userName)!=null)
        {
            FacesMessage msg = new FacesMessage("Username already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else if(userName!=null&&userName.length()>11){
            FacesMessage msg = new FacesMessage("Username Length is greater than eleven");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else if(customerrepo.findByEmail(email)!=null)
        {
            FacesMessage msg = new FacesMessage("Email already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        if(customerrepo.findByPhone(phone)!=null)
        {
            FacesMessage msg = new FacesMessage("Phone already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else{
        Customer cust = new Customer();                
        cust.setUsername(userName);
        cust.setStatus(CustomerStatus.INACTIVE.ordinal());
        cust.setReward(0d);        
        cust.setPhone(phone);
        cust.setPassword(Util.hash(passwd));
        cust.setLastName(lname);
        cust.setFirstName(fname);
        cust.setEmail(email);
        cust.setAddress(address);
        cust.setBalance(customerrepo.getProperty("SignUpBonus"));
        String code = Util.generateCode();
        cust.setActivationCode(code);
        cust.setRegDate(new Date());
        cust.setAccountType(accountType);
        cust.setRate(customerrepo.getProperty("FlatRate"));
        customerrepo.create(cust);
        customerList.add(cust);
        
        //SENDING EMAIL
        SendEmail sendObject=new SendEmail();
        Email mail=new Email();
        mail.setEmailAddress(email);
        mail.setMessage("Dear "+fname+", SMS Solutions welcomes you. "
                + "Your sms account has been successfully created. "
                + "Your Username is "+userName+" and your password is "+passwd+" ."
                + " Your number verification code has been sent to your phone. \n" +
"Kindly click on the link below to verify your number and activate your account:\n" +
"http://smssolutions.com.ng:8180/BulkSMS/verifycode\n" +
"Thank you for your patronage.");
        mail.setSubject("SMS Account Created");
        sendObject.sendSimpleMail(mail);
        //END OF EMAIL
        
        
        
        String SMSMESSAGE="Your SMS account has been created successfully."
                + " Username = "+userName+" and"
                + " password = "+passwd+" . Your verification code is "+code+"."
                + " Kindly confirm your phone to log in";
        smsrepo.sendSMS(ResponseCode.HEADER, SMSMESSAGE, null, phone, new Date());
        
        FacesMessage msg = new FacesMessage("Account Created successfully");   
        FacesContext.getCurrentInstance().addMessage(null, msg); 
 
        email = "";
        phone = "";
        lname = "";
        fname = "";
        status=0;        
        return null;
        }
    }
    
    
    public String addAction2() throws ProcessingException  {
        if(StringUtils .containsWhitespace(userName))
        {
            FacesMessage msg = new FacesMessage("Username contains white spaces");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        
        if(customerrepo.findByUsername(userName)!=null)
        {
            FacesMessage msg = new FacesMessage("Username already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else if(userName!=null&&userName.length()>11){
            FacesMessage msg = new FacesMessage("Username Length is greater than eleven");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else if(customerrepo.findByEmail(email)!=null)
        {
            FacesMessage msg = new FacesMessage("Email already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        if(customerrepo.findByPhone(phone)!=null)
        {
            FacesMessage msg = new FacesMessage("Phone already exists");   
            FacesContext.getCurrentInstance().addMessage(null, msg);                         
            return null;
        }
        else{
        RequestContext context = RequestContext.getCurrentInstance();
        Customer cust = new Customer();                
        cust.setUsername(userName);
        cust.setStatus(CustomerStatus.INACTIVE.ordinal());
        cust.setReward(0d);
//        customer.setReseller();
        cust.setPhone(phone);
        try {
            cust.setPassword(Util.hash(passwd));
        } catch (ProcessingException ex) {
            Logger.getLogger(CustomerAdminMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        cust.setLastName(lname);
        cust.setFirstName(fname);
        cust.setEmail(email);
        cust.setBalance(customerrepo.getProperty("SignUpBonus"));        
        String code = Util.generateCode();
        cust.setActivationCode(code);
        cust.setAccountType(accountType);
        cust.setAddress(address);
        cust.setRate(customerrepo.getProperty("FlatRate"));
        cust.setRegDate(new Date());
        String messsg="";
        try {        
            customerrepo.create(cust);
        } catch (Exception ex) {
            messsg=ex.getMessage();
        }
        
        //SENDING EMAIL
        SendEmail sendObject=new SendEmail();
        Email mail=new Email();
        mail.setEmailAddress(email);
        mail.setMessage("Dear "+fname+", SMS Solutions welcomes you. "
                + "Your sms account has been successfully created. "
                + "Your Username is "+userName+" and your password is "+passwd+" ."
                + " Your number verification code has been sent to your phone. \n" +
"Kindly click on the link below to verify your number and activate your account:\n" +
"http://smssolutions.com.ng:8180/BulkSMS/verifycode\n" +
"Thank you for your patronage.");
        mail.setSubject("SMS Account Created");
        sendObject.sendSimpleMail(mail);
        //END OF EMAIL
        
        String SMSMESSAGE="Your SMS account has been created successfully."
                + " Username = "+userName+" and"
                + " password = "+passwd+" . Your verification code is "+code+"."
                + " Kindly confirm your phone to log in";
        smsrepo.sendSMS(ResponseCode.HEADER, SMSMESSAGE, null, phone, new Date());
                
        
        messsg="Account Created successfully";
        FacesMessage msg = new FacesMessage(messsg);   
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        context.addCallbackParam("loggedIn", true);
 
        email = "";
        phone = "";
        lname = "";
        fname = "";
        status=0;        
        return null;
        }
    }
    
    public void onEdit(RowEditEvent event) {
        Customer ccust=(Customer) event.getObject();                
        FacesMessage msg = new FacesMessage("Customer Edited",ccust.getFirstName());                  
       Customer cust=customerrepo.findByUserId(ccust.getCustomerId());                        
        cust.setPhone(ccust.getPhone());        
        cust.setLastName(ccust.getLastName());
        cust.setFirstName(ccust.getFirstName());
        cust.setEmail(ccust.getEmail());        
        cust.setRate(ccust.getRate());
        customerrepo.edit(cust);        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
       
    public void onCancel(RowEditEvent event) {  
        FacesMessage msg = new FacesMessage("Customer deleted");   
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        Customer pr=(Customer) event.getObject();
        pr=customerrepo.findByUserId(pr.getCustomerId());
        List<Outmessages> outmsg= smsrepo.findBySenderId(pr.getCustomerId());
        for(Outmessages out:outmsg){
            smsrepo.remove(out);
        }
        customerrepo.remove(pr);
        customerList.remove(pr);
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getCpasswd() {
        return cpasswd;
    }

    public void setCpasswd(String cpasswd) {
        this.cpasswd = cpasswd;
    }
    
    
    
    public void resetPasswd() throws ProcessingException{
        RequestContext context = RequestContext.getCurrentInstance();
        String resmsg="Your username address does not exist "+myusername;
        Adminusers cust=adminrepo.findByUsername(myusername);     
        String pwd=new Util().genPass();
        if(cust!=null)
        {
        cust.setPassword(Util.hash(pwd));
        String msg="Your account has been reset your new password is: "+pwd+" and your username is: "+cust.getUsername();        
        adminrepo.edit(cust);
        smsrepo.sendSMS(ResponseCode.HEADER, msg, null, cust.getPhonenumber(), new Date());
        
        //SENDING EMAIL
//        SendEmail sendObject=new SendEmail();
//        Email mail=new Email();
//        mail.setEmailAddress(email);
//        mail.setMessage("Dear "+cust.getUsername()+", you have requested for your "
//                + "login detail which are below:\n" +
//                "Username = "+cust.getUsername()+"\n" +
//                "Password = "+pwd+"\n" +
//                "Kindly change your password from your profile page after signing in successfully.\n "
//                + "Thank you for choosing SMS SOLUTIONS");
//        mail.setSubject("Password Reset Details");
//        sendObject.sendSimpleMail(mail);
        //END OF EMAIL
        
        
        resmsg="Password reset sent to your email and phone";
        }
        FacesMessage msg = new FacesMessage(resmsg);   
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        context.addCallbackParam("loggedIn", true);
 
        email = "";
    }
    
    
    public void reset(Integer custId) throws ProcessingException{
        RequestContext context = RequestContext.getCurrentInstance();
        String resmsg="Internal Error";
        Customer cust=customerrepo.findByUserId(custId);     
        String pwd=new Util().genPass();
        if(cust!=null)
        {
        cust.setPassword(Util.hash(pwd));
        String msg="Your account has been reset. Your new password is: "+pwd+" and your username is: "+cust.getUsername();        
        customerrepo.edit(cust);
        smsrepo.sendSMS(ResponseCode.HEADER, msg, null, cust.getPhone(), new Date());    
        
                //SENDING EMAIL
        SendEmail sendObject=new SendEmail();
        Email mail=new Email();
        mail.setEmailAddress(cust.getEmail());
        mail.setMessage("Dear "+cust.getFirstName()+",\n Your account has been reset. Your new password is: "+pwd+
                " and your username is: "+cust.getUsername()            
                + "Thank you for choosing SMS SOLUTIONS");
        mail.setSubject("Password Reset Details");
        sendObject.sendSimpleMail(mail);
        //END OF EMAIL
        
        resmsg="New password has been sent to customers phone";
        }
        System.out.println("Here:"+resmsg);
        FacesMessage msg = new FacesMessage(resmsg);   
        FacesContext.getCurrentInstance().addMessage(null, msg); 
        context.addCallbackParam("loggedIn", true);       
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    public void onSelect(AjaxBehaviorEvent event) {
    checked = ((HtmlSelectBooleanCheckbox)event.getSource()).isSelected();
    System.out.println("checked:"+checked);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public UserType[] getUserTypes() {
        return UserType.values();
    }

    public Adminusers getAu() {
        return au;
    }

    public void setAu(Adminusers au) {
        this.au = au;
    }

    public String getMyusername() {
        return myusername;
    }

    public void setMyusername(String myusername) {
        this.myusername = myusername;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }
    
    
    
    
    
    
}
