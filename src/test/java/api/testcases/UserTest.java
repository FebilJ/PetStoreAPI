package api.testcases;

import org.testng.Assert;
import org.testng.annotations.*;
import com.github.javafaker.Faker;

import api.endpoints.userEndPoints;
import api.payload.user;
import io.restassured.response.Response;

import static org.awaitility.Awaitility.await; // Import Awaitility
import java.util.concurrent.TimeUnit; // For TimeUnit

public class UserTest {

	//to generate fake data
	
	Faker faker; //class level object creation
	user userPayload;
	
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
	}
	
	@Test(priority=1)
	public void testCreatUser() {
		System.out.println("========Create User Data=========");
		Response response = userEndPoints.createUser(userPayload);
		
		System.out.println("Fetching data for Create User: " + userPayload.getUsername());
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
	}

	@Test(priority=2)
	public void testGetUserData() {
			System.out.println("============Get User Data=========");
		 // Use Awaitility to wait for the user to be created before fetching
	       await().atMost(5, TimeUnit.SECONDS).until(() -> {
	       Response response = userEndPoints.getUser(this.userPayload.getUsername());

	       System.out.println("Fetching data for Get User: " + this.userPayload.getUsername());
			
	       //log response
	       response.then().log().all();
			
	       //validation
	       return response.getStatusCode() == 200; // Wait until the status code is 200
	       });
	}
	
	
	@Test(priority=3)
	public void testUpdatetUser() {
		System.out.println("===========Update User Data========");
		
		userPayload.setFirstName(faker.name().firstName());
		Response response = userEndPoints.updateUser(this.userPayload.getUsername(),userPayload);
		
		
		System.out.println("Fetching data for Update User: " + this.userPayload.getUsername());
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
	}
	
	
	@Test(priority=4)
	public void testDeletetUser() {
		System.out.println("============Delete User Data=========");
		
		Response response = userEndPoints.deleteUser(this.userPayload.getUsername());
		System.out.println("Fetching data for user to delete: " + this.userPayload.getUsername());
		
		//log response
		response.then().log().all();
		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
	}
}