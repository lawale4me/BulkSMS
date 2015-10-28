/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beans;


import com.core.ProcessingException;
import com.dao.AddressBookRepo;
import com.dao.AdminRepo;
import com.dao.BatchRepo;
import com.dao.CustomerRepo;
import com.entities.Addressbook;
import com.entities.Adminusers;
import com.entities.Batch;
import com.entities.Customer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Ahmed
 */
@ManagedBean
@ViewScoped
public class AddressBookMBean {

    
    public AddressBookMBean() {
    }

    private String name,phonenumber,batchname;    
    private Batch batch;
    Addressbook contact;
    private List<Addressbook> contacts;
    private List<Batch>batchList;
    private UploadedFile file,myfile;
    String username;
    Adminusers user;
    Customer customer;
    @Inject
    AddressBookRepo addressrepo;
    @Inject
    AdminRepo adminrepo;
    @Inject
    CustomerRepo customerrepo;
    @Inject
    BatchRepo batchrepo;
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);
        username=(String) httpSession.getAttribute("username");
        customer=customerrepo.findByUsername(username);
        contacts = addressrepo.getContacts(customer);
        batchList = batchrepo.getBatchList(customer);
    }



    public String addAction() throws ProcessingException {
        Addressbook ad = new Addressbook();
        ad.setBatchId(batch);
        ad.setName(name);
        ad.setOwnerId(customer);
        ad.setPhonenumber(phonenumber);
        
        addressrepo.create(ad);
        contacts.add(ad);

        name = null;
        phonenumber=null;        
        return null;
    }

    public String addAction2() throws ProcessingException {
        System.out.println("addAction222222222");   
                
        int count=0,icount=0;
        if (file != null) {
            
            ArrayList<Addressbook> addBooks=new ArrayList<Addressbook>();
            ArrayList<String> Strs = new ArrayList<String>();
            if(file.getFileName().endsWith("xls")||file.getFileName().endsWith("xlsx"))
            {
               addBooks=readExcel(file);
            }
            else
            {
            String line = "";
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(file.getInputstream()));
            
            while ((line = br.readLine()) != null) {
                System.out.println("Contact:" + line);
                Strs.add(line);
            }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
                for (String str : Strs) {                
                String[] arr=str.split(",");
                Addressbook ad = new Addressbook();                
                ad.setName(arr.length>1?arr[1]:"");                
                ad.setPhonenumber(arr[0]);
                addBooks.add(ad);
                }
            
            }
            
            
            Batch b=new Batch();
            b.setBatchdate(new Date());
            b.setBatchname(batchname);
            b.setBatchowner(customer);           
            batchrepo.create(b);
            for (Addressbook ad : addBooks) 
            {   
                ad.setOwnerId(customer);                
                if(ad.getPhonenumber()!=null&&ad.getPhonenumber().startsWith("2347")||ad.getPhonenumber().startsWith("2348")||ad.getPhonenumber().startsWith("2349")&&ad.getPhonenumber().length()==13)
                {                                                    
                ad.setBatchId(b);                
                addressrepo.create(ad);
                contacts.add(ad);
                count++;
                }else{
                   icount++; 
                }
            }
            name = "";
            phonenumber="";
            batchname=null;

            FacesMessage message = new FacesMessage("Successful", file.getFileName() +
                    " was uploaded.\n Total Valid: "+ count+"\n Total Invalid: "+icount);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Error", " File not uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return null;
    }
    
    public String addAction3() throws ProcessingException {
        
        System.out.println("File"+myfile);
        if (myfile != null) {
            ArrayList<String> Strs = new ArrayList<String>();
            String line = "";
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(myfile.getInputstream()));
            
            while ((line = br.readLine()) != null) {
                System.out.println("Contact:" + line);
                Strs.add(line);
            }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            
            
            for (String str : Strs) {                
                String[] arr=str.split(",");
                Addressbook ad = new Addressbook();
                ad.setBatchId(batch);
                ad.setName(arr.length>1?arr[1]:"");
                ad.setOwnerId(customer);
                ad.setPhonenumber(arr[0]);
                addressrepo.create(ad);
                contacts.add(ad);
            }        

            FacesMessage message = new FacesMessage("Successful", myfile.getFileName() + " was uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Error", " File not uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        return null;
    }
    

    public void onEdit(RowEditEvent event) {
         Addressbook ccust=(Addressbook) event.getObject();                
        FacesMessage msg = new FacesMessage("Customer details Edited",ccust.getName());                  
        Addressbook cust=addressrepo.findByAddressbookId(ccust.getAddressbookId());                       
        cust.setName(ccust.getName());        
        cust.setPhonenumber(ccust.getPhonenumber());        
        addressrepo.edit(cust);        
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Contact Deleted");
        Addressbook pr = (Addressbook) event.getObject();
        Addressbook add = addressrepo.findByAddressbookId(pr.getAddressbookId());                
        addressrepo.remove(add);
        contacts.remove(pr);
        contacts = addressrepo.getContacts(customer);
        FacesContext.getCurrentInstance().addMessage(null, msg); 
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Addressbook getContact() {
        return contact;
    }

    public void setContact(Addressbook contact) {
        this.contact = contact;
    }

    public List<Addressbook> getContacts() {
        return contacts;
    }

    public void setContacts(List<Addressbook> contacts) {
        this.contacts = contacts;
    }

    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Adminusers getUser() {
        return user;
    }

    public void setUser(Adminusers user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getBatchname() {
        return batchname;
    }

    public void setBatchname(String batchname) {
        this.batchname = batchname;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public UploadedFile getMyfile() {
        return myfile;
    }

    public void setMyfile(UploadedFile myfile) {
        this.myfile = myfile;
    }

    private ArrayList<Addressbook> readExcel(UploadedFile file) {
        
        ArrayList<Addressbook> adbooks=new ArrayList<Addressbook>();
        InputStreamReader reader = null;
        try {
            
            File myFile = new File(file.getFileName());
            FileUtils.copyInputStreamToFile(file.getInputstream(), myFile);
            FileInputStream fis = new FileInputStream(myFile);
            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext())
            {
                Addressbook add=new Addressbook();
                Row row = rowIterator.next();
               
                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int i=0;
                while (cellIterator.hasNext()) 
                {

                    Cell cell = cellIterator.next();                    
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    System.out.print("Cell:"+i+":"+cell.getStringCellValue() + "\t");
                    switch (cell.getCellType()) 
                    { 
                    case Cell.CELL_TYPE_STRING: 
                    if(i==0){
                          add.setPhonenumber(cell.getStringCellValue());
                        }
                        else{
                            add.setName(cell.getStringCellValue());
                      }                    
                    break;
                        
                    case Cell.CELL_TYPE_NUMERIC: 
                        if(i==0){
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            add.setPhonenumber(cell.getStringCellValue());
                        }                        
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;                    
                    default : 
                    }                                                                     
                    i++;                    
                }
                if(add.getPhonenumber()!=null){
                adbooks.add(add);
                }
          }
            System.out.println(""); 
        } catch (IOException ex) {
            Logger.getLogger(AddressBookMBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {            
        }
        return adbooks;
    }




  
    
    

}
