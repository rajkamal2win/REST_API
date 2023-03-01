import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;

public class Basics {

	public static void main(String[] args) {
		//validate Add place API
		
		//given - All Input details 
		//when - Submit the API - Resource and HTTP Method
		//then - Validate the response
		//log all - To see all the details to be logged
		
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
												.body(Payload.AddPlace())
					.when().post("maps/api/place/add/json")
					.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
					.header("Server", "Apache/2.4.18 (Ubuntu)");	
	
		//validate body has scope with value APP using hamcrest library
		//validate response header server value 
	}	

}
