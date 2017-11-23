/*String t
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author ubuntu
 */
public class FundingPreFilter extends BaseClass implements Runnable {

    private String threadname;
    private Thread t;
    private int increment_low;
    private int increment_high;
    private int i;
   static Connection con;
    
    
    public static void main(String[] args) throws Exception {
        
       con = connectToDatabase();
        for(int  i = 0;i<5610;i++){
        
            FundingPreFilter t1 = new FundingPreFilter("Thread "+i, (i*50)-1, (i+1)*50);
        
       t1.start();
        
        }
       
    
    
    
    
    }

    FundingPreFilter(String name, int low, int high) {
        threadname = name;
        increment_low = low;
        increment_high = high;
       
    }

    public void start( ) throws Exception{
      
      
        
        if (t == null) {
            t = new Thread(this, threadname);
            t.start();
         
        }
    }

    @Override
    public void run() {
        int cnt;
        Exception ex = null;
        cnt = increment_low;
        try {
            
            Statement stmt = null;
            ResultSet rs = null;
            
            String response = null;
            String result = null;
            boolean success = false;
            long funding ;
            int retry_cnt = 0;
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT CRUNCHBASE_URL FROM master_table WHERE ID > " + increment_low + " AND ID < " + increment_high);
             
            while (rs.next()) {

                result = trim(rs.getString(1));
                while (!success) {
                    try {
                        response = fetchS("https://api.crunchbase.com/v/3/organizations/" + result + "?user_key=bb9bdd625fbb708759f70c49b38dbfa8&organization_types=company");
                        success = true;
                        cnt++;
                    } catch (Exception e) {
                        success = false;
                        if(retry_cnt == 5){break;}
                        retry_cnt++;
                    }
                }
                success = false;
               
              
              if(response!=null){
                funding = Long.parseLong(response);
             con.createStatement().executeUpdate("UPDATE master_table SET FUNDING = "+funding+" WHERE ID = "+cnt);
              }
              else{
                  System.out.println("response NULL");
                 System.out.println("ID " + cnt);
              } 
                
               
            }
        } catch (Exception e) {
            ex = e;
            
        } finally {
            if (ex != null) {

                try {
                    System.out.println("ID: " + cnt);
                    message(ex);
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
            }

        }

       
    }

}
