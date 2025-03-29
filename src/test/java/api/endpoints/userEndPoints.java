package api.endpoints;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import api.payload.user;
import io.restassured.http.ContentType;

public class userEndPoints {
	//user module functions
    public static Response createUser(user payload) {
           Response response = given()
          .accept(ContentType.JSON) //type of data which we accept
          .contentType(ContentType.JSON) //type of data which we send 
          .body(payload) //the data which we want to send
                
          .when()
          .post(Routes.post_url); //send to this url
               
          return response;
    }
    
    public static Response getUser(String userName) {
           Response response = given()
          .accept(ContentType.JSON)
          .pathParam("username", userName)
               
          .when()
          .get(Routes.get_url);
                
          return response;
    }
    
    public static Response updateUser(String userName, user payload) {
            Response response = given()
           .accept(ContentType.JSON)
           .contentType(ContentType.JSON)
           .pathParam("username", userName)
           .body(payload)
                
           .when()
           .put(Routes.put_url);
               
           return response;
    }
    
    public static Response deleteUser(String userName) {
            Response response = given()
           .accept(ContentType.JSON)
           .pathParam("username", userName)
                
           .when()
           .delete(Routes.del_url);
                
           return response;
     }
}