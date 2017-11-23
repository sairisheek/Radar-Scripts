/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 *
 * @author ubuntu
 */
public class AngellistLinker extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Connection con = connectToDatabase();
        int id = 11;
        PreparedStatement pst = con.prepareStatement("SELECT NAME FROM cbase_master_prefiltered WHERE ID = " + id);
        ResultSet rs = pst.executeQuery();
        rs.next();
        String dbname = rs.getString("NAME");
        String response = fetch("https://api.angel.co/1/search?v=1&query=" + URLEncoder.encode(dbname, "UTF-8") + "&type=Startup&client_id=0bdabb752fa4e07f61a59c797aa3f05533be9cc98164cc44&access_token=1dd6555f46c5771b9cfcde241aae180c2d188f1bf2c0dab2");
        JSONObject properties
                = ((JSONObject) (((JSONArray) JSONValue.parseWithException(response)).get(0)));

        System.out.println(properties.get("name"));
        System.out.println(dbname);
        System.out.println(similarity(dbname, (String) properties.get("name")));

        pst = con.prepareStatement("UPDATE cbase_master_prefiltered SET ANGELLIST_ID = ? , ANGELLIST_NAME = ? , ANGELLIST_URL = ? WHERE ID = "
                + id);
        pst.setInt(1, (Integer) properties.get("id"));
        pst.setString(2, (String) properties.get("name") + "/" + similarity((String) properties.get("name"), dbname));
        pst.setString(3, (String) properties.get("url"));
        pst.executeUpdate();
    
    }

}
