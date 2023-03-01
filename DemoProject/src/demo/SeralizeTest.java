package demo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SeralizeTest {

	public static void main(String[] args) {
	RestAssured.baseURI="https://rahulshettyacademy.com/";
	
	AddPlace ap = new AddPlace();
	ap.setAccuracy(50);
	ap.setName("DeaDshot House");
	ap.setAddress("DeaDshot House");
	ap.setPhone_number("29, side layout, cohen 09");
	ap.setLanguage("French-IN");
	ap.setWebsite("http://google.com");
	
	List<String> myList = new ArrayList<String>();
	myList.add("shoe park");
	myList.add("shop");
	ap.setTypes(myList);
	
	Location loc = new Location();
	loc.setLat(-38.383494);
	loc.setLng(33.427362);
	ap.setLocation(loc);
	
	Response res = given().log().all().queryParam("key", "qaclick123")
		.body(ap)
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response();
	
	String responseString = res.asString();
	System.out.println(responseString);
	}
}
