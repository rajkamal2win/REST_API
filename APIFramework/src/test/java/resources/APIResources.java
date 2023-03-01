package resources;

//enum is a special class in java which has collection of constants or methods
public enum APIResources {

	addPlaceAPI("/maps/api/place/add/json"),
	getPlaceAPI("/maps/api/place/get/json"),
	deletePlaceAPI("/maps/api/place/delete/json"),
	updatePlaceAPI("/maps/api/place/update/json");

	private String resource;
	
	APIResources(String resource) {
		this.resource=resource;
	}
	
	public String getResource () {
		return resource;
	}
	
}
