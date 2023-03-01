import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.RestAssured;

public class RetreiveFromJsonFile {

	@Test
	public void addPlace () throws IOException
	{
		//Convert content of the file to string -> content of file can convert into Bytes -> Bytes data to string
		String payload = new String (Files.readAllBytes(Paths.get("C:\\Users\\Rajkamal\\Downloads\\Automation\\Rest API\\addPlace.json")));
		
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload)
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200)
		.body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)");
	}
	
}
