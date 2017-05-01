/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orange;

import java.beans.Statement;
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
 * @author USERNAME
 */
public class AddCustServlet extends HttpServlet {

    Connection con ;
    Statement stmt;
    PreparedStatement pst,pst2,pst3,pst4;
    ResultSet rs,rs2,rs3,rs4;
   
    
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
            
            String name = req.getParameter("name");
            String phone = req.getParameter("phone");
            String email = req.getParameter("email");
            String zip = req.getParameter("zip");
            String SSN = req.getParameter("ssn");
            String address = req.getParameter("address");
            String gender=req.getParameter("gender");
            String rateplan = req.getParameter("rp");
            
            pst2=con.prepareStatement("select rp_id,rp_fees,fu_sms,fu_data,fu_on_voice,fu_cross_voice,fu_international_voice from db_billing.rate_plan where rp_name=?" );
            pst2.setString(1,rateplan);
            rs2= pst2.executeQuery();
            
            int rp_id = 0 ;
            float rp_fees=0;
            int fu_sms=0;
            int fu_data=0;
            int fu_on_voice=0;
            int fu_cross_voice=0;
            int fu_international_voice=0;
            
            while(rs2.next()){
                rp_id=rs2.getInt(1);
                rp_fees=rs2.getFloat(2);
                fu_sms=rs2.getInt(3);
                fu_data=rs2.getInt(4);
                fu_on_voice=rs2.getInt(5);
                fu_cross_voice=rs2.getInt(6);
                fu_international_voice=rs2.getInt(7);
            
            }
           
            
            pst3=con.prepareStatement("select vas_id from db_billing.vas_rp where rp_id=?" );
            pst3.setInt(1,rp_id);
            rs3= pst3.executeQuery();
            int i=0;
            Integer[] Vas_ids=new Integer[200];
            
            while(rs3.next()){ 
                Vas_ids[i]=rs3.getInt(1);
                i++;
            }
            


            pst=con.prepareStatement("insert into db_billing.customer(cus_name,email,postal_code,address,phone,ssn,gender,rp_id,vas_services,fu_sms,fu_data,fu_on_voice,fu_cross_voice,fu_international_voice) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst.setString(1,name);
            pst.setString(2,email);
            pst.setInt(3,Integer.parseInt(zip));
            pst.setString(4,address);
            pst.setInt(5,Integer.parseInt(phone));
            pst.setInt(6,Integer.parseInt(SSN));
            pst.setString(7, gender);
            pst.setInt(8, rp_id);
            pst.setString(9,Vas_ids[0].toString());
            pst.setInt(10, fu_sms);
            pst.setInt(11, fu_data);
            pst.setInt(12, fu_on_voice);
            pst.setInt(13, fu_cross_voice);
            pst.setInt(14, fu_international_voice);
            
            
            int executeUpdate = pst.executeUpdate();
            
            int cus_id=0;
            pst2=con.prepareStatement("select cus_id from db_billing.customer where cus_name=?" );
            pst2.setString(1,name);
            rs2= pst2.executeQuery();
            while(rs2.next()){
                cus_id=rs2.getInt(1);
            
            }
            
            pst4=con.prepareStatement("insert into db_billing.billing(cus_id,total_fees,one_fee,fees_tax,used_service)values(?,?,?,?,?)");
            pst4.setInt(1,cus_id);
            pst4.setFloat(2,rp_fees);
            pst4.setFloat(3,rp_fees);
            pst4.setFloat(4,0);
            pst4.setString(5,"Monthly fees for the RatePlan Cost");
            int executeUpdate1 = pst4.executeUpdate();
           
            
            
            
            if(executeUpdate==1 && executeUpdate1==1)
            {    
                resp.getWriter().println("<center><b>Your Request has been submitted succesfully</b></center>");
            }
            else{
                resp.getWriter().println("<center><b>Something went Wrong ,please try again</b></center>");
            }
            pst.close();
            pst3.close();
            pst2.close();
        } catch (SQLException ex) {
            Logger.getLogger(AddCustServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
   
   
   
   
    
    
}
