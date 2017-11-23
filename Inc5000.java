/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author ubuntu
 */
public class Inc5000 extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
         Connection con = connectToDatabase(2);
        WebDriver driver = new FirefoxDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get("http://www.inc.com/inc5000/list/2015");
        js.executeScript("document.getElementsByClassName('show_option')[4].click()");
        int rank;
        String company;
        int growth;
        String temp;
        long r;
        float revenue;
        String industry;
        String script;
        driver.findElement(By.className("close")).click();
        for (int i = 0; i < 25; i++) {
            js.executeScript("document.getElementById('page').value = '" + (i + 1) + "'");
            js.executeScript("document.getElementById('pageCount').children[1].click()");
            for (int j = 0; j < 200; j++) {

                script = "return document.getElementsByClassName('data_row')[" + j + "].children[0].innerHTML";

                rank = Integer.parseInt((String) js.executeScript(script));
                System.out.print(rank + " ");
                company = (String) js.executeScript("return document.getElementsByClassName('data_row')[" + j + "].children[1].textContent");
                System.out.print(company + " ");
                temp = (String) js.executeScript("return document.getElementsByClassName('data_row')[" + j + "].children[2].textContent");
                temp = temp.replaceAll("%", "");
                temp = temp.replaceAll(",", "");
                growth = Integer.parseInt(temp);

                System.out.print(growth + " ");
                temp = (String) js.executeScript("return document.getElementsByClassName('data_row')[" + j + "].children[3].textContent");
                temp = temp.substring(1);
                if (temp.contains("m")){
                temp = temp.replaceAll("m", "");
                revenue = (Float.parseFloat(temp) * 1000000);
                }
                else{
                temp = temp.replaceAll("b", "");
                revenue = (Float.parseFloat(temp) * 1000000000);
                }
                r = (long)revenue;
                System.out.print(r + " ");
            
                industry = (String) js.executeScript("return document.getElementsByClassName('data_row')[" + j + "].children[4].textContent");
                System.out.println(industry + " ");
                putInDB(con,rank,company,growth,revenue,industry);
            }
        }
    }

    static void putInDB(Connection con,int rank,String company,int growth,float revenue, String industry) throws Exception{
       PreparedStatement pst =con.prepareStatement("INSERT INTO Inc5000(RANK,COMPANY,GROWTH,REVENUE,INDUSTRY) VALUES(?,?,?,?,?)");
        pst.setInt(1, rank);
        pst.setString(2, company);
        pst.setInt(3, growth);
        pst.setFloat(4, revenue);
        pst.setString(5, industry);
        pst.executeUpdate();
    }


}
