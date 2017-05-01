/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orange;

import java.beans.Statement;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.script.ScriptEngine.FILENAME;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class processor extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
        String src_a=req.getParameter("src");
        String dst_b=req.getParameter("dst");
        String service_id=req.getParameter("service_id");
        String amount=req.getParameter("quantity");
        String time=req.getParameter("time");
        String ex_charges=req.getParameter("excharges");
        
        try{
            BufferedWriter bw=null;
            FileWriter fw=null;
            
            fw=new FileWriter("/home/alex/Desktop/Bill/cdr.txt");
            bw=new BufferedWriter(fw);
            String attribute=src_a+","+dst_b+","+service_id+","+amount+","+time+","+ex_charges;
            bw.write(attribute.toString());
            
            bw.close();
            fw.close();
            resp.getWriter().println("<center><b>The CDR has been created successfully in the path /home/alex/Desktop/Bill/cdr.txt </b></center>");
            resp.getWriter().println("<br><center><form action=\"CDR_Rating\"> <input type=\"submit\" value=\"Rate\"> </form>");
                    
        } catch (IOException ex) {
            Logger.getLogger(processor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
		        
                      
	
    
    
    
    
    }

    
    

}
