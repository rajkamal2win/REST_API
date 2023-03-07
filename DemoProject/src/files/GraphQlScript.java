package files;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class GraphQlScript {

	public static void main(String[] args) {
		// Query
		int characterId = 1369;
		String queryResonse = given().log().all().header("Content-Type", "application/json").body(
				"{\"query\":\"query ($characterId: Int!, $locationId: Int!, $episodeId: Int!) {\\n  character(characterId: $characterId) {\\n    name\\n    status\\n    type\\n    species\\n    id\\n  }\\n  location(locationId: $locationId) {\\n    name\\n    dimension\\n  }\\n  episode(episodeId: $episodeId) {\\n    id\\n    air_date\\n\\t\\tepisode\\n    name\\n  }\\n  characters(filters: {name: \\\"Rahul\\\"}, pagination: {limit: 10, page: 3}) {\\n    info {\\n      count\\n      pages\\n    }\\n    result {\\n      name\\n      type\\n      gender\\n    }\\n  }\\n  episodes(filters: {name: \\\"GOT\\\", episode: \\\"Red Wedding\\\"}) {\\n    info {\\n      next\\n      prev\\n    }\\n    result {\\n      air_date\\n      name\\n      episode\\n    }\\n  }\\n}\\n\",\"variables\":{\"characterId\":"+characterId+",\"locationId\":1815,\"episodeId\":1036}}")
				.when().post("https://rahulshettyacademy.com/gq/graphql").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js = new JsonPath(queryResonse);
		String characterName = js.get("data.character.name");
		System.out.println(characterName);
		Assert.assertEquals(characterName, "Arya Stark");
		
		//Mutations
		String newCharacter = "Jon Snow";
		String mutationResonse = given().log().all().header("Content-Type", "application/json").body(
				"{\"query\":\"mutation ($locationName: String!, $characterName: String!, $episodeName: String!) {\\n  createLocation(location: {name: $locationName, type: \\\"House of Stark\\\", dimension: \\\"001\\\"}) {\\n    id\\n  }\\n  createCharacter(character: {name: $characterName, type: \\\"No One\\\", status: \\\"Alive\\\", species: \\\"Stark\\\", gender: \\\"female\\\", image: \\\"NoOne.jpg\\\", originId: 1814, locationId: 1814}) {\\n    id\\n  }\\n  createEpisode(episode: {name: $episodeName, air_date: \\\"July 2017\\\", episode: \\\"S07E01\\\"}) {\\n    id\\n  }\\n  deleteLocations(locationIds: [1816]) {\\n    locationsDeleted\\n  }\\n}\\n\",\"variables\":{\"locationName\":\"The Wall\",\"characterName\":\""+newCharacter+"\",\"episodeName\":\"Battle of the Bastards\"}}")				
				.when().post("https://rahulshettyacademy.com/gq/graphql").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		js = new JsonPath(mutationResonse);
		int characterID = js.get("data.createCharacter.id");
		System.out.println(characterID);
	}

}
