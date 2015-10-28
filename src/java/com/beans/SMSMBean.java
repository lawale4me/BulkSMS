/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.core.ProcessingException;
import com.dao.AdminRepo;
import com.dao.AuditRepo;
import com.dao.BatchRepo;
import com.dao.CustomerRepo;
import com.dao.DraftRepo;
import com.dao.SMSRepo;
import com.entities.Addressbook;
import com.entities.Adminusers;
import com.entities.Batch;
import com.entities.Customer;
import com.entities.Draft;
import com.util.UserType;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@RequestScoped
public class SMSMBean {

    
    String sender,number,message,draft;
    Date schedule=null;
    Batch batch[];
    List<Batch> batchList;
    UserType role;
    String username;
    Adminusers adminUser;
    List<String> branchnames;    
    @Inject
    AdminRepo adminrepo;
    @Inject
    AuditRepo auditrepo;
    @Inject
    SMSRepo smsrepo;
    @Inject
    CustomerRepo customerrepo;
    @Inject
    BatchRepo batchrepo;
    @Inject 
    DraftRepo draftrepo;    
    Customer customer;
    Integer idraft;    
    Draft selectedDraft;
    List<Draft> drafts;
    Integer[] ibatch;
    
    @ManagedProperty(value="#{loginMBean}")
    private LoginMBean loginMBean; 
    
    /**
     * Creates a new instance of UserMBean
     */
    public SMSMBean() {
    }

    @PostConstruct
    public void init() {        
        
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("username");                 
        customer=customerrepo.findByUsername(username);
        batchList=batchrepo.getBatchList(customer);
        drafts=draftrepo.findByCustomer(customer.getCustomerId());        
    }
    
    

   

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
    }     
    
    
    public static String formatDestination(String phone) {

        String destinationMsisdn = phone;
        String formattedDest = "invalid";
        try {
            if (destinationMsisdn.length() == 13) {
                if (destinationMsisdn.startsWith("234")) {
                    if (Pattern.matches("[2][3][4][0-9]*", destinationMsisdn)) {
                        formattedDest = destinationMsisdn;

                    }
                }            
            } else if (destinationMsisdn.length() == 11) {                
                if (destinationMsisdn.startsWith("0")) {                    
                    if (Pattern.matches("[0-9]*", destinationMsisdn));
                    formattedDest = destinationMsisdn.replaceFirst("0", "234");                    
                }
            }
             if (destinationMsisdn.contains("\\/")) {
                formattedDest = "invalid";                
                return formattedDest;
            }

             if (!Pattern.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]", formattedDest)) {
                formattedDest = "invalid";                
                return formattedDest;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return formattedDest;

    }

    
           
    public String sendSMS() throws ProcessingException
    {                      
        System.out.println("Schedule:"+schedule);
        String[] numbers= number.split(",");
        if(numbers.length<customer.getBalance())
        {
          customer.setBalance(customer.getBalance()-numbers.length);
           for(String num:numbers){
            num=formatDestination(num);
            smsrepo.sendSMS(sender, message, customer,num,schedule);
         }
        message=null;
        sender=null;
        schedule=null;
        number=null;
        }
        else
        {
            FacesMessage message = new FacesMessage("Error", " Insufficient Units");
        FacesContext.getCurrentInstance().addMessage(null, message); 
            return null;
        }
        auditrepo.audit(customer.getUsername(), "SMS Sent","IPADDRESS");      
        customerrepo.edit(customer);
        loginMBean.Balance=customer.getBalance().toString();
        
        FacesMessage message = new FacesMessage("Succesful","Message(s) have been submitted");
        FacesContext.getCurrentInstance().addMessage(null, message);        
        return "";
    }
    
    
    public String sendBulkSMS() throws ProcessingException
    {    
        System.out.println("ibatch:"+ibatch.length);
        //for(Batch bb:batch){
        for(Integer ib:ibatch){
            Batch bb =batchrepo.getBatch(ib);
        if(bb.getAddressbookCollection().size()<customer.getBalance())
        {
          customer.setBalance(customer.getBalance()-bb.getAddressbookCollection().size());        
         for(Addressbook contact:bb.getAddressbookCollection())
         {
            smsrepo.sendSMS(sender, message, customer,contact.getPhonenumber(),schedule);
         }
        }
        }
        customerrepo.edit(customer);
        loginMBean.Balance=customer.getBalance().toString();
        message=null;
        sender=null;
        schedule=null;
        number=null;
        
        auditrepo.audit(customer.getUsername(), "SMS Sent","IPADDRESS");        
        
        FacesMessage message = new FacesMessage("Succesful","Message(s) have been submitted");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        return "";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }      

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public Batch[] getBatch() {
        return batch;
    }

    public void setBatch(Batch[] batch) {
        this.batch = batch;
    }

    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }
    
    public void updateMsg(AjaxBehaviorEvent event){
        if(selectedDraft!=null){          
         message=selectedDraft.getDraft();    
        }
        
    }
    
    public void saveMsg(){
        String title,msg;
         
        try {        
            draftrepo.draft(customer, message);
            title="Successful";
            msg="Message saved successfully";
            message="";
          } catch (ProcessingException ex) {
            title="Error saving!!";
            msg=ex.getMessage();
        }
        
        drafts=draftrepo.findByCustomer(customer.getCustomerId());
        
        FacesMessage message = new FacesMessage(title, msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void deleteMsg(){
         String title = null,msg="Not Applicable";
         if(selectedDraft!=null&&selectedDraft.getDraftId()!=-1)
         {             
             Draft Ddraft=draftrepo.findDraft(selectedDraft.getDraftId());
        try {        
                        
            draftrepo.remove(Ddraft);
            message=null;
            title="Successful";
            msg="Message deleted successfully";
          } catch (Exception ex) {
            title="Error deleting!!";
            msg=ex.getMessage();
          }
        }
       
        drafts=draftrepo.findByCustomer(customer.getCustomerId());
        FacesMessage message = new FacesMessage(title, msg);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public Integer getIdraft() {
        return idraft;
    }

    public void setIdraft(Integer idraft) {
        this.idraft = idraft;
    }

    public Draft getSelectedDraft() {
        return selectedDraft;
    }

    public void setSelectedDraft(Draft selectedDraft) {
        this.selectedDraft = selectedDraft;
    }

    public List<Draft> getDrafts() {        
        return drafts;
    }

    public void setDrafts(List<Draft> drafts) {
        this.drafts = drafts;
    }
    
    
    public void reset() {
     //  RequestContext.getCurrentInstance().reset("form:panel");
        message=null;
        sender=null;
        number=null;
        schedule=null;
    }

    public LoginMBean getLoginMBean() {
        return loginMBean;
    }

    public void setLoginMBean(LoginMBean loginMBean) {
        this.loginMBean = loginMBean;
    }

    public Integer[] getIbatch() {
        return ibatch;
    }

    public void setIbatch(Integer[] ibatch) {
        this.ibatch = ibatch;
    }
             
    

}