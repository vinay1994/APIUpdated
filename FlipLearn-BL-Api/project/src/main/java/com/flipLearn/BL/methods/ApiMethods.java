package com.flipLearn.BL.methods;

import static com.jayway.restassured.RestAssured.given;

import com.flipLearn.BL.utilities.Xls_Reader;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class ApiMethods {
	public static String loginId="satya.script";
	public static String sessionToken="8Sne7oZLsskDfwkaxPOYsc8dN";
	public static String uuid="";
	public static String profileCode="";
	public static String SupportedApiVersion="1";
	public static final String Content_Type="application/json";
	public static String APIUrl="";
	public static String APIBody="";
	public static String APIResponse="";
	public static String errorMessage="";
	public static int errorCode=200;
	public static String sheetName="";
	
	
	public Response hitPostRequest(String APIUrl,String APIBody){
		Response response = null;
		try {
			RequestSpecBuilder builder = new RequestSpecBuilder();
			builder.setBody(APIBody);			
			builder.setContentType(Content_Type);			
			RequestSpecification requestSpec = builder.build();
			response = given()
					.authentication().preemptive().basic("", "").spec(requestSpec).when().post(APIUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public Response hitGetRequest(String APIUrl){
		Response response = null;
		try {
			response = given().headers("loginId", ""+loginId+"", "sessionToken",""+sessionToken+"",
					"profileCode",""+profileCode+"","SupportedApiVersion",""+SupportedApiVersion+"")
					.get(APIUrl);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public void setResponseCodeWithResponseData(String Sheetname, int i, int statusCode, String response){
		Xls_Reader xls = new Xls_Reader();
		xls.setCellData(Sheetname, "RESP_CODE", i, Integer.toString(statusCode));
		xls.setCellData(Sheetname, "API_RESPONSE", i, response);
	}
	
	public void printStatement(String statement){
		System.out.println(statement);
	}
}
