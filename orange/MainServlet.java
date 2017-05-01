package orange;

import java.beans.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class MainServlet extends HttpServlet {
    
    Connection con;
    Statement stmt;
    PreparedStatement pst1, pst2;
    ResultSet rs1, rs2;
    
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
            
            String btn = req.getParameter("btn");
            int len = 0;
            
            switch (btn) {
                
                case "add":  //done

                    pst1 = con.prepareStatement("select count(rp_name) from db_billing.rate_plan", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs1 = pst1.executeQuery();
                    
                    while (rs1.next()) {
                        len = rs1.getInt(1);
                    }
                    
                    pst2 = con.prepareStatement("select rp_name from db_billing.rate_plan", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs2 = pst2.executeQuery();
                    String[] RP = new String[len];
                    int i = 0;
                    
                    while (rs2.next()) {
                        RP[i] = rs2.getString(1);
                        System.out.println(RP[i]);
                        i++;
                    }
                    
                    resp.getWriter().println("<html>\n"
                            + "\n"
                            + "    <head>\n"
                            + "\n"
                            + "    </head>\n"
                            + "\n"
                            + "    <body bgcolor=#C0C0C0>\n"
                            + "\n"
                            + "        <form action=\"AddCustServlet\">            \n"
                            + "            <table border=\"1\"  align=\"center\"  bordercolor=\"red\">    \n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <th colspan=\"2\"> <b><font size=\"6\"> Enter Customer's Info      </font>\n"
                            + "                        </b>\n"
                            + "                    </th>\n"
                            + "                </tr>    \n"
                            + "\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Name   </td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"name\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <td>Phone</td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"phone\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <td>Email</td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"email\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <td>Postal-Code</td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"zip\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>National ID</td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"ssn\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Address</td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"address\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Gender</td>\n"
                            + "\n"
                            + "                    <td><select name=\"gender\">"
                            + "<option>male</option>"
                            + "<option>female</option>"
                            + "</select>"
                            + "   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Rate Plan</td>\n"
                            + "\n"
                            + "                    <td>\n"
                            + "                        <select name=\"rp\">   \n");
                    for (int j = 0; j < RP.length; j++) {
                        
                        resp.getWriter().println(" <option >" + RP[j] + "</option>\n");
                        
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
                    break;
                case "search":  //done
                    resp.sendRedirect("search.html");
                    break;
                case "show":   //jasper not yet
                    resp.getWriter().println("<html>\n"
                            + "\n"
                            + "    <head>\n"
                            + "\n"
                            + "    </head>\n"
                            + "\n"
                            + "    <body bgcolor=#C0C0C0>\n"
                            + "\n"
                            + "        <form action=\"BillServlet\">            \n"
                            + "            <table border=\"1\"  align=\"center\"  bordercolor=\"red\">    \n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <th colspan=\"2\"> <b><font size=\"6\"> Export Customer's Bill      </font>\n"
                            + "                        </b>\n"
                            + "                    </th>\n"
                            + "                </tr>    \n"
                            + "\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Customer's MSISDN   </td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"phone\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "\n"
                            + "                \n"
                            + "                <tr rowspan='2'>\n"
                            + "                    \n"
                            + "                    \n"
                            + "                    <td> <input type=\"reset\" value=\"Reset\" />  </td>\n"
                            + "\n"
                            + "                    <td> <input type=\"submit\" value=\"Export\" />   </td>\n"
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
                            + "\n"
                            + "");
                    break;
                case "create":   //create new Rate_Plan
                    resp.getWriter().println("\n"
                            + "<html>\n"
                            + "    <head>\n"
                            + "        <title>Create New Rate Plan</title>\n"
                            + "\n"
                            + "    </head>\n"
                            + "    <body bgcolor=\"pink\">\n"
                            + "\n"
                            + "        <form action=\"CreateRP\">\n"
                            + "            <center>\n"
                            + "                <table>\n"
                            + "                    <tr>\n"
                            + "                        <th colspan=\"2\">\n"
                            + "                    <h3>Create New Profile</h3>\n"
                            + "                    </th>\n"
                            + "\n"
                            + "                    <tr>\n"
                            + "\n"
                            + "                    <tr>    \n"
                            + "                        <td>\n"
                            + "                            <b>Rate Plan Name\n"
                            + "                        </td>   \n"
                            + "                        <td>\n"
                            + "                            <input type=\"text\" name=\"rp_name\">\n"
                            + "                        </td>   \n"
                            + "\n"
                            + "                    </tr>    \n"
                            + "\n"
                            + "                    <tr>    \n"
                            + "                        <td><b>Services</td>\n"
                            + "                        <td>\n"
                            + "                            <br><input type=\"checkbox\" name=\"service1\" value=\"voice\">Voice<br>\n"
                            + "                            <input type=\"checkbox\" name=\"service2\" value=\"sms\">SMS<br>\n"
                            + "                            <input type=\"checkbox\" name=\"service3\" value=\"data\">Data<br>\n"
                            + "                        </td> </tr>  \n"
                            + "\n"
                    );
                    
                    pst1 = con.prepareStatement("select vas_name from db_billing.vas ");
                    rs1 = pst1.executeQuery();
                    String[] Vas_services = new String[100];
                    int l = 0;
                    
                    while (rs1.next()) {
                        
                        Vas_services[l] = rs1.getString(1);
                        l++;
                    }
                    
                    resp.getWriter().println("<tr><B>VAS Services</B></tr> <tr>");
                    
                    for (int j = 0; j < Vas_services.length; j++) {
                        if (Vas_services[j] == null) {
                            break;
                        }
                        
                        resp.getWriter().println("<br><input type=\"checkbox\" "
                                + "name=\"vas\"value=" + Vas_services[j].replaceAll(" ", "") + ">" + Vas_services[j]);
                    }
                    
                    resp.getWriter().println("</tr>\n"
                            + "\n"
                            + "                    <tr>\n"
                            + "                        <td colspan=\"2\"><B>Voice Rating in Piasters</B></td>  \n"
                            + "                    </tr>    \n"
                            + "                    <ul>\n"
                            + "                    \n"
                            + "                        <tr>\n"
                            + "                            <td > <li>  International calls   </li>         </td> \n"
                            + "                        <td> <input type=\"text\" name=\"intvoicer\"></td>\n"
                            + "                    \n"
                            + "                        </tr>   \n"
                            + "                    \n"
                            + "                    <tr>\n"
                            + "                        <td><li>On-Net calls          </li>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"onvoicer\"></td>\n"
                            + "                    </tr>\n"
                            + "                    \n"
                            + "                    <tr>\n"
                            + "                        <td><li>Cross-Net calls  </li>             </td> \n"
                            + "                        <td> <input type=\"text\" name=\"crossvoicer\"></td>\n"
                            + "                    </tr>\n"
                            + "                        </ul>\n"
                            + "                    <tr>\n"
                            + "                        <td><B>SMS Rating in Piasters            </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"smsr\"></td>\n"
                            + "                    </tr>\n"
                            + "                    <tr>\n"
                            + "                        <td><B>External Data Charges per MB  </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"datar\"></td>\n"
                            + "                    </tr>\n"
                            + "                    <tr>\n"
                            + "                        <td colspan=\"2\"><B>Free Units</B></td>  \n"
                            + "                    </tr>\n"
                            + "                    <tr>\n"
                            + "                        <td><B>Data FU in MB  </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"fudata\"></td>\n"
                            + "                    </tr>\n"
                            + "                    <tr>\n"
                            + "                        <td><B>On-net voice in Min </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"fuonvoice\"></td>\n"
                            + "                    </tr>\n"
                            + "                    \n"
                            + "                    <tr>\n"
                            + "                        <td><B>Cross-net voice in Min </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"fucrossvoice\"></td>\n"
                            + "                    </tr>\n"
                            + "                     <tr>\n"
                            + "                        <td><B>International voice in Min </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"fuintvoice\"></td>\n"
                            + "                    </tr>\n"
                            + "                     <tr>\n"
                            + "                        <td><B>SMS Free Units  </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"fusms\"></td>\n"
                            + "                    </tr>\n"
                            + "                     <tr>\n"
                            + "                        <td><B>Rate Plan Fees </b>  </td> \n"
                            + "                        <td> <input type=\"text\" name=\"rpfees\"></td>\n"
                            + "                    </tr>\n"
                            + "\n"
                            + "                    <tr rowspan='2'>\n"
                            + "                    \n"
                            + "                    \n"
                            + "                    <td align=\"right\"> <input type=\"reset\" value=\"Reset\" />  </td>\n"
                            + "\n"
                            + "                    <td align=\"right\"> <input type=\"submit\" value=\"create\" />   </td>\n"
                            + "\n"
                            + "                </tr>   \n"
                            + "\n"
                            + "\n"
                            + "\n"
                            + "                </table>\n"
                            + "\n"
                            + "            </center>\n"
                            + "\n"
                            + "        </form>\n"
                            + "    </body>\n"
                            + "</html>");
                    
                    break;
                
                case "showprofile":  //done
                    resp.sendRedirect("ShowProfileServlet");
                    break;
                
                case "assign"://to assign recurring services like vas service that is paid one time monthly

                    resp.getWriter().println("\n"
                            + "<html>\n"
                            + "    <head>    \n"
                            + "    </head>\n"
                            + "\n"
                            + "    <body bgcolor=\"orange\">\n"
                            + "        <form action='VAS_Servlet'>\n"
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
                            + "                        <td><b> <input type=\"text\" name='phone'>    </b> </td>\n"
                            + "                    </tr>\n"
                            + "\n"
                            + "\n"
                            + "                    <tr>\n"
                            + "                        <td> <b>Choose the Service</b></td>\n"
                            + "                        <td><select name='vas'>\n"
                            + "");
                    
                    resp.getWriter().println("<option> No available Services<option> </select>\n"
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
                            + "                    <td> <input type=\"submit\" value=\"next\" />   </td>\n"
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
                    
                    break;
                
                case "assign1":  //to assign one time fee like buying new router
                    resp.getWriter().println("<html>\n"
                            + "    <head>\n"
                            + "        <title>Assign one time fee for a customer </title>\n"
                            + "\n"
                            + "    </head>\n"
                            + "    <body bgcolor=\"pink\">\n"
                            + "        <form action=\"onetimeServlet\">   \n"
                            + "            <table border=\"1\" align=\"center\"  bordercolor=\"red\" >    \n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <th colspan=\"2\"> <b><font size=\"6\"> Assign one Time fee      </font>\n"
                            + "                        </b>\n"
                            + "                    </th>\n"
                            + "                </tr>    \n"
                            + "\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "\n"
                            + "                    <td>Customer's Phone  </td>\n"
                            + "\n"
                            + "                    <td><input type=\"text\" name=\"phone\">   </td>\n"
                            + "\n"
                            + "                </tr>\n"
                            + "\n"
                            + "                <tr rowspan='2'>\n"
                            + "                    <td>Choose the service </td>\n"
                            + "\n"
                            + "                    <td>\n"
                            + "                        <select name='service'> ");
                    
                    pst1 = con.prepareStatement("select count(service_name) from db_billing.one_fee", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs1 = pst1.executeQuery();
                    
                    while (rs1.next()) {
                        len = rs1.getInt(1);
                    }
                    
                    pst2 = con.prepareStatement("select service_name from db_billing.one_fee", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    rs2 = pst2.executeQuery();
                    String[] Ser = new String[len];
                    int k = 0;
                    
                    while (rs2.next()) {
                        Ser[k] = rs2.getString(1);
                        System.out.println(Ser[k]);
                        k++;
                    }
                    
                    for (int j = 0; j < Ser.length; j++) {
                        
                        resp.getWriter().println(" <option >" + Ser[j] + "</option>\n");
                        
                    }
                    
                    resp.getWriter().println("                    </select>\n"
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
                            + "                    <td> <input type=\"submit\" value=\"assign\" />   </td>\n"
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
                    
                    break;
                
                case "proceed":
                    resp.sendRedirect("cdr.html");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
