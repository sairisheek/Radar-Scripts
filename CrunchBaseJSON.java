package scripts;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;

import java.sql.PreparedStatement;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class CrunchBaseJSON extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Exception ex = null;
        try {

             
            File directory = new File("/home/ubuntu/cbase_odm");
            File[] files = directory.listFiles();
            for (File file : files) {

                if (!file.delete()) { System.out.println("Failed to delete " + file);

                }

            }
download("https://api.crunchbase.com/v/3/odm/odm.json.tar.gz?user_key=bb9bdd625fbb708759f70c49b38dbfa8","/home/ubuntu/cbase_odm/odm.json.tar.gz");            
untar("/home/ubuntu/cbase_odm/odm.json.tar.gz", "/home/ubuntu/cbase_odm");
            System.out.println("Parsing JSON...");
            JSONObject root = (JSONObject) JSONValue.parseWithException(new FileReader("/home/ubuntu/organizations.json"));
            JSONArray temp = (JSONArray) root.get("root");

            System.out.println("Parsed JSON!");

            PreparedStatement pst = null;
            Connection con = connectToDatabase();
            pst = con.prepareStatement("INSERT INTO cbase_json_table(NAME, DESCRIPTION, DOMAIN, HOMEPAGE_URL, FACEBOOK_URL"
                    + ", TWITTER_URL, LINKEDIN_URL, CITY, REGION, COUNTRY, CBASE_UUID, CRUNCHBASE_URL, IMAGE_URL"
                    + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
int cnt = 0;
            System.out.println("Loading values...");
            for (int i = 0; true; i++) {
                try {
                    
                    JSONObject properties = (JSONObject) temp.get(i);
                   
                    if(!((String)properties.get("primary_role")).equals("company")){continue;}
                    pst.setString(1, (String) properties.get("name"));
                    pst.setString(2, (String) properties.get("short_description"));
                    pst.setString(3, (String) properties.get("homepage_domain"));
                    pst.setString(4, (String) properties.get("homepage_url"));
                    pst.setString(5, (String) properties.get("facebook_url"));
                    pst.setString(6, (String) properties.get("twitter_url"));
                    pst.setString(7, (String) properties.get("linkedin_url"));
                    pst.setString(8, (String) properties.get("location_city"));
                    pst.setString(9, (String) properties.get("location_region"));
                    pst.setString(10, (String) properties.get("location_country_code"));
                    pst.setString(11, (String) properties.get("crunchbase_uuid"));
                    pst.setString(12, (String) properties.get("crunchbase_url"));
                    pst.setString(13, (String) properties.get("profile_image_url"));
                    pst.executeUpdate();
                    cnt = i;
                } catch (NullPointerException ne) {
                  
                } catch (Exception e) {
                    System.out.println(cnt);
                    e.printStackTrace();
                    message(e);
                    break;
                }
            }
            System.out.println("Values Loaded!");

        } catch (Exception e) {
            ex = e;
        } finally {
            if (ex != null) {
                message(ex);
            }
        }

    }

}
