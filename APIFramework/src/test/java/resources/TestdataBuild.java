package resources;

import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.Location;

public class TestdataBuild {
	
	public AddPlace add_Place_Payload(String name, String language, String address) {
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setName(name);
		ap.setAddress(address);
		ap.setPhone_number("(+91) 987 654 3210");
		ap.setLanguage(language);
		ap.setWebsite("http://google.com");

		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		ap.setTypes(myList);

		Location loc = new Location();
		loc.setLat(-38.383494);
		loc.setLng(33.427362);
		ap.setLocation(loc);
		
		return ap;
	}
	
	public String delete_Place_Payload(String place_id) {	
		return "{\r\n    \"place_id\":\""+place_id+"\"\r\n}";
	}
	

}
