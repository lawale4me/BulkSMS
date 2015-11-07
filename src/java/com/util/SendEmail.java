/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.util;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;


/**
 *
 * @author Ahmed
 */
public class SendEmail {
        
    private static String formatEmail(String cid) {
        String email ="<html><head><title>Two Factor Authentication</title>" +
                "<style type='text/css'>div{margin:0px;padding:0px;}</style>" +
                "</head><body style='margin:0px'>" +cid+
                "</body></html>";

        return email;
    }

    
    public void sendSimpleMail(Email msg)
    {
    // Create The Email
        MultiPartEmail email = new MultiPartEmail();
        try 
        {               
            email.setHostName(Log.EMAILHOST);
            email.setSmtpPort(25);
            email.setAuthenticator(new DefaultAuthenticator(Log.EMAILADDRESS, Log.EMAIL_PASSWORD));            
            email.setFrom(Log.EMAILADDRESS);
            email.setSubject(msg.getSubject());
            email.setMsg(msg.getMessage()+ResponseCode.EMAIL_SIGNATURE);
            email.addTo(msg.getEmailAddress());
//            email.setTLS(true);        
            System.out.println("**************");
           String stat= email.send();                            
           System.out.println(stat);
            System.out.println("**************");
        }
        catch (EmailException ee) {
        ee.printStackTrace();
        }
    }
    
    
    public static void main(String args[]) 
    {
        try
        {                    
            Email email=new Email();
            email.setEmailAddress("lawale4me@yahoo.com");
            email.setSubject("Your have create a new Case :");
            email.setMessage("Case Body"+"\n");
            System.out.println("about to send");
            new SendEmail().sendSimpleMail(email);
            System.out.println("sent");
        }catch(Exception ex){
            ex.printStackTrace();
        }           
    }
    
    
}