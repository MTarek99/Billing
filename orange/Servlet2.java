/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orange;

import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.String.join;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class Servlet2 extends HttpServlet {

    Connection con ;
    Statement stmt;
    PreparedStatement pst1,pst2,pst3,pst4;
    ResultSet rs1,rs2,rs3,rs4;
   
    
    public void init(ServletConfig config) throws ServletException {
        try {
               
            try {
                Class.forName("org.postgresql.Driver");
                // DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(AddCustServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                //con = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
               con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
               
               
        } catch (SQLException ex) {
                Logger.getLogger(AddCustServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String[] vas_services=req.getParameterValues("vas");
            String query="select vas_id from db_billing.vas where vas_name IN(";
            
            for(int i=0;i<vas_services.length;i++)
            {
                
                query=query.concat("\'"+vas_services[i]+"\'"+",");
            }
            
            query=query.substring(0,query.length()-1);
            query=query.concat(")");
            System.out.println(""+query);
            pst1 = con.prepareStatement(query);
            rs1 = pst1.executeQuery();
            
            int[] vas_ids=new int[vas_services.length];
            int i=0;
            
            while(rs1.next()){
                
                vas_ids[i]=rs1.getInt(1);
                System.out.println(vas_ids[i]);
                i++;
                
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Servlet2.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        
        
    }

}

/*       
//create pdf file
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
              contentStream.setFont(PDType1Font.COURIER_BOLD, 18);
              
              //Setting the leading
              contentStream.setLeading(14.5f);

              //Setting the position for the line
              contentStream.newLineAtOffset(50, 540);
                


//Adding text in the form of string
                  contentStream.showText(text1);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text2);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text3);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text4);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text5);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text6);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text7);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text8);
                  contentStream.newLine();
                  contentStream.newLine();
                  
                  contentStream.showText(text9);
                  contentStream.newLine();
                  
                  
                  contentStream.showText(text91);
                  contentStream.newLine();
                  
                  contentStream.showText(text92);
                  contentStream.newLine();
                  
                  contentStream.showText(text93);
                  contentStream.newLine();
              
                for(int k=0;k<used_service.length;j++)  
                { 
                    contentStream.showText(""+used_service[k]+":"+one_fee[k]+"EGP");
                  
                 
                    contentStream.newLine();
                }
                  
                  contentStream.showText(text10);
                  contentStream.newLine();
                  contentStream.newLine();
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
   
        } catch (SQLException ex) {
            Logger.getLogger(BillServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }
 
    
    
    
    }
    
    


        catch (SQLException ex) {
            Logger.getLogger(BillServlet.class.getName()).log(Level.SEVERE, null, ex);    
    


*/