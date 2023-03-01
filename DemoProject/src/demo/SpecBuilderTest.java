package demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

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
	
	//Request Spec Builders
	RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/").addQueryParam("key", "qaclick123")
									.setContentType(ContentType.JSON).build();
	
	//Response Spec Builders
	ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
	
	RequestSpecification res = given().spec(req)
		.body(ap);
	
	Response response = res.when().post("maps/api/place/add/json")
		.then().spec(resSpec).extract().response();
	
	String responseString = response.asString();
	System.out.println(responseString);
	}
}