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
public class CreateRP extends HttpServlet {

    
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
            
            String rp_name=req.getParameter("rp_name");
            String s_voice,s_sms,s_data;
            if(req.getParameter("service1")!=null){s_voice="true";}else{s_voice="false";}
            if(req.getParameter("service2")!=null){s_sms="true";}else{s_sms="false";}
            if(req.getParameter("service3")!=null){s_data="true";}else{s_data="false";}
           
            String fee_voice_international=req.getParameter("intvoicer");
            String fee_voice_on=req.getParameter("onvoicer");
            String fee_voice_cross=req.getParameter("crossvoicer");
            String fee_sms=req.getParameter("smsr");
            String fee_data=req.getParameter("datar");
            String fu_data=req.getParameter("fudata");
            String fu_on_voice=req.getParameter("fuonvoice");
            String fu_cross_voice=req.getParameter("fucrossvoice");
            String fu_international_voice=req.getParameter("fuintvoice");
            String fu_sms=req.getParameter("fusms");
            String rp_fees=req.getParameter("rpfees");
            
            
            pst2 = con.prepareStatement("insert into db_billing.rate_plan(rp_name,s_sms,s_voice,s_data,fu_sms"
                    + ",fu_voice,fu_data,fu_on_voice,fu_cross_voice,fu_international_voice,rp_fees,fee_sms,fee_data"
                    + ",fee_voice_international,fee_voice_on,fee_voice_cross) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            pst2.setString(1,rp_name);
            pst2.setString(2,s_sms);
            pst2.setString(3,s_voice);
            pst2.setString(4,s_data);
           
            pst2.setInt(5,Integer.parseInt(fu_sms));
            pst2.setInt(6,100);
            pst2.setInt(7,Integer.parseInt(fu_data));
            pst2.setInt(8,Integer.parseInt(fu_on_voice));
            pst2.setInt(9,Integer.parseInt(fu_cross_voice));
            pst2.setInt(10,Integer.parseInt(fu_international_voice));
            
            pst2.setFloat(11,Float.parseFloat(rp_fees));
            pst2.setFloat(12,Float.parseFloat(fee_sms)/100f);
            pst2.setFloat(13,Float.parseFloat(fee_data)/100f);
            pst2.setFloat(14,Float.parseFloat(fee_voice_international)/100f);
            pst2.setFloat(15,Float.parseFloat(fee_voice_on)/100f);
            pst2.setFloat(16,Float.parseFloat(fee_voice_cross)/100f);
            
            
            
            int executeUpdate = pst2.executeUpdate();
            
            
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
        
            pst3 = con.prepareStatement("select rp_id from db_billing.rate_plan where rp_name=?");
            pst3.setString(1,rp_name);
            rs3 = pst3.executeQuery();
            int rp_id=0;
            while(rs3.next())
            {
                rp_id=rs3.getInt(1);
            }
            int executeUpdate1=0;
            
            for(int j=0;j<vas_services.length;j++)
            {
                pst4=con.prepareStatement("insert into db_billing.vas_rp(rp_id,vas_id) values(?,?)");
                pst4.setInt(1,rp_id);
                pst4.setInt(2,vas_ids[j]);
                executeUpdate1 = pst4.executeUpdate();
            }
            
            if(executeUpdate==1&&executeUpdate1==1)
            {    
                resp.getWriter().println("<center><b>Your Request has been submitted succesfully</b></center>");
            }
            else
            {
                resp.getWriter().println("<center><b>Something went Wrong ,please try again</b></center>");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(CreateRP.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }


    
}
