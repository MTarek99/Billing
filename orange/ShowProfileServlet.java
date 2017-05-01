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
public class ShowProfileServlet extends HttpServlet {
    
    Connection con ;
    Statement stmt;
    PreparedStatement pst;
    ResultSet rs;
    
    
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
            resp.getWriter().println("<html>\n"
                    + "    <head>    \n"
                    + "    </head>\n"
                    + "\n"
                    + "    <body bgcolor=\"orange\">\n"
                    + "    <center>\n"
                    + "        <table border=\"1\"  bordercolor=\"red\" align='center'>\n"
                    + "\n"
                    + "            <tr>\n"
                    + "                <th colspan=\"2\"><b><font size=\"4\" > Orange Profiles and their Prices  </font></b> </th>\n"
                    + "\n"
                    + "            </tr>\n"
                    + "           \n"
                    + "            <tr>\n"
                    + "                <th> <b>Rate Plan </b></th>\n"
                    + "                <th><b> Price     </b> </th>\n"
                    + "            </tr>\n"
                    + "            \n"
                    + "");
            
            
            pst=con.prepareStatement("select rp_name ,rp_fees from db_billing.rate_plan " );
            
            rs= pst.executeQuery();
            
            while(rs.next())
            {
             
                String rpname=rs.getString("rp_name");
                String rpfees=rs.getString("rp_fees");
                
                resp.getWriter().println("\n" +
"            <tr>\n" +
"                <td>\n" +rpname +"</td>\n" +
"                <td>\n" +rpfees+"EGP" +"</td>\n" +
"            </tr>\n" +
"");
                
            }
            
 
            resp.getWriter().println("        \n" +
    "        </table>\n" +
    "    </center>\n" +
    "</body>\n" +
    "</html>\n" +
    "");
        
        } catch (SQLException ex) {
            Logger.getLogger(ShowProfileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    
    
    }
    
}
