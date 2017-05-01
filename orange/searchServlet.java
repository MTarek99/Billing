package orange;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import org.apache.derby.client.am.Connection;
//import org.apache.derby.client.am.ResultSet;
import static org.apache.tomcat.jni.Buffer.address;
import static org.eclipse.jdt.internal.compiler.parser.Parser.name;

/**
 *
 * @author USERNAME
 */
public class searchServlet extends HttpServlet {
    
    Connection con;
    
    PreparedStatement pst, pst1;
    ResultSet rs, rs1;
    
    @Override
    public void init() throws ServletException {
        try {
            
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(searchServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");
        try {
            String phone = req.getParameter("phone");
            System.out.println("" + phone);
            pst = con.prepareStatement("select * from db_billing.customer,db_billing.rate_plan "
                    + "where phone=? AND db_billing.customer.rp_id=db_billing.rate_plan.rp_id", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            pst.setString(1, phone);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                
                String name = rs.getString("cus_name");
                String email = rs.getString("email");
                String zip = Integer.toString(rs.getInt("postal_code"));
                String address = rs.getString("address");
                String phonee = rs.getString("phone");
                String SSN = Integer.toString(rs.getInt("ssn"));
                String gender = rs.getString("gender");
                String rpname = rs.getString("rp_name");
                String rpfees = rs.getString("rp_fees");
                
                resp.getWriter().println("<html>\n"
                        + "\n"
                        + "    <head>\n"
                        + "\n"
                        + "    </head>\n"
                        + "\n"
                        + "    <body bgcolor=#C0C0C0>\n"
                        + "\n"
                        + "        <form action=\"searchServlet\">            \n"
                        + "            <table border=\"1\"  align=\"center\"  bordercolor=\"red\">    \n"
                        + "\n"
                        + "                <tr rowspan='2'>\n"
                        + "                    <th colspan=\"2\"> <b><font size=\"6\"> Search for a Customer      </font>\n"
                        + "                        </b>\n"
                        + "                    </th>\n"
                        + "                </tr>    \n"
                        + "\n"
                        + "\n"
                        + "                <tr rowspan='2'>\n"
                        + "\n"
                        + "                    <td>Customer's MSISDN   </td>\n"
                        + "\n"
                        + "                    <td><input type=\"text\" name=\"phone\" value="+phone+"></td>\n"
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
                        + "                    <td> <input type=\"submit\" value=\"search\" />   </td>\n"
                        + "\n"
                        + "                </tr>\n"
                        + "\n"
                        + "\n"
                        + "            </table>\n"
                        + "\n"
                        + "\n"
                        + "        </form>   ");
                
                resp.getWriter().println("<html> <body bgcolor='orange'><table bordercolor='black'align='center' border=1" + ">\n" + "<tr>\n"
                        + "     <td colspan=\"2\"><b>name: </td>\n"
                        + "     <td colspan=\"2\"><b>" + name + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "    <tr>\n"
                        + "     <td colspan=\"2\"><b>email: </td>\n"
                        + "     <td colspan=\"2\"><b>" + email + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "    <tr>\n"
                        + "     <td colspan=\"2\"><b>Zip_Code: </td>\n"
                        + "     <td colspan=\"2\"><b>" + zip + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "    <tr>\n"
                        + "     <td colspan=\"2\"><b>Address: </td>\n"
                        + "     <td colspan=\"2\"><b>" + address + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "    <tr>\n"
                        + "     <td colspan=\"2\"><b>Phone: </td>\n"
                        + "     <td colspan=\"2\"><b>" + phonee + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "    <tr>\n"
                        + "     <td colspan=\"2\"><b>National ID </td>\n"
                        + "     <td colspan=\"2\"><b>" + SSN + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "     <td colspan=\"2\"><b>Rate Plan </td>\n"
                        + "     <td colspan=\"2\"><b>" + rpname + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "     <td colspan=\"2\"><b>Rate Plan Price </td>\n"
                        + "     <td colspan=\"2\"><b>" + rpfees + "EGP" + " </td>\n"
                        + "    </tr>\n"
                        + "    \n"
                        + "</table>  ");
            }
            
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(searchServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
