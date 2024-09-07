/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedaikopipbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author elly gou
 */
public class koneksi {
    
 //Connection con;
    private static Connection con;
    
    //public static PreparedStatement ps;
     //public static Statement stm;
      //public static Exception ex;
       //public static void main(String args[]){
    
    public static Connection configDB() throws SQLException{
           try{
                String url= "jdbc:mysql://localhost/kedaikopipbo";
                String user= "root";
                String pass= "";
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                con= DriverManager.getConnection(url,user,pass);
                
                
                //Class.forName("com.mysql.jdbc.Driver");
                //con= DriverManager.getConnection(url,user,pass);
                
                System.out.println("Koneksi Berhasi");
           }catch (Exception e){
               System.err.println("Koneksi Gagal"+e.getMessage());
               
           }
           return con;
      }
    
}

