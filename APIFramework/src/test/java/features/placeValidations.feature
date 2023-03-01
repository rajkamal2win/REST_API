Feature: Validating Place API's 

@AddPlace @Regression
Scenario Outline: Verify if Place is being Successfully added using AddPlaceAPI 
	Given Add Place Payload with "<name>" "<language>" "<address>" 
	When user calls "addPlaceAPI" with "Post" http request 
	Then the API call is success with status code 200 
	And "status" in response body is "OK" 
	And "scope" in response body is "APP" 
	And verify place_Id created maps to "<name>" using "getPlaceAPI" 
	
	Examples: 
		|name						| language | address 						|
		|DeaDshot Home	| Tamil	   | 221 B Baker Street |
	 #|DeaDshot House | English	 | 123 Gotham City	  |

@DeletePlace @Regression
Scenario: Verify if Delete Place functionality is working 
	Given DeletePlace Payload 
	When user calls "deletePlaceAPI" with "delete" http request 
	Then the API call is success with status code 200 
	And "status" in response body is "OK"