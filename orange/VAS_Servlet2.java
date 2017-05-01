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
 * @author alex
 */
public class VAS_Servlet2 extends HttpServlet {

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
            String vas_name=req.getParameter("vas");
            
            
            pst1=con.prepareStatement("select vas_services,cus_id from db_billing.customer where phone=?");
            pst1.setString(1,phone);
            rs1=pst1.executeQuery();
            String vas_services=null;           
            int cus_id=0;
            
            while(rs1.next()){
                vas_services=rs1.getString(1);  
                cus_id=rs1.getInt(2);
            }
        
            pst2=con.prepareStatement("select vas_id,vas_fees from db_billing.vas where vas_name=?");
            pst2.setString(1,vas_name);
            rs2=pst2.executeQuery();
                      
            int vas_id=0;
            float vas_fees=0;
            
            while(rs2.next()){
                vas_id=rs2.getInt(1);
                vas_fees=rs2.getFloat(2);
            }
        
           vas_services=vas_services.concat(","+""+Integer.toString(vas_id));
           
            pst3=con.prepareStatement("update db_billing.customer set vas_services=?  where cus_id=?");
            pst3.setString(1,vas_services);
            pst3.setInt(2,cus_id);
            int executeUpdate = pst3.executeUpdate();
            
            
            pst4=con.prepareStatement("insert into db_billing.billing(cus_id,total_fees,one_fee,fees_tax,used_service)values(?,?,?,?,?)");
            pst4.setInt(1,cus_id);
            pst4.setFloat(2,vas_fees);
            pst4.setFloat(3,vas_fees);
            pst4.setFloat(4,0);
            pst4.setString(5, vas_name);
            int executeUpdate1 = pst4.executeUpdate();
           
            
            
            if(executeUpdate==1&&executeUpdate1==1)
            {    
                resp.getWriter().println("<center><b>Your Request has been submitted succesfully</b></center>");
            }
            else{
                resp.getWriter().println("<center><b>Something went Wrong ,please try again</b></center>");
            }
            pst4.close();
            pst3.close();
            pst2.close();
            pst1.close();
        } catch (SQLException ex) {
            Logger.getLogger(VAS_Servlet2.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    
    
    }
    
   


}
