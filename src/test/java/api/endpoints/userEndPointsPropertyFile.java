package api.endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import api.payload.user;
import io.restassured.http.ContentType;

public class userEndPointsPropertyFile {
	
	//debug approach 0
//	public static void main(String[] args) { // OR place inside another method where you debug
//        System.out.println("File found at: " + 
//            userEndPointsPropertyFile.class.getClassLoader().getResource("Routes.properties"));
//    }
	
	//debug approach 1
	public static void main(String[] args) { 
        URL fileUrl = userEndPointsPropertyFile.class.getClassLoader().getResource("Routes.properties");
        
        System.out.println("one File found at: file:/D:/eclipse-workspace/PetStoreAPI/target/test-classes/Routes.properties");
        if (fileUrl != null) {
            System.out.println("File found at: " + fileUrl);
        } else {
            System.out.println("❌ Routes.properties NOT found!");
        }
    }

	
	static ResourceBundle getURL(){
		ResourceBundle bundle  = ResourceBundle.getBundle("Routes.properties"); //load Routes.properties file
		
		return bundle;
	}
	
	private static Properties properties = new Properties();
    // Static block to load properties when the class is initialized
    static {
        try (InputStream input = userEndPointsPropertyFile.class.getClassLoader().getResourceAsStream("Routes.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Routes.properties file not found in classpath!");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to get values from the properties file
    public static String getURL(String key) {
        return properties.getProperty(key);
    }
	
	
	
	//user module functions
    public static Response createUser(user payload) {
    		
    	   String post_url = getURL().getString("post_url");
    	   
           Response response = given()
          .accept(ContentType.JSON) //type of data which we accept
          .contentType(ContentType.JSON) //type of data which we send 
          .body(payload) //the data which we want to send
                
          .when()
          .post(post_url); //send to this url
               
          return response;
    }
    
    public static Response getUser(String userName){
    	   String get_url = getURL().getString("get_url");
    	
           Response response = given()
          .accept(ContentType.JSON)
          .pathParam("username", userName)
               
          .when()
          .get(get_url);
                
          return response;
    }
    
    public static Response updateUser(String userName, user payload){
    		String put_url = getURL().getString("update_url");
    	
            Response response = given()
           .accept(ContentType.JSON)
           .contentType(ContentType.JSON)
           .pathParam("username", userName)
           .body(payload)
                
           .when()
           .put(put_url);
               
           return response;
    }
    
    public static Response deleteUser(String userName){
    		String del_url = getURL().getString("delete_url");
    	
            Response response = given()
           .accept(ContentType.JSON)
           .pathParam("username", userName)
                
           .when()
           .delete(del_url);
                
           return response;
     }
}