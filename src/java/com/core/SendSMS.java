/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.dao.CustomerRepo;
import com.dao.SMSRepo;
import com.entities.Customer;
import com.util.Util;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ahmed
 */
public class SendSMS extends HttpServlet {

    
    @Inject
    SMSRepo smsrepo;
    @Inject
    CustomerRepo custrepo;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws com.core.ProcessingException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
           PrintWriter out = response.getWriter();
           try{
            String message = request.getParameter("message");
            String destAddr = request.getParameter("destAddr");
            String header = request.getParameter("header");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            Customer cust = custrepo.validate(username, Util.hash(password));
            if(cust!=null)
            {
               double rate =cust.getRate();
               if(message!=null&&destAddr!=null&&header!=null){
              if(cust.getBalance()>rate)
              {
              cust.setBalance(cust.getBalance()-rate);
              smsrepo.sendSMS(header, message, cust, destAddr, new Date());                      
              custrepo.edit(cust);
              out.println("Sms Sent");  
              }
              else
              {
                out.println("Insufficient funds");  
              }
               }else{out.println("Incorrect details");  }
            }
            else
            {
                out.println("Invalid credentials");
            }
        } catch (ProcessingException ex) 
        {
            Logger.getLogger(SendSMS.class.getName()).log(Level.SEVERE, null, ex);            
        }
           finally{
               out.close();
           }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
