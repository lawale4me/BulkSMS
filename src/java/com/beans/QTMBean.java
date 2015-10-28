/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beans;


import com.dao.AdminRepo;
import com.dao.CustomerRepo;
import com.dao.QTRepo;
import com.dao.SMSRepo;
import com.dao.TransactionRepo;
import com.entities.Adminusers;
import com.entities.Customer;
import com.entities.Outmessages;
import com.entities.QuicktellerTransactions;
import com.entities.Transactions;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@SessionScoped
public class QTMBean {

    /**
     * Creates a new instance of ProductMBean
     */
    public QTMBean() {
    }
    
    boolean checked;
    private String username,type;
    Adminusers au;
    private List<Customer> customerList ;
    private List<QuicktellerTransactions> qtList ;    
    
    Date startdate=null,enddate=null;
    
    String userName,phone,passwd,lname,fname,email,cpasswd;
        int accountType;
        int status;
        double reward;
    
    @Inject 
    CustomerRepo customerrepo;
    @Inject
    QTRepo qtrepo;
    
    @Inject
    TransactionRepo traxnrepo;
    
    @Inject 
    AdminRepo adminrepo;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();  
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();  
        HttpSession httpSession = request.getSession(false);          
        username=(String) httpSession.getAttribute("adminadmin");
        au=adminrepo.findByUsername(username);     
        qtList=qtrepo.findAllDesc();
    }                      
   
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Adminusers getAu() {
        return au;
    }

    public void setAu(Adminusers au) {
        this.au = au;
    }


    

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
 
      

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<QuicktellerTransactions> getQtList() {
        return qtList;
    }

    public void setQtList(List<QuicktellerTransactions> qtList) {
        this.qtList = qtList;
    }


    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
    
}
