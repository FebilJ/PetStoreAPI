package api.testcases;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.Assert;
import org.testng.annotations.*;
import com.github.javafaker.Faker;


import api.endpoints.userEndPointsPropertyFile;
import api.payload.user;
import io.restassured.response.Response;

import static org.awaitility.Awaitility.await; // Import Awaitility
import java.util.concurrent.TimeUnit; // For TimeUnit

public class UserTestPropertyFile {

	//to generate fake data
	
	Faker faker; //class level object creation
	user userPayload;
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
	
	@Test
	public void testFilePath() {
	    System.out.println("File found at: " + 
	        getClass().getClassLoader().getResource("Routes.properties"));
	}
	
	@Test(priority=1)
	public void testCreateUser() {
		System.out.println("=================================");
		System.out.println("Create User Data");
		
		Response response = userEndPointsPropertyFile.createUser(userPayload);
		
		System.out.println("Fetching data for Create User: " + userPayload.getUsername());
		System.out.println("=================================");
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
		
		//log
		Logger.info("Create User Data executed");
	}

	@Test(priority=2)
	public void testGetUserData() {
		   System.out.println("=================================");
		   System.out.println("Get User Data");
		
		   //Use Awaitility to wait for the user to be created before fetching
	       await().atMost(5, TimeUnit.SECONDS).until(() -> {
	       Response response = userEndPointsPropertyFile.getUser(this.userPayload.getUsername());

	       System.out.println("Fetching data for Get User: " + this.userPayload.getUsername());
	       System.out.println("=================================");
			
	       //log response
	       response.then().log().all();
			
	       //validation
	       return response.getStatusCode() == 200; // Wait until the status code is 200
	       });
	       
	     //log
		 Logger.info("Get User Data executed");
	}
	
	
	@Test(priority=3)
	public void testUpdatetUser() {
		
		System.out.println("=================================");
		System.out.println("Update User Data");
		System.out.println("=================================");
		
		userPayload.setFirstName(faker.name().firstName());
		Response response = userEndPointsPropertyFile.updateUser(this.userPayload.getUsername(),userPayload);
		
		//System.out.println("Fetching data for Update User: " + this.userPayload.getUsername());
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
		
		//Read User data to check if first name is updated
		Response  responsePostUpdate = userEndPointsPropertyFile.getUser(this.userPayload.getUsername());
		System.out.println("=================================");
		System.out.println("After Update User Data");
		System.out.println("=================================");
		responsePostUpdate.then().log().all();
		
		//log
		Logger.info("Update User Data executed");
	}
	
	
	@Test(priority=4)
	public void testDeletetUser() {
		System.out.println("=================================");
		System.out.println("Delete User Data");
		System.out.println("=================================");
		
		Response response = userEndPointsPropertyFile.deleteUser(this.userPayload.getUsername());
		System.out.println("Fetching data for user to delete: " + this.userPayload.getUsername());
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
		
		//log
		Logger.info("Delete User Data executed");
	}
}