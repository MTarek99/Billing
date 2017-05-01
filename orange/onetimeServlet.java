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
public class onetimeServlet extends HttpServlet {

      //get the cus_id using  phone query in customer table 
    //get the sevice_id using  service query in one_fee table
    //insert a record in third table and never forget to update the price of service in Billing 
    Connection con;
    PreparedStatement pst1,pst2,pst3,pst4,pst5;
    ResultSet rs1,rs2,rs3,rs4;

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
           
           // resp.setContentType("text/html");

            
            String service = req.getParameter("service");
            String phone = req.getParameter("phone");
            
                      
            int service_id=0;
            int cus_id=0;
            

            pst2 = con.prepareStatement("select cus_id from db_billing.customer where phone=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst2.setString(1,phone);
            rs2 = pst2.executeQuery();

            while (rs2.next()) {
                
                cus_id = rs2.getInt("cus_id");
                
            }
            
            pst1 = con.prepareStatement("select service_id from db_billing.one_fee where service_name=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst1.setString(1,service);
            rs1 = pst1.executeQuery();

            while (rs1.next()) {
                service_id = rs1.getInt(1);
                System.out.println(""+service_id);
            }
            
            pst3 = con.prepareStatement("insert into db_billing.cus_one_fee values(?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst3.setInt(1,cus_id);
            pst3.setInt(2,service_id);
            int executeUpdate = pst3.executeUpdate();
            
            
            
            float one_fee=0;
            pst5=con.prepareStatement("select service_fees from db_billing.one_fee where service_name=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst5.setString(1,service);
            rs4=pst5.executeQuery();
            
            while(rs4.next()){
            
               one_fee=rs4.getFloat(1);
            }
            
            
            pst4 = con.prepareStatement("insert into db_billing.billing (total_fees,one_fee,cus_id,used_service,fees_tax) values(?,?,?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst4.setFloat(1,one_fee);
            pst4.setFloat(2,one_fee);
            pst4.setInt(3,cus_id);
            pst4.setString(4,service);
            pst4.setFloat(5,0);
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
            Logger.getLogger(onetimeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
