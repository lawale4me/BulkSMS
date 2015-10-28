/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core;

import com.dao.CustomerRepo;
import com.dao.QTRepo;
import com.dao.SMSRepo;
import com.dao.TransactionRepo;
import com.entities.Customer;
import com.entities.QuicktellerTransactions;
import com.entities.Transactions;
import com.util.QTResponse;
import com.util.QTStatus;
import com.util.ResponseCode;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Ahmed
 */
@Stateless
public class TaskBean implements Job{
    @EJB
    QTRepo qtrepo;
    @EJB
    CustomerRepo custrepo;
    @EJB
    TransactionRepo tranxrepo;
    @Inject
    SMSRepo smsrepo;
    
    List<QuicktellerTransactions> qtTranx;
    
    /**
     * Creates a new instance of TaskBean
     */
    public TaskBean() {
        try {
            Context context = new InitialContext();            
            qtrepo = (QTRepo) context.lookup("java:global/BulkSMS/QTRepo");
            custrepo = (CustomerRepo) context.lookup("java:global/BulkSMS/CustomerRepo");
            tranxrepo = (TransactionRepo) context.lookup("java:global/BulkSMS/TransactionRepo");
            smsrepo =(SMSRepo) context.lookup("java:global/BulkSMS/SMSRepo");
        } catch (NamingException ex) {
            Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {                            
             List<QuicktellerTransactions> pendingList=qtrepo.getAllPending();
             if(pendingList!=null){
             for(QuicktellerTransactions qt:pendingList){         
                 if(qt.getTxRef()!=null){
                 QTResponse resp= ServerConnector.getQtResponse(qt.getTxRef());
                 if(resp.isHonour()&&resp.isSuccess())
                 {
                    Customer cust= custrepo.findByUsername(qt.getCustomerId());
                    double flatRate=cust.getRate();
                    Transactions Rtx=new Transactions();
                    Rtx.setBalanceBefore(cust.getBalance());
                    
                    qt.setRespCode(resp.getResponseCode());                     
                    qt.setRequestStatus(QTStatus.TREATED.ordinal());
                    if(resp.getDescription()!=null&&!resp.getDescription().isEmpty()&&resp.getDescription().trim().length()>4)
                    {                    
                    qt.setRespDesc(resp.getDescription());                    
                    }
                    
                    cust.setBalance(cust.getBalance()+Double.parseDouble(resp.getAmount())/100/flatRate);
                    custrepo.edit(cust);
                    
                    
                     try {                
                         String msg="Dear "+cust.getFirstName()+", Your account has been credited with "+Double.parseDouble(resp.getAmount())/100/flatRate+"units;";
                         smsrepo.sendSMS(ResponseCode.HEADER, msg, null, cust.getPhone(), new Date());
                     } catch (ProcessingException ex) {
                         Logger.getLogger(TaskBean.class.getName()).log(Level.SEVERE, null, ex);
                     }
                    
                    Rtx.setBalanceAfter(cust.getBalance());                       
                    Rtx.setAmount(Double.parseDouble(resp.getAmount()));
                    Rtx.setCreditType("QT TopUp");		      	    
                    Rtx.setCustomer(cust.getUsername());
                    Rtx.setSource("Quickteller");
                    Rtx.setTransDate(new Date());
                    Rtx.setDescription(qt.getTxRef());                        
                    tranxrepo.create(Rtx);                    
                 }
                 else if(resp.isHonour()){
                     qt.setRespCode(resp.getResponseCode());                     
                     qt.setRequestStatus(QTStatus.FAILED.ordinal());
                     if(resp.getDescription()!=null&&!resp.getDescription().isEmpty()&&resp.getDescription().trim().length()>4){                    
                    qt.setRespDesc(resp.getDescription());                    
                    }
                 }                 
                 
             }else{
                     qt.setRequestStatus(QTStatus.ERROR.ordinal());
                 }
                 qtrepo.edit(qt);
             }
             }
             else{
                 System.out.println("No pending tranx");
             }
         
        }
    
    
}
