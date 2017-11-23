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
public class DBImporter extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Connection con1 = connectToDatabase(1);
        Connection con2 = connectToDatabase(2);
        ResultSet rs;
        for (int i = 11; i < getCount(con1, "cbase_master_prefiltered") + 1; i++) {
            rs = con1.prepareStatement("SELECT * FROM cbase_master_prefiltered WHERE ID =" + i).executeQuery();
           
        }

    }

}
