package bdd.APIFramework;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.dataDriven;
public class excelDriven {


	@Test
	public void addBook() throws IOException
	{
		dataDriven d=new dataDriven();
		ArrayList data=d.getData("RestAddbook","RestAssured");


		HashMap<String, Object>  map = new HashMap<String, Object>();
		map.put("name", data.get(1));
		map.put("isbn", data.get(2));
		map.put("aisle", data.get(3));
		map.put("author", data.get(4));

		/* For Nested Json
	   HashMap<String, Object>  map2 = new HashMap<>();
		map.put("lat", "12");
		map.put("lng", "34");
		map.put("location", map2);*/


		RestAssured.baseURI="https://rahulshettyacademy.com";
		Response resp=given().log().all().
				header("Content-Type","application/json").
				body(map).
				when().
				post("/Library/Addbook.php").
				then().log().all().assertThat().statusCode(200).
				extract().response();
		JsonPath js= ReusableMethods.rawToJson(resp);
		String id=js.get("ID");
		System.out.println(id);

		// Create a place =response (place id)

		// delete Place = (Request - Place id)
		HashMap<String, Object>  map3 = new HashMap<String, Object>();
		map3.put("ID", id);

		Response res = given().log().all().header("Content-Type","application/json")
		.body(map3)
		.when().delete("/Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200).
		extract().response();
		js=ReusableMethods.rawToJson(res);
		String msg = js.get("msg");
		System.out.println(msg);	 
	}


	public static String GenerateStringFromResource(String path) throws IOException {

		return new String(Files.readAllBytes(Paths.get(path)));

	}
}
