package com.flipLearn.BL.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import static com.jayway.restassured.RestAssured.*;

public class AppTest{
	public static String loginId="satya.teacher";
	public static String sessionToken="";
	public static String uuid="";
	public static String profileCode="";
	public static String SupportedApiVersion="1";

	@Test(priority=1)
	public void loginRequest() throws JSONException,InterruptedException {
		String APIUrl = "http://bl.fliplearn.com/user/login";
		String APIBody = "{\"login\":{\"loginId\":\"satya.teacher\",\"password\":\"123456\",\"macAddress\":\"1c:87:2c:9e:8a:21\",\"imeiNumber\":\"357870062622548\",\"deviceName\":\"a\",\"platform\":\"web\",\"osName\":"
				+ "\"android\",\"osVersion\":\"5.0\",\"buildVersion\":\"4.1.2.5\",\"deviceCode\":\"\"}}";
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBody(APIBody);			
		builder.setContentType("application/json");			
		RequestSpecification requestSpec = builder.build();
		Response response = given()
				.authentication().preemptive().basic("", "").spec(requestSpec).when().post(APIUrl);
		long statusCode=response.getStatusCode();
		System.out.println("\nStatus Code is "+statusCode);
		JSONObject JSONResponseBody = new JSONObject(response.body().asString());
		sessionToken=JSONResponseBody.getJSONObject("data").getString("sessionToken");
		uuid=JSONResponseBody.getJSONObject("data").getString("uuid");
		System.out.println("\nsessionToke is "+sessionToken + "\nUUID is "+uuid);
		Assert.assertEquals(uuid, "188795181309");
	}
	@Test(priority=2)
	public void getUserProfile() throws JSONException{
		Response response = given().headers("loginId", ""+loginId+"", "sessionToken",""+sessionToken+"")
				.get("http://bl.fliplearn.com/group/getUserProfiles");
		long statusCode=response.getStatusCode();
		JSONObject jsonObject = new JSONObject(response.body().asString());
		System.out.println("\nStatus Code is "+statusCode);
		JSONArray jsonArray = jsonObject.getJSONArray("response");
		profileCode = jsonArray.getJSONObject(0).getString("profileCode");
		System.out.println("\nProfile code is "+profileCode);
		Assert.assertEquals(profileCode, "3984746503");
	}
}
