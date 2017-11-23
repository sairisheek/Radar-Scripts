/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 *
 * @author ubuntu
 */
public class CrunchBaseFounders extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //initialization
        Connection con = connectToDatabase();
        ResultSet rs = null;
        String perm = null;
        String stmt = null;
        String query = null;
        String request = null;
        JSONArray data = null;
        ArrayList names;

        
        
        //start looping thru rows
        for (int i = 1; i < 47600; i++) {
           //get the permalink of each entry so we can concatenate the api query
            rs = (con.prepareStatement("SELECT PERMALINK FROM cbase_master_prefiltered WHERE ID = " + i)).executeQuery();

            rs.next();
            try {
                //the permalink
                perm = rs.getString("PERMALINK");
            } catch (SQLException sqle) {
            }
            //permalinks are in format "/organization/name-of-organization", we just need name 
            query = perm.substring(perm.lastIndexOf("/") + 1);
            
            //get the JSON info from the API
            request = fetchCBASE("https://api.crunchbase.com/v/3/organizations/" + query + "/founders?user_key=bb9bdd625fbb708759f70c49b38dbfa8");
            if (request == null) {
                continue;
            }
            //get array of "Person" entries in the request
            data = ((JSONArray) ((JSONObject) ((JSONObject) JSONValue.parseWithException(request)).get("data")).get("items"));
            
            //parse the names of each "Person" entry
            names = parseNames(data);
          
            //format the name to look like "|Steve Jobs|Steve Wozniak|Ben Elowitz|"
            String formatted_name = "";
            for (int j = 0; j < names.size(); j++) {
                formatted_name += "|" + names.get(j);
                if (j == (names.size() - 1)) {
                    formatted_name += "|";
                }
            }
            try {
                //the name could be something like Dwayne "The Rock" Johnson, and MySQL doesn't like unescaped quotes 
                //so "Dwayne "The Rock" Johnson becomes "Dwayne \"The Rock\" Johnson
                formatted_name = escape(formatted_name);
                
                stmt = "UPDATE cbase_master_prefiltered SET FOUNDERS = \"" + formatted_name + "\" WHERE ID = " + i;
                if (i % 100 == 0) {
                    System.out.println(i);
                }
                con.prepareStatement(stmt).executeUpdate();//
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(stmt);
                System.exit(0);
            }
        }
    }

}
