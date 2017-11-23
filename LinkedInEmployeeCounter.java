/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author ubuntu
 */
public class LinkedInEmployeeCounter extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
       String url;
        WebDriver driver = new FirefoxDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Connection con = connectToDatabase(2);
        String num;
        driver.get("https://www.linkedin.com/uas/login");
        waitFor(driver, By.id("session_key-login"), "---");
        waitFor(driver, By.id("session_password-login"), "---");
        waitFor(driver, By.id("btn-primary"), true);
        System.out.println("Retrieving URL data....");
        ResultSet rs = con.prepareStatement("SELECT LINKEDIN_URL FROM radar_access_startupraw WHERE LINKEDIN_URL  != ''").executeQuery();
        System.out.println("Retrieved data!");
        waitFor(driver, By.id("sharebox-container"));
        takeScreenshot("start",true);
        int cnt = 0;
        int c = 0;
        
        while (rs.next()) {
           url = rs.getString("LINKEDIN_URL");
            System.out.println(url);
            driver.navigate().to(url);
            try {
                waitFor(driver, By.className("density"), 10);
            } catch (TimeoutException te) {
                takeScreenshot("screen"+cnt);
                cnt++;
                continue;
               
            }
            while(driver.getTitle().contains("403: ")){Thread.sleep(60000); driver.navigate().to(url);Thread.sleep(5000);
           }
            num = (String) js.executeScript("return document.getElementsByClassName('density')[0].textContent");
            if(!num.equals("")){
                con.prepareStatement("UPDATE radar_access_startupraw SET num_employees = \"" +num+"\" WHERE LINKEDIN_URL = \""+url+"\"").executeUpdate();
            }
            else{
                con.prepareStatement("UPDATE radar_access_startupraw SET num_employees = \"DNE\" WHERE LINKEDIN_URL = \""+url+"\"").executeUpdate();
            }
            System.out.println(num);
            if(c==4000){message("Finished");}
            c++;
        }

    }

}
