import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.Payload;
import files.ReUsableMethods;

public class EndtoEnd {

	public static void main(String[] args) {
		//Scenario 2 Add place -> Update Place with new address -> Get Place to validate if New address is present in response
		
		RestAssured.baseURI="https://rahulshettyacademy.com/";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body(Payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)")
		.extract().response().asString();
		System.out.println("***"+response+"***");
		
		JsonPath js = new JsonPath(response); //for parsing json (take string as input and convert to json)
		String placeId = js.getString("place_id");
		System.out.println("***"+placeId+"***");
		
		//Update Place
		String newAddress = "70 winter walk, Vegas US";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
			.body("{\r\n"
					+ "\"place_id\":\""+placeId+"\",\r\n"
					+ "\"address\":\""+newAddress+"\",\r\n"
					+ "\"key\":\"qaclick123\"\r\n"
					+ "}")
			.when().put("maps/api/place/update/json")
			.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Validate New Address
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		//Assert from cucumber
		Assert.assertEquals(actualAddress,newAddress);	
	}

}
