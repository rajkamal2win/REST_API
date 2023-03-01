package stepDefinitions;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {
	
	@Before("@DeletePlace")
	public void beforeScenario() throws IOException {
		//execute this code only when place id is null
		//write a code that will give u place id
		stepDefinition sd = new stepDefinition();
		
		if(stepDefinition.place_id==null) 
		{
		sd.add_place_payload_with("Tony Stark", "French", "10880 Malibu Point 90265");
		sd.user_calls_with_http_request("addPlaceAPI", "POST");
		sd.verify_place_id_created_maps_to_using("Tony Stark", "getPlaceAPI");
		}
	}
}
