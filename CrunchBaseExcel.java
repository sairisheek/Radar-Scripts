/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import com.monitorjbl.xlsx.StreamingReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Admin
 */
/**
 *
 * @author Admin
 */
public class CrunchBaseExcel extends BaseClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        InputStream is = new FileInputStream(new File("/home/ubuntu/cbase_excel_exports/crunchbase_export.xlsx"));
       
        System.out.println("Parsing Excel...");
        StreamingReader reader = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .sheetIndex(2)
                .read(is);
        System.out.println("Excel Parsed!");
        Connection con = connectToDatabase();
        Iterator it = reader.iterator();
        Row r = null;
        PreparedStatement pst = con.prepareStatement("INSERT INTO cbase_excel_table(PERMALINK, NAME, HOMEPAGE_URL, "
                + "CATEGORY_LIST, MARKET, FUNDING, STATUS, COUNTRY, REGION, CITY) VALUES(?,?,?,?,?,?,?,?,?,?)");
        int cnt = 0;
        while (it.hasNext()) {
            r = (Row) it.next();
            if (cnt == 0) {
                cnt++;
                continue;
            }

            try {
                pst.setString(1, r.getCell(1).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(2, r.getCell(0).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(3, r.getCell(2).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(4, r.getCell(3).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(5, r.getCell(4).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setLong(6, Long.parseLong(r.getCell(5).getStringCellValue()));
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(7, r.getCell(6).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(8, r.getCell(7).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(9, r.getCell(9).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }
            try {
                pst.setString(10, r.getCell(10).getStringCellValue());
            } catch (NullPointerException npe) {
                System.out.println(cnt);
            }

            cnt++;
            pst.executeUpdate();
        }

    }
}
