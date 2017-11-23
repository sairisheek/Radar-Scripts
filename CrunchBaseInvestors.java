/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import static scripts.BaseClass.escape;
import static scripts.BaseClass.fetchCBASE;
import static scripts.BaseClass.parseNames;

/**
 *
 * @author ubuntu
 */
public class CrunchBaseInvestors extends BaseClass{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
       
        Connection con = connectToDatabase();
        ResultSet rs = null;
        String perm = null;
        String query;
        JSONArray data;
        String names;
        String request_investor;
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
            request_investor = fetchCBASE("https://api.crunchbase.com/v/3/organizations/" + query + "/investors?user_key=bb9bdd625fbb708759f70c49b38dbfa8");
            if (request_investor == null) {
                continue;
            }
            //get array of "Person" entries in the request
            data = ((JSONArray) ((JSONObject) ((JSONObject) JSONValue.parseWithException(request)).get("data")).get("items"));
            
            
        }
    }
    }
    

