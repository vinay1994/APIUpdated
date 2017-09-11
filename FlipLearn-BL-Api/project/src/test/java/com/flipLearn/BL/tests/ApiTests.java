package com.flipLearn.BL.tests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.flipLearn.BL.methods.ApiMethods;
import com.flipLearn.Bl.utilities.Xls_Reader;
import com.jayway.restassured.response.Response;

public class ApiTests extends ApiMethods{
	Xls_Reader xls;
	@BeforeClass
	public void initialize(){
		xls=new Xls_Reader();
	}
	@Test(priority=1)
	public void loginRequest() throws JSONException,InterruptedException {
		sheetName="Login";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl = xls.getCellData(sheetName, "API_URL", i);
			APIBody = xls.getCellData(sheetName, "API_REQUEST", i).trim();
			APIResponse=xls.getCellData(sheetName, "API_RESPONSE_Expected", i).trim();
			Response response=hitPostRequest(APIUrl, APIBody);
			int statusCode=response.getStatusCode();
			JSONObject JSONResponseBody = new JSONObject(response.body().asString());
			setResponseCodeWithResponseData(sheetName,i,statusCode,response.body().asString());
			if(statusCode==200 && (!response.body().asString().contains(":40"))){
				System.out.println("\nStatus Code is "+statusCode);
				sessionToken=JSONResponseBody.getJSONObject("data").getString("sessionToken");
				uuid=JSONResponseBody.getJSONObject("data").getString("uuid");
				System.out.println("\nsessionToke is "+sessionToken);
				Assert.assertEquals(uuid, xls.getCellData(sheetName, "UUID", 2));
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			}else if(response.body().asString().contains(":40")){
				errorMessage=JSONResponseBody.getJSONObject("error").getString("errorMessage");
				errorCode=JSONResponseBody.getJSONObject("error").getInt("errorCode");
				System.out.println("\nerror Code is "+errorCode);
				System.out.println(errorMessage);
				Assert.assertEquals(errorMessage, xls.getCellData(sheetName, "API_RESPONSE_Expected", i));
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			}
		}
	}
	@Test(priority=2)
	public void getUserProfile() throws JSONException{
		sheetName="getUserProfile";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			JSONObject jsonObject = new JSONObject(response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			JSONArray jsonArray = jsonObject.getJSONArray("response");
			profileCode = jsonArray.getJSONObject(0).getString("profileCode");
			printStatement("\nProfile code is "+profileCode);
			Assert.assertEquals(profileCode, xls.getCellData(sheetName, "profileCode", 2));
			xls.setCellData(sheetName, "RESULT", i, "Pass");
		}
	}
}
