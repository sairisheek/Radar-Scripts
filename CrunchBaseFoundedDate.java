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
public class CrunchBaseFoundedDate extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //initialization
        Connection con = connectToDatabase(2);
        ResultSet rs = null;
        String perm = null;
        String stmt = null;
        String query = null;
        String request = null;
        JSONObject data = null;
        String founded_date;

        //start looping thru rows
        for (int i = 1; i < 47600; i++) {
            //get the permalink of each entry so we can concatenate the api query
            rs = (con.prepareStatement("SELECT PERMALINK FROM radar_access_startupraw WHERE ID = " + i)).executeQuery();

            rs.next();
            try {
                //the permalink
                perm = rs.getString("PERMALINK");
            } catch (SQLException sqle) {
            }
            //permalinks are in format "/organization/name-of-organization", we just need name 
            query = perm.substring(perm.lastIndexOf("/") + 1);

            //get the JSON info from the API
            request = fetchCBASE("https://api.crunchbase.com/v/3/organizations/" + query + "?user_key=bb9bdd625fbb708759f70c49b38dbfa8");
            if (request == null) {
                continue;
            }
            //get array of "Person" entries in the request
            data = ((JSONObject) ((JSONObject) ((JSONObject) JSONValue.parseWithException(request)).get("data")).get("properties"));
            founded_date = (String) data.get("founded_on");
            stmt = "UPDATE radar_access_startupraw SET founded_date = \"" + founded_date + "\" WHERE ID = " + i;
            con.prepareStatement(stmt).executeUpdate();
            if (i % 100 == 0) {
                System.out.println(i);
            }
        }

    }
}
