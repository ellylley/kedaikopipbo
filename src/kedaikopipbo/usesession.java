/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedaikopipbo;

/**
 *
 * @author elly gou
 */
public class usesession {

 private static int id;
    private static String username;
    private static String nama_user;
    
    public static int get_id(){
        return id;
    }
    public static void set_id(int id){
        usesession.id = id;      
    }
    public static String get_username(){
        return username;
    }
    public static void set_username(String username){
        usesession.username = username; 
}
    public static String get_nama(){
        return nama_user;
    }
    public static void set_nama(String nama_user){
        usesession.nama_user = nama_user; 
    }
//    public void clearSession() {
//        username = null;
        
    //}

}
