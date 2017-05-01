/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orange;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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
public class CDR_Rating extends HttpServlet {

    
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
           
            BufferedReader br=null;
            FileReader fr=null;
            
            fr=new FileReader("/home/alex/Desktop/Bill/cdr.txt");
            br=new BufferedReader(fr);
            
            String str = br.readLine();
            System.out.println(""+str);
            String[] cdr=str.split(",");
            
            
            String src_a=cdr[0];
            String dst_b=cdr[1];
            String service_id=cdr[2];
            String amount=cdr[3];
            String time=cdr[4];
            String ex_charges=cdr[5];
            
            System.out.println(src_a+"--"+dst_b+"--"+service_id+"--"+amount+"--"+time+"--"+ex_charges);
            
            br.close();
            fr.close();
            
            pst2=con.prepareStatement("select rp_id,fu_sms,fu_data,fu_on_voice,fu_cross_voice,fu_international_voice from db_billing.customer where phone=?");
            pst2.setString(1,src_a.substring(1,src_a.length()));
            rs2=pst2.executeQuery();
            
            float deduced_fu=0f;
            int rp_id=0;
            int fu_sms=0;
            int fu_data=0;
            int fu_on_voice=0;
            int fu_cross_voice=0;
            int fu_international_voice=0;
            float fee_sms=0f;
            float fee_data=0f;
            float fee_voice_international=0f;
            float fee_voice_on=0f;
            float fee_voice_cross=0f;
            
            while(rs2.next()){
                rp_id=rs2.getInt(1);
                fu_sms=rs2.getInt(2);
                fu_data=rs2.getInt(3);
                fu_on_voice=rs2.getInt(4);
                fu_cross_voice=rs2.getInt(5);
                fu_international_voice=rs2.getInt(6);
                
            }
            
            int executeUpdate1=0;
            
            pst2=con.prepareStatement("select fee_sms,fee_data,fee_voice_international,fee_voice_on,fee_voice_cross from db_billing.rate_plan where rp_id=?");
            
            pst2.setInt(1,rp_id);
            rs2=pst2.executeQuery();
            
            while(rs2.next()){
                fee_sms=rs2.getFloat(1);
                fee_data=rs2.getFloat(2);
                fee_voice_international=rs2.getFloat(3);
                fee_voice_on=rs2.getFloat(4);
                fee_voice_cross=rs2.getFloat(5);
            }
           
           switch(service_id){
                case "1": //voice  voice_on|voice_cross|voice_international
                if(dst_b.substring(0,3).equalsIgnoreCase("010"))
                 {
                     //fu_on_voice>0 &&
                   if( fu_on_voice>=Integer.parseInt(amount)/60)
                   {
                    //update db_billing.customer set fu_sms=fu_sms-Integer.parseInt(amount);
                     pst3 = con.prepareStatement("update db_billing.customer set fu_on_voice=fu_on_voice-? where phone=?");
                     pst3.setInt(1,(int)Math.ceil(Integer.parseInt(amount)/60));
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     deduced_fu=0f;
                   }
                   else if( fu_on_voice<Integer.parseInt(amount)/60 &&Integer.parseInt(amount)/60>0)
                   
                   {
                     deduced_fu=((Integer.parseInt(amount)/60)-fu_on_voice)*fee_voice_on ;
                     fu_on_voice=0;
                     pst3 = con.prepareStatement("update db_billing.customer set fu_on_voice=? where phone=?");
                     pst3.setInt(1,0);
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     
                   
                   }
                   
                  else{
                      deduced_fu=fee_voice_on*(int)Math.ceil(Integer.parseInt(amount)/60);
                       
                    }
                    
                
                    
                 }
                else if(dst_b.substring(0,3).equalsIgnoreCase("012"))
                {
                    //fu_cross_voice>0 &&
                    if( fu_cross_voice>=(Integer.parseInt(amount)/60))
                   {
                    //update db_billing.customer set fu_sms=fu_sms-Integer.parseInt(amount);
                     pst3 = con.prepareStatement("update db_billing.customer set fu_cross_voice=fu_cross_voice-? where phone=?");
                     pst3.setInt(1,(int)Math.ceil(Integer.parseInt(amount)/60));
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     deduced_fu=0f;
                   }
                    else if(fu_cross_voice<Integer.parseInt(amount)/60 &&Integer.parseInt(amount)/60>0)
                    {
                    
                     deduced_fu=((Integer.parseInt(amount)/60)-fu_cross_voice)*fee_voice_cross ;
                     fu_cross_voice=0;
                     pst3 = con.prepareStatement("update db_billing.customer set fu_cross_voice=? where phone=?");
                     pst3.setInt(1,0);
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     
                    }
                    
                    else{
                      deduced_fu=fee_voice_cross*(int)Math.ceil(Integer.parseInt(amount)/60);
                       
                    }
                    
                
                
                
                }
                else  //international call
                {
                    //fu_international_voice>0 &&
                    if( fu_international_voice>=(Integer.parseInt(amount)/60))
                   {
                    //update db_billing.customer set fu_sms=fu_sms-Integer.parseInt(amount);
                     pst3 = con.prepareStatement("update db_billing.customer set fu_international_voice=fu_international_voice-? where phone=?");
                     pst3.setInt(1,(int)Math.ceil(Integer.parseInt(amount)/60));
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     deduced_fu=0f;
                   }
                   else if(fu_international_voice<Integer.parseInt(amount)/60 &&Integer.parseInt(amount)/60>0)
                    {
                    
                     deduced_fu=((Integer.parseInt(amount)/60)-fu_international_voice)*fee_voice_international ;
                     fu_cross_voice=0;
                     pst3 = con.prepareStatement("update db_billing.customer set fu_international_voice=? where phone=?");
                     pst3.setInt(1,0);
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     
                    }
                    
                    else{
                      deduced_fu=fee_voice_cross*(int)Math.ceil(Integer.parseInt(amount)/60);
                       
                    }

                
                
                
                }
                break;
                
                case "2": //sms
            //fu_sms>0 &&     
                   if(fu_sms>=Integer.parseInt(amount))
                   {
                    //update db_billing.customer set fu_sms=fu_sms-Integer.parseInt(amount);
                     pst3 = con.prepareStatement("update db_billing.customer set fu_sms=fu_sms-? where phone=?");
                     pst3.setInt(1,Integer.parseInt(amount));
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     deduced_fu=0f;
                   }
               
                   else if(fu_sms<Integer.parseInt(amount) &&fu_sms>0)
                    {
                    
                     deduced_fu=((Integer.parseInt(amount))-fu_sms)*fee_sms ;
                     fu_sms=0;
                     pst3 = con.prepareStatement("update db_billing.customer set fu_sms=? where phone=?");
                     pst3.setInt(1,0);
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     
                    }
                    
                   else{
                      deduced_fu=fee_sms*Integer.parseInt(amount);
                       
                    }
                    
                break;    
                
                case "3": //data
                
                //fu_data>0 && 
                   if(fu_data>=Integer.parseInt(amount))
                   {
                    //update db_billing.customer set fu_sms=fu_sms-Integer.parseInt(amount);
                     pst3 = con.prepareStatement("update db_billing.customer set fu_data=fu_data-? where phone=?");
                     pst3.setInt(1,Integer.parseInt(amount));
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     deduced_fu=0f;
                   }
                   
                   else if(fu_data<Integer.parseInt(amount) &&fu_data>0)
                    {
                    
                     deduced_fu=((Integer.parseInt(amount))-fu_data)*fee_data ;
                     fu_data=0;
                     pst3 = con.prepareStatement("update db_billing.customer set fu_data=? where phone=?");
                     pst3.setInt(1,0);
                     pst3.setString(2,src_a.substring(1,src_a.length()));
                     executeUpdate1 = pst3.executeUpdate();
                     
                    }
                    
                else{
                      deduced_fu=Float.parseFloat(ex_charges);
                       
                    }    
                     
                break;    
          
            }  //end of the main switch
           
            pst1 = con.prepareStatement("insert into db_billing.transactions(src_a,dst_b,service_id,amount,start_time,ex_charges,deduced_fu,rp_id) values(?,?,?,?,?,?,?,?)");
            pst1.setString(1,src_a.substring(1,src_a.length()));
            pst1.setString(2,dst_b.substring(1,dst_b.length()));
            pst1.setInt(3,Integer.parseInt(service_id));
            pst1.setString(4,amount);
            pst1.setString(5,time);
            pst1.setString(6,ex_charges);
            pst1.setFloat(7,deduced_fu);
            pst1.setInt(8,rp_id);
            
            int executeUpdate = pst1.executeUpdate();
                 
            if(executeUpdate==1 && executeUpdate1==1)
            {    
                resp.getWriter().println("<center><b>The CDR has been Rated succesfully</b></center>");
            }
            else
            {
                resp.getWriter().println("<center><b>Something went Wrong ,please try again</b></center>");
            }
           
        
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CDR_Rating.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    
    
}
