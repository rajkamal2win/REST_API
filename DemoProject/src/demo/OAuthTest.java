package demo;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		/*
		 * System.setProperty("webdriver.chrome.driver", "drivers/IEDriverServer.exe");
		 * WebDriver driver = new ChromeDriver(); driver.manage().window().maximize();
		 * driver.get(
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php"
		 * ); driver.findElement(By.cssSelector("input[type='email']")).sendKeys(
		 * "rajchronus");
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER
		 * ); Thread.sleep(3000);
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(
		 * "rajkamal***");
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.
		 * ENTER); Thread.sleep(3000); String url = driver.getCurrentUrl();
		 */	

		//Google stopped automating gmail sign in... Manually enter the url
		String url= "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWg4xKbISggIAhTVySAa1UUf7ipoBDyRcv4p6ad5sbjLPNk6ngFJxYEbciKNEJL--A&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none#";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];


		String accessTokenResponse = given().urlEncodingEnabled(false)
				.queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code")
				.when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");



		String response = given().queryParam("access_token", accessToken)
				.when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println(response);

		//Deserailzing using pojo
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
				.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
		List<Api> apiCourses = gc.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) 
		{
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) 
			{
				System.out.println(apiCourses.get(i).getPrice()); 
			}	
		}
		
		//print all the course titles of web automation
		String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
		ArrayList<String> actualCourseTitles = new ArrayList<String>();
		List<WebAutomation> webApp = gc.getCourses().getWebAutomation();
		for (int i = 0; i < webApp.size(); i++) 
		{
			actualCourseTitles.add(webApp.get(i).getCourseTitle());
		}
		//converting string of array to list of string
		List<String> expectedCourseTitles = Arrays.asList(courseTitles);
	
		Assert.assertTrue(expectedCourseTitles.equals(actualCourseTitles));
	}
}
