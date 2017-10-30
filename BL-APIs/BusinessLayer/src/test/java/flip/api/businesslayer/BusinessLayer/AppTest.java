package flip.api.businesslayer.BusinessLayer;

import java.io.IOException;
import java.sql.SQLException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import util.LoadProperty;
import util.Xls_Reader;

import com.jayway.restassured.response.Response;

public class AppTest extends App{
	static Xls_Reader xls;
	static LoadProperty load;

	@BeforeClass
	public void initialize() throws ClassNotFoundException, SQLException, IOException{
		//xls=new Xls_Reader();
		//load=new LoadProperty();
	}
	@AfterClass
	public void CloseProcesses() throws ClassNotFoundException, SQLException{
	}


	@Test
	public void executeAndVerifyResult(){
		load=new LoadProperty();
		xls=new Xls_Reader();
		sheetName="ExecutionSheet";
		int statusCode=200;
		//int rowCount=xls.getRowCount(sheetName);
		//System.out.println("Row count"+rowCount);
		for(int i=2;i<120;i++){
			loginId=(String) load.getProperty(xls.getCellData(sheetName, "RoleType", i));
			getSessionToken(loginId);
			getProfileCode();
			APIName=xls.getCellData(sheetName, "API_NAME", i);
			printStatement("\n*********************************************");
			System.out.println("Executing for "+APIName+"");
			//printStatement("*********************************************");
			APIUrl = xls.getCellData(sheetName, "API_URL", i);
			APIBody = xls.getCellData(sheetName, "API_REQUEST", i).trim();
			APIResponse=xls.getCellData(sheetName, "API_RESPONSE_Expected", i).trim();
			APIMethod=xls.getCellData(sheetName, "API_METHOD", i).trim();
			AssertionType=xls.getCellData(sheetName, "AssertionType", i).trim();
			if(APIMethod.equalsIgnoreCase("GET")){
				Response response=hitGetRequest(loginId,APIUrl);
				try {
					statusCode=response.getStatusCode();
				} catch (Exception e) {}
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
			}else if(APIMethod.equalsIgnoreCase("POST")){
				Response response=hitPostRequest(loginId,APIUrl, APIBody);
				statusCode=response.getStatusCode();
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
			}else if(APIMethod.equalsIgnoreCase("PUT")){
				Response response=hitPutRequest(loginId,APIUrl, APIBody);
				statusCode=response.getStatusCode();
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
			}else if(APIMethod.equalsIgnoreCase("Delete")){
				Response response=hitPutRequest(loginId,APIUrl, APIBody);
				statusCode=response.getStatusCode();
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, String.valueOf(statusCode), i,AssertionType);
			}
		}
	}

	// Do not execute Below Function
	public void createAnnouncement(){
		sheetName="homework_create_uuid";
		APIName="http://commevent.fliplearn.com:8084/event";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIBody= getRequestJson(i, sheetName, "HOMEWORK_CREATE");
			printStatement(APIName);
			printStatement(APIBody);
			Response response=hitPostRequest(APIName, APIBody);
			System.out.println("Response is-->>> "+response.asString());
			int statusCode=response.getStatusCode();
			printStatement("Status Code is "+statusCode);
			xls.setCellData(sheetName, "Result", i, response.asString());
		}
	}

}
