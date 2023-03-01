import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {

		RestAssured.baseURI="http://localhost:8080";

		//Login Scenario
		SessionFilter session = new SessionFilter();
		given().relaxedHTTPSValidation().header("Content-Type","application/json")
		.body("{ \"username\": \"rajkamal2win\", \"password\": \"Stark@619\" }")
		.filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().extract().response().asString();

		//Add comment
		String expectedMessage = "Hi How are you?";
		String addCommentResponse = given().log().all().pathParam("id", "10025").header("Content-Type","application/json")
				.body("{\r\n"
						+ "    \"body\": \""+expectedMessage+"\",\r\n"
						+ "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n"
						+ "    }\r\n"
						+ "}")
				.filter(session)
				.when().post("/rest/api/2/issue/{id}/comment")
				.then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");

		//Add attachment
		given().pathParam("id", "10025").header("X-Atlassian-Token","no-check").filter(session)
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("Jira.txt"))
		.when().post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);

		//Get Issue
		String issueDetails = given().filter(session).pathParam("id", "10025").queryParam("fields", "comment")
				.when().get("/rest/api/2/issue/{id}")
				.then().log().all().extract().response().asString();
		System.out.println(issueDetails);

		JsonPath js1 = new JsonPath(issueDetails);
		int commentCount = js1.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentCount; i++) 
		{
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentId)) 		
			{
			String message=js1.getString("fields.comment.comments["+i+"].body");
			System.out.println("****"+message+"****");
			Assert.assertEquals(message, expectedMessage);
			break;
			}
		}
	}
}
