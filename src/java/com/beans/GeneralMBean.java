/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Ahmed
 */
@Named(value = "generalMBean")
@ApplicationScoped
public class GeneralMBean {

    /**
     * Creates a new instance of GeneralMBean
     */
    public GeneralMBean() {
    }
    
    private String COPYRIGHT;

    public String getCOPYRIGHT() {
        return COPYRIGHT;
    }

    public void setCOPYRIGHT(String COPYRIGHT) {
        this.COPYRIGHT = COPYRIGHT;
    }
    
    
    
    
}
