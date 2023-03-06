package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.TestdataBuild;
import resources.Utils;

public class stepDefinition extends Utils {
	RequestSpecification res;
	ResponseSpecification resSpec;
	Response response;
	TestdataBuild data = new TestdataBuild();
	static String place_id;

	@Given("Add Place Payload with {string} {string} {string}")
	public void add_place_payload_with(String name, String language, String address) throws IOException {
		res = given().spec(requestSpecifcation())
				.body(data.add_Place_Payload(name, language, address));
	}

	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method) {
		//Calling enum class
		//constructor will be called with value of resource which you pass
		APIResources resourceAPI = APIResources.valueOf(resource);
		System.out.println(resourceAPI.getResource());

		//Response Spec Builders
		resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

		if(method.equalsIgnoreCase("Post"))
			response = res.when().post(resourceAPI.getResource());
		else if(method.equalsIgnoreCase("Get"))
			response = res.when().get(resourceAPI.getResource());
		else if(method.equalsIgnoreCase("Delete"))
			response = res.when().delete(resourceAPI.getResource());	
	}

	@Then("the API call is success with status code {int}")
	public void the_api_call_is_success_with_status_code(Integer int1) {
		assertEquals(response.getStatusCode(),200);
	}

	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyValue, String expectedValue) {
		assertEquals(getJsonPath(response, keyValue), expectedValue);
	}	

	@Then("verify place_Id created maps to {string} using {string}")
	public void verify_place_id_created_maps_to_using(String expectedName, String resource) throws IOException {
		//prepare request spec
		place_id = getJsonPath(response, "place_id");
		res = given().spec(requestSpecifcation())
				.queryParam("place_id", place_id);
		user_calls_with_http_request(resource,"GET");
		String actualName = getJsonPath(response, "name");
		assertEquals(expectedName, actualName);
	}

	@Given("DeletePlace Payload")
	public void delete_place_payload() throws IOException {
		res = given().spec(requestSpecifcation())
				.body(data.delete_Place_Payload(place_id));
	}
}
