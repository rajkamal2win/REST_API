import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(Payload.CoursePrice());

		//Print No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);

		//Print Purchase Amount
		int totalAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalAmount);

		//Print Title of the first course
		String titleFirstCourse = js.getString("courses[0].title");
		System.out.println(titleFirstCourse);

		//Print All course titles and their respective Prices
		for (int i = 0; i < count; i++) 
		{
			String courseTitles = js.get("courses["+i+"].title");
			int prices = js.getInt("courses["+i+"].price");
			System.out.println(courseTitles);
			System.out.println(prices);
		}

		//Print no of copies sold by RPA Course
		for (int i = 0; i < count; i++) 
		{
			String courseTitles = js.get("courses["+i+"].title");
			if (courseTitles.equalsIgnoreCase("RPA")) 
			{
				//copies sold
				System.out.println(js.get("courses["+i+"].copies").toString());
				break;
			}
		}

	}

}
