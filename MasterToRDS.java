/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;

/**
 *
 * @author ubuntu
 */
public class MasterToRDS extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Connection local = connectToDatabase(1);
        Connection remote = connectToDatabase(2);
        
    }
    
}
