package demo;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.DataDetail;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;
import pojo.ViewOrderResponse;

public class ECommerceAPITest {

	public static void main(String[] args) {
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		// Login
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("DeaDshot@test.com");
		loginRequest.setUserPassword("Testing@123");

		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);

		LoginResponse loginResponse = reqLogin.when().post("api/ecom/auth/login").then().log().all()
				.assertThat().statusCode(200).extract().response().as(LoginResponse.class);

		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
		System.out.println(loginResponse.getToken());
		System.out.println(loginResponse.getUserId());
		Assert.assertEquals(loginResponse.getMessage(), "Login Successfully");

		// Create Product
		RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).build();
		// to pass form-data parameters
		RequestSpecification reqAddProduct = given().spec(addProductBaseReq)
				.param("productName", "Nike Air Jordan").param("productAddedBy", userId).param("productCategory", "fashion")
				.param("productSubCategory", "shoe").param("productPrice", "23456")
				.param("productDescription", "Nike Air Jordan Limited Edition Sneaker").param("productFor", "Men")
				.multiPart("productImage", new File(System.getProperty("user.dir") + "\\resources\\Sneaker.jpg"));

		String addProductResponse = reqAddProduct.when().post("/api/ecom/product/add-product").then().log().all()
				.assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		Assert.assertEquals(js.get("message"), "Product Added Successfully");
		
		//Create Order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.setContentType(ContentType.JSON).build();
	
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		orderDetailList.add(orderDetail);
		Orders orders = new Orders();
		orders.setOrders(orderDetailList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(orders);
		String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all()
				.assertThat().statusCode(201).extract().response().asString();
		JsonPath js1 = new JsonPath(responseAddOrder);
		String orderId = js1.getString("orders[0]");
		Assert.assertEquals(js1.get("message"), "Order Placed Successfully");
		
		//View Order
		RequestSpecification viewOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token).build();
		
		RequestSpecification viewOrderReq = given().log().all().spec(viewOrderBaseReq).queryParam("id", orderId);
		ViewOrderResponse viewOrderResponse = viewOrderReq.when().get("/api/ecom/order/get-orders-details").then().log().all()
			.assertThat().statusCode(200).extract().response().as(ViewOrderResponse.class);
		
		Assert.assertEquals(viewOrderResponse.getData().get_id(), orderId);
		Assert.assertEquals(viewOrderResponse.getData().getProductOrderedId(), productId);
		Assert.assertEquals(viewOrderResponse.getMessage(), "Orders fetched for customer Successfully");
		//JsonPath js2 = new JsonPath(responseViewOrder);
		//Assert.assertEquals(js2.get("data._id"), orderId);
		//Assert.assertEquals(js2.get("message"), "Orders fetched for customer Successfully");
		
		//Delete Product
		RequestSpecification deleteProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq).pathParam("productId", productId);
		String deleteProdResponse = deleteProdReq.delete("/api/ecom/product/delete-product/{productId}").then().log().all()
		.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js3 = new JsonPath(deleteProdResponse);
		Assert.assertEquals(js3.get("message"), "Product Deleted Successfully");
		
		//Delete Order
		RequestSpecification deleteOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteOrderReq = given().log().all().spec(deleteOrderBaseReq).pathParam("orderId", orderId);
		String deleteOrderResponse = deleteOrderReq.when().delete("https://rahulshettyacademy.com/api/ecom/order/delete-order/{orderId}").then().log().all()
			.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js4 = new JsonPath(deleteOrderResponse);
		Assert.assertEquals(js4.get("message"), "Orders Deleted Successfully");
		
	}
}
