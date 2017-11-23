/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ubuntu
 */
public class Inc5000CrossRef extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Connection con = connectToDatabase(2);
        /*     ResultSet rs = con.prepareStatement("SELECT COMPANY FROM Inc5000").executeQuery();
         ResultSet rs1;
         int cnt = 1;
        
         ArrayList existing_list = new ArrayList<String>();
        
        
         while (rs.next()) {

         rs1 = con.prepareStatement("SELECT * FROM radar_access_startupraw WHERE NAME LIKE \"" + escape(rs.getString("COMPANY")) + "\"").executeQuery();
         while (rs1.next()) {
         existing_list.add(rs1.getString("NAME"));
         }
         }

    
    
         /***********************************************************************************************************************************************//*
     
       
         //set bool flag true for existing
         for(int i =0;i<existing_list.size();i++){
         con.prepareStatement("UPDATE radar_access_startupraw SET isinc5000 = 1 WHERE NAME =\""+existing_list.get(i)+"\"").executeUpdate();
         }
    
         */


        /*
         //existing funding
         ResultSet set = con.prepareStatement("SELECT NAME FROM radar_access_startupraw WHERE isinc5000 = 1").executeQuery();
         ResultSet inc;
         while (set.next()) {
         inc = con.prepareStatement("SELECT * FROM Inc5000 WHERE COMPANY = \"" + set.getString("NAME") + "\"").executeQuery();
         inc.next();
         con.prepareStatement("UPDATE radar_access_startupraw SET FUNDING = " + inc.getInt("REVENUE") + " WHERE NAME = \""+set.getString("NAME")+"\"").executeUpdate();
          
        
        
         }
         */
    
    /*ResultSet set = con.prepareStatement("SELECT NAME FROM radar_access_startupraw WHERE isinc5000 = 1").executeQuery();
        //delete coexisting
        while(set.next()){
        con.prepareStatement("DELETE FROM Inc5000 WHERE COMPANY = \""+set.getString("NAME")+"\"").executeUpdate();
    }
    */
    
        
        
      ResultSet inc = con.prepareStatement("SELECT * FROM Inc5000").executeQuery();
      PreparedStatement pst;
      while(inc.next()){
         pst = con.prepareStatement("INSERT INTO radar_access_startupraw(NAME, FUNDING, MARKET, isinc5000) VALUES(?,?,?,1)");
         pst.setString(1, inc.getString("COMPANY"));
         pst.setLong(2, inc.getLong("REVENUE"));
         pst.setString(3, inc.getString("INDUSTRY"));
         pst.executeUpdate();
      }
    
    }

}
