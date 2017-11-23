/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static scripts.BaseClass.connectToDatabase;

/**
 *
 * @author ubuntu
 */
public class ExecutionTimer extends BaseClass implements Runnable {


    private String threadname;
    private Thread t;
    public long start_time;
    public long end_time;
    public long time_elapsed;
    public boolean done = false;

ExecutionTimer(String name){
    threadname= name;
}    
    
    
    
    public void start() {
        System.out.println("Starting Timer: " + threadname);
        if (t == null) {
            t = new Thread(this, threadname);
            t.start();
        }
    }

    @Override
    public void run() {
      start_time = System.currentTimeMillis();
        
         while(!done){
           
         }
         
         end_time = System.currentTimeMillis();
         System.out.println(start_time-end_time);
       
      
      }

}
