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
public class VAS_Servlet extends HttpServlet {


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
    
            String phone = req.getParameter("phone");
            resp.getWriter().println("\n"
                + "<html>\n"
                + "    <head>    \n"
                + "    </head>\n"
                + "\n"
                + "    <body bgcolor=\"orange\">\n"
                + "        <form action='VAS_Servlet2'>\n"
                + "            <center>\n"
                + "                <table border=\"1\"  bordercolor=\"red\">\n"
                + "\n"
                + "                    <tr>\n"
                + "                        <th colspan=\"2\"><b><font size=\"4\" > Assign VAS Services </font></b> </th>\n"
                + "\n"
                + "                    </tr>\n"
                + "\n"
                + "                    <tr>\n"
                + "                        <td> <b>Enter Customer's phone here </b></td>\n"
                + "                        <td><b> <input type=\"text\" name='phone' value="+phone+">    </b> </td>\n"
                + "                    </tr>\n"
                + "\n"
                + "\n"
                + "                    <tr>\n"
                + "                        <td> <b>Choose the Service</b></td>\n"
                + "                        <td><select name='vas'>\n"
                + "");
            
        
            int len = 0;
        
            try {
                
            
            
            pst1=con.prepareStatement("select vas_services from db_billing.customer where phone=?");
            pst1.setString(1,phone);
            rs1=pst1.executeQuery();
            String Vas_services=null;           
            
            while(rs1.next()){
                Vas_services=rs1.getString(1);
            }
            
                String[] Vas_ids = Vas_services.split(",");
                
                Integer[] vas_ids=new Integer[Vas_ids.length];
                
                String query="";
                
                query=query.concat("select vas_name from db_billing.vas where vas_id NOT IN(");
                
                for(int i=0;i<Vas_ids.length;i++){
                    
                  vas_ids[i]= Integer.parseInt(Vas_ids[i]);
                  query=query.concat(""+vas_ids[i]+",");
                }
                       
                query=query.substring(0,query.length()-1);                        
                query=query.concat(")");
                System.out.println(query);
            
              pst2=con.prepareStatement(query);
              
              rs2=pst2.executeQuery();
                      
            String[] Vas_services2=new String[10000];
            int n=0;
            
            while(rs2.next()){
                
                Vas_services2[n]=rs2.getString(1);
                    
               
                n++;
            }    
            
            
            for (int j = 0; j < Vas_services2.length; j++) {
                
                if(Vas_services2[j]==null) break; 
                resp.getWriter().println(" <option >" + Vas_services2[j] + "</option>\n"); 
                 
            }
                
            resp.getWriter().println("                        </select>\n"
                + "                    \n"
                + "                    \n"
                + "                    \n"
                + "                    </td>\n"
                + "\n"
                + "                </tr>\n"
                + "                \n"
                + "                <tr rowspan='2'>\n"
                + "                    \n"
                + "                    \n"
                + "                    <td> <input type=\"reset\" value=\"Reset\" />  </td>\n"
                + "\n"
                + "                    <td> <input type=\"submit\" value=\"submit\" />   </td>\n"
                + "\n"
                + "                </tr>\n"
                + "\n"
                + "\n"
                + "            </table>\n"
                + "\n"
                + "\n"
                + "        </form>   \n"
                + "    </body>\n"
                + "</html>\n"
                + "");
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

        
    








}

    




