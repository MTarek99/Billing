/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orange;

import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author alex
 */
public class BillServlet extends HttpServlet {


    Connection con;
    Statement stmt;
    PreparedStatement pst1, pst2,pst3,pst4;
    ResultSet rs1, rs2,rs3,rs4;
    
    public void init(ServletConfig config) throws ServletException {
        try {
            
            try {
                Class.forName("org.postgresql.Driver");
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AddCustServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            //con = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            
            System.out.println("connected");
        } catch (SQLException ex) {
            Logger.getLogger(AddCustServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      
            
        try {
            String phone=req.getParameter("phone");
            
            pst1 = con.prepareStatement("select sum(deduced_fu) from db_billing.transactions where src_a=?");
            pst1.setString(1, phone);
            rs1=pst1.executeQuery();
            float cost_after_FU=0f;
            
            while(rs1.next()){
                cost_after_FU=rs1.getFloat(1);
            }
            
            
            pst1 = con.prepareStatement("select * from db_billing.customer where phone=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst1.setString(1, phone);
            rs1 = pst1.executeQuery();
            int cus_id=0;
            String cus_name="";
            String email="";
            String postal_code="";
            String address="";
            int ssn=0;
            String gender="";
            int rp_id=0;
            
            while (rs1.next()) {
                cus_id= rs1.getInt(1);
                cus_name=rs1.getString(2);
                email=rs1.getString(3);
                postal_code=rs1.getString(4);
                address=rs1.getString(5);
                ssn=rs1.getInt(6);
                gender=rs1.getString(7);
                rp_id=rs1.getInt(8);
            }
            
            pst4=con.prepareStatement("insert into db_billing.billing (cus_id,total_fees,one_fee,used_service) values(?,?,?,?)");
            pst4.setInt(1,cus_id);
            pst4.setFloat(2,cost_after_FU);
            pst4.setFloat(3, cost_after_FU);
            pst4.setString(4,"Usage after Free Units ");
            
            int executeUpdate = pst4.executeUpdate();
            if(executeUpdate==1){System.out.println("cdr calculated in billing now");}
            pst2 = con.prepareStatement("select rp_name,rp_fees from db_billing.rate_plan where rp_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst2.setInt(1,rp_id);
            rs2 = pst2.executeQuery();
            String rp_name="";
            float rp_fees=0f;
            
            while(rs2.next()){
                rp_name=rs2.getString(1);
                rp_fees=rs2.getFloat(2);
            }
            
            pst4 = con.prepareStatement("select sum(total_fees),count(total_fees) from db_billing.billing where cus_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst4.setInt(1,cus_id);
            float total_fees=0;
            rs4 = pst4.executeQuery();
            int count=0; 
            
            while(rs4.next()){
                total_fees=rs4.getFloat(1);
                count=rs4.getInt(2);
                
            }
            
            
            
            pst3 = con.prepareStatement("select one_fee,used_service from db_billing.billing where cus_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst3.setInt(1,cus_id);
            
            rs3 = pst3.executeQuery();
            
            float[] one_fee=new float[count];
            String[] used_service=new String[count];
            int j=0;
            
            while(rs3.next()){
                
                one_fee[j]=rs3.getFloat(1);
                used_service[j]=rs3.getString(2);
                
            }
            
            //cus_name instead of test
            String text1 = ("Customer Name: "+cus_name);
            
            String text2 = ("Mobile Number: "+phone);
            String text3 = ("Email: "+email);
            String text4 = ("Address: "+address);
            String text5 = ("Postal Code: "+postal_code);
            String text6 = ("National ID: "+ssn);
            String text7 = ("Gender: "+gender);
            String text8 = ("RatePlan Name: "+rp_name);
            String text9 = ("Total Fees: "+(Math.round(((total_fees+cost_after_FU)+0.1*(total_fees+cost_after_FU)))*100.0)/100.0+"  EGP");
            String text91=("Invoice Details:");
            String text92=("Rate Plan Fees:"+rp_fees+"  EGP");
            String text93=("Taxes(10%):"+Math.round(0.1*(total_fees+cost_after_FU)*100.0)/100.0+"  EGP");
            String text94=("Usage after free Units:"+cost_after_FU+"  EGP");
            String text95=("Date: "+java.time.LocalDate.now().toString());
            String text10 = ("************************************************************************");
            String text11 = ("  Thanks for being a part of Wind. ");
        
            System.out.println(""+text1+text2+text3+text4+text5+text6+text7+text8+text9+text91+text92+text93+
            text10+text11);
        
         //Creating a blank page
            //Creating PDF document object
            try ( PDDocument document = new PDDocument())
            {
                //Creating a blank page
                PDPage blankPage = new PDPage();
                
                //Adding the blank page to the document
                document.addPage(blankPage);
                
                //Creating the PDDocumentInformation object
                PDDocumentInformation pdd = document.getDocumentInformation();
                
                //Setting the author of the document
                pdd.setAuthor("Mohamed Tarek");
                
                // Setting the title of the document
                pdd.setTitle("User's invoice");
                
                //Setting the creator of the document
                pdd.setCreator("Billing System Invoice");
                
                //Setting the subject of the document
                pdd.setSubject("Test");
                
                //Setting the created date of the document
                Calendar date = new GregorianCalendar();
                date.set(2017,4,7);
                pdd.setCreationDate(date);
                
                //Setting the modified date of the document
                date.set(2017,4,8);
                pdd.setModificationDate(date);
                
                //Setting keywords for the document
                pdd.setKeywords("Name,PhoneNo.");
                
                System.out.println("Properties added");
                
                //Saving the document
                document.save("/home/alex/Desktop/Bill/"+cus_name+".pdf");
                System.out.println("PDF created");
                
                //Closing the document
                document.close();
            }
        
        
        
      //Loading an existing document
      File file = new File("/home/alex/Desktop/Bill/"+cus_name+".pdf");
      
        //Retrieving the pages of the document
        try (PDDocument document = PDDocument.load(file))
        {
            //Retrieving the pages of the document
            PDPage page = document.getPage(0);
            
            //Creating PDImageXObject object
             PDImageXObject pdImage = PDImageXObject.createFromFile("/home/alex/Desktop/Bill/wind.jpg",document);
        
       
            //Begin the Content stream
          try (PDPageContentStream contentStream = new PDPageContentStream(document, page))
          {         
              
             //Drawing the image in the PDF document
             contentStream.drawImage(pdImage, 180, 550);

             System.out.println("Image inserted");
   
             
              //Begin the Content stream
              contentStream.beginText();
              
              //Setting the font to the Content stream
              contentStream.setFont(PDType1Font.TIMES_ROMAN, 14);
              
              //Setting the leading
              contentStream.setLeading(14.5f);

              //Setting the position for the line
              contentStream.newLineAtOffset(50, 540);
                
                 String test= "massry";
                 System.out.println(test);
                 
                  //Adding text in the form of string
                  contentStream.showText(text95);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text1);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text2);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text3);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text4);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text5);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text6);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text7);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text8);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text9);
                  contentStream.newLine();
                  contentStream.newLine();
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text91);
                  contentStream.newLine();
                  
                  contentStream.showText(text92);
                  contentStream.newLine();
                  
                  contentStream.showText(text93);
                  contentStream.newLine();
                  
                  contentStream.showText(text94);
                  contentStream.newLine();
                  
                  contentStream.showText(text10);
                  contentStream.newLine();
                  
                  contentStream.showText(text11);
                  contentStream.newLine();
                  
                  //Ending the content stream
                 contentStream.endText();

                  System.out.println("Content added");

                 //Closing the content stream
                 contentStream.close();
          }
          
            //Saving the document
            document.save(new File("/home/alex/Desktop/Bill/"+cus_name+".pdf"));
            
            //Closing the document
            document.close();
     }
 
     resp.getWriter().println("<center><b> The Bill has been exported in the path /home/alex/Desktop/Bill");
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        } catch (SQLException ex) {
            Logger.getLogger(BillServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
               
          }
        
        
        }