package api.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;
import com.github.javafaker.Faker;

import api.endpoints.userEndPoints;
import api.payload.user;
import api.utilities.DataProviders;
import io.restassured.response.Response;

import static org.awaitility.Awaitility.await; // Import Awaitility

import java.util.concurrent.TimeUnit; // For TimeUnit

public class UserTestDD {
	
	user userPayload;
	Faker faker; //class level object creation
	public static Logger Logger;
	
	@BeforeClass
	public void generateTestData() {
		
		faker = new Faker(); //allocating memory to this object
		userPayload = new user(); //allocating memory to this object
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		///obtain logger
        Logger = LogManager.getLogger("PetStoreAPI");
	}
	
	
	//Actual code
	@Test(priority=1, dataProvider = "AllData", dataProviderClass = DataProviders.class)
	public void testCreateUser(String userID, String UserName, String fname, String lname, String email, String pwd, String phone){
		
		userPayload = new user(); //allocating memory to this object
		
		userPayload.setId(Integer.parseInt(userID));
		userPayload.setUsername(UserName);
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		userPayload.setEmail(email);
		userPayload.setPassword(pwd);
		userPayload.setPhone(phone);
		
		
		System.out.println("=================================");
		System.out.println("Create User Data");
		
		Response response = userEndPoints.createUser(userPayload);
		
		System.out.println("Fetching data for Create User: " + userPayload.getUsername());
		System.out.println("=================================");
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
	}
	
//	Approach 1 = failed
//	@Test(priority=1, dataProvider = "AllData", dataProviderClass = DataProviders.class)
//	public void testCreateUser(String userID, String UserName, String fname, String lname, String email, String pwd, String phone){
//		
//		userPayload = new user(); //allocating memory to this object
//		
//		userPayload.setId(Integer.parseInt(userID));
//		userPayload.setUsername(UserName);
//		userPayload.setFirstName(fname);
//		userPayload.setLastName(lname);
//		userPayload.setEmail(email);
//		userPayload.setPassword(pwd);
//		userPayload.setPhone(phone);
//		
//		
//		System.out.println("=================================");
//		System.out.println("Create User Data");
//		
//		Response response = userEndPoints.createUser(userPayload);
//		
//		System.out.println("Fetching data for Create User: " + userPayload.getUsername());
//		System.out.println("=================================");
//		//log response
//		response.then().log().all();
//		
//		//validation
//		Assert.assertEquals(response.getStatusCode(),200);
//		
//		// ✅ Add a delay after creating a user to allow database consistency
//	    try {
//	        Thread.sleep(2000); // 2-second delay
//	    } catch (InterruptedException e) {
//	        e.printStackTrace();
//	    }
//
//	    // ✅ Re-check if the user exists after creation
//	    Response getUserResponse = userEndPoints.getUser(UserName);
//	    Assert.assertEquals(getUserResponse.getStatusCode(), 200, "User was not found after creation!");
//	}
	

	@Test(priority=2, dataProvider = "UserNamesData", dataProviderClass = DataProviders.class)
	public void testGetUserData(String username) throws InterruptedException {
			System.out.println("=================================");
			System.out.println("Get User Data");
		
		    //Use Awaitility to wait for the user to be created before fetching
	        await().atMost(10, TimeUnit.SECONDS).until(() -> {
	        Response response = userEndPoints.getUser(username);

	        System.out.println("Fetching data for Get User: " + username);
	        System.out.println("=================================");
			
	        //log response
	        response.then().log().all();
			
	        //validation
	        return response.getStatusCode() == 200; // Wait until the status code is 200
	        });
	        
	        Thread.sleep(5000);
	}
	
	@Test(priority=3, dataProvider = "UserNamesData", dataProviderClass = DataProviders.class)
	public void testDeletetUser(String username) {
		System.out.println("=================================");
		System.out.println("Delete User Data");
		System.out.println("=================================");
		
		Response response = userEndPoints.deleteUser(username);
		System.out.println("Fetching data for user to delete: " + username);
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
	}
	
	
	/*@Test(priority=3, dataProvider = "UserNamesData", dataProviderClass = DataProviders.class)
	public void testDeletetUser(String username){
		
	    // Step 1: Check if user exists before deletion
	    Response getUserResponse = userEndPoints.getUser(username);
	    System.out.println("Before deletion: User " + username + " exists? Status code = " + getUserResponse.getStatusCode());

	    // Step 2: If user does not exist, fail early
	    Assert.assertEquals(getUserResponse.getStatusCode(), 200, "User not found before deletion!");

	    // Step 3: Proceed with deletion
	    Response deleteResponse = userEndPoints.deleteUser(username);
	    System.out.println("Delete response for " + username + ": " + deleteResponse.getStatusCode());

	    Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Delete request failed!");
	}*/
}