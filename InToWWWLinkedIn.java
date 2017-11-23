/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author ubuntu
 */
public class InToWWWLinkedIn extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Connection con = connectToDatabase(2);
       ResultSet rs = con.prepareStatement("SELECT LINKEDIN_URL FROM radar_access_startupraw WHERE LINKEDIN_URL LIKE \"%in.lin%\"").executeQuery();
       String original;
       String replacement;
       while(rs.next()){
           original= rs.getString("LINKEDIN_URL");
         replacement= original.replace("/in", "/www");
          con.prepareStatement("UPDATE radar_access_startupraw SET LINKEDIN_URL = \""+replacement+"\" WHERE LINKEDIN_URL = \""+original+"\"").executeUpdate();
       }
    }
    
}
