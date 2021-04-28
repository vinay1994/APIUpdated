package com.flipLearn.BL.tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.flipLearn.BL.methods.ApiMethods;
import com.jayway.restassured.response.Response;

public class CreateHomoWorkAPITest extends ApiMethods{
com.flipLearn.BL.utilities.Xls_Reader xls;

	@BeforeClass
	public void initialize(){
		xls=new com.flipLearn.BL.utilities.Xls_Reader();
	}
	
	
	@Test(priority=1)
	public void homeworkfilterhierarchy() throws JSONException{
		sheetName="homeworkfilterhierarchy";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());		
			JSONObject jsonObject = new JSONObject((response.body().asString()));
			if(statusCode==200 && (response.body().asString().contains(":40"))) {
				errorMessage=jsonObject.getJSONObject("error").getString("errorMessage");
			printStatement("\nerrorMessage is "+errorMessage);
			Assert.assertEquals(errorMessage, xls.getCellData(sheetName, "API_RESPONSE_Expected", i));
			xls.setCellData(sheetName, "RESULT", i, "Pass");
			}
		}
	
	
	
	}
}
