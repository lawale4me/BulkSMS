/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import com.dao.CustomerRepo;
import com.entities.Customer;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ahmed
 */
@Named(value = "topupMBean")
@SessionScoped
public class TopupMBean implements Serializable {

    private String amount;
    private String urlstring,username;
    private double flatRate;
    Customer customer;
    @Inject
    CustomerRepo custrepo;
    
    public TopupMBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        username=(String) httpSession.getAttribute("username");     
        customer=custrepo.findByUsername(username);
        if(customer!=null){
        flatRate=customer.getRate();
        }
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {        
        this.amount =amount;
        try {
            setUrlstring(username+URLEncoder.encode("&amount="+amount,"UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TopupMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String buyCredit(){
        return "pretty:confirmtopup";
    }

    public String getUrlstring() {
        System.out.println("Urlstring:"+urlstring);        
        return urlstring;
    }

    public void setUrlstring(String urlstring) {
        this.urlstring = urlstring;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getFlatRate() {        
        return flatRate;
    }

    public void setFlatRate(double flatRate) {
        this.flatRate = flatRate;
    }        
    
    
    public int getUnits(){
        System.out.println("Amount:"+amount+"Rate "+getFlatRate());
        return (int) (Integer.parseInt(amount)/flatRate);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
    
}
