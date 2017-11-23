/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ubuntu
 */
public class CrunchBaseIntegrator extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        try {
            Connection con = connectToDatabase();
            PreparedStatement pst = null;
            ResultSet rs_excel = null;
            ResultSet rs_json = null;
            for (int i = 57113; true; i++) {
                pst = con.prepareStatement("SELECT * FROM cbase_excel_table WHERE ID = " + (i + 1));
                rs_excel = pst.executeQuery();
                rs_excel.next();

                pst = con.prepareStatement("SELECT * FROM cbase_json_table WHERE NAME = \"" + rs_excel.getString("NAME") + "\"");
                rs_json = pst.executeQuery();
                rs_json.next();
                System.out.println(i);
                pst = con.prepareStatement("INSERT INTO cbase_master(NAME, PERMALINK, HOMEPAGE_URL, CATEGORY_LIST, MARKET, "
                        + "FUNDING, STATUS, COUNTRY, REGION, CITY, DESCRIPTION, DOMAIN, IMAGE_URL, FACEBOOK_URL, TWITTER_URL, LINKEDIN_URL, "
                        + "CBASE_UUID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, rs_excel.getString("NAME"));
                pst.setString(2, rs_excel.getString("PERMALINK"));
                pst.setString(3, rs_excel.getString("HOMEPAGE_URL"));
                pst.setString(4, rs_excel.getString("CATEGORY_LIST"));
                pst.setString(5, rs_excel.getString("MARKET"));
                pst.setLong(6, Long.parseLong(rs_excel.getString("FUNDING")));
                pst.setString(7, rs_excel.getString("STATUS"));
                pst.setString(8, rs_excel.getString("COUNTRY"));
                pst.setString(9, rs_excel.getString("REGION"));
                pst.setString(10, rs_excel.getString("CITY"));
                try {
                    pst.setString(11, rs_json.getString("DESCRIPTION"));
                    pst.setString(12, rs_json.getString("DOMAIN"));
                    pst.setString(13, rs_json.getString("IMAGE_URL"));
                    pst.setString(14, rs_json.getString("FACEBOOK_URL"));
                    pst.setString(15, rs_json.getString("TWITTER_URL"));
                    pst.setString(16, rs_json.getString("LINKEDIN_URL"));
                    pst.setString(17, rs_json.getString("CBASE_UUID"));
                    pst.executeUpdate();
                } catch (SQLException sqle) {
                    pst.setString(11, "");
                    pst.setString(12, "");
                    pst.setString(13, "");
                    pst.setString(14, "");
                    pst.setString(15, "");
                    pst.setString(16, "");
                    pst.setString(17, "");
                    pst.executeUpdate();

                }
            }

        } catch (Exception e) {
            message(e);
        }

    }

}
