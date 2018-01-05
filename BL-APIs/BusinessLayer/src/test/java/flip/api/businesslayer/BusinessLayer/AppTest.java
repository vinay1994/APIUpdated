package flip.api.businesslayer.BusinessLayer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.sql.SQLException;
import util.LoadProperty;
import util.Xls_Reader;
import com.jayway.restassured.response.Response;

public class AppTest extends App{
	LoadProperty load;
	@BeforeMethod
	public void initialize() throws ClassNotFoundException, SQLException, IOException{
		load=new LoadProperty();
		load.setPropertyPath();
		setBaseUrl();
	}

	@Test
	public void executeAndVerifyResult(){
		Xls_Reader xls=new Xls_Reader();
		sheetName="ExecutionSheet";
		int statusCode=200;
		int rowCount=xls.getRowCount(sheetName);
		System.out.println("Row count is   "+rowCount);
		for(int i=2;i<rowCount+1;i++){
			APIName=xls.getCellData(sheetName, "API_NAME", i);
			loginId=(String) load.getProperty(xls.getCellData(sheetName, "RoleType", i));
			if(!(APIName.equalsIgnoreCase("Login"))){
				getSessionToken(loginId);
				getProfileCode();
			}	else{
				sessionToken="";
			}
			printStatement("\n*********************************************");
			printStatement("Executing for "+APIName+"");
			printStatement("*********************************************");
			APIUrl = baseUlr + xls.getCellData(sheetName, "API_URL", i);
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
				if(AssertionType.equalsIgnoreCase("RESP_CODE")){
					verifyExecutionResult(xls, sheetName, String.valueOf(statusCode), i,AssertionType);
				}else{
					verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
				}
			}else if(APIMethod.equalsIgnoreCase("POST")){
				Response response=hitPostRequest(loginId,APIUrl, APIBody);
				try {
					statusCode=response.getStatusCode();
				} catch (Exception e) {}
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				if(AssertionType.equalsIgnoreCase("RESP_CODE")){
					verifyExecutionResult(xls, sheetName, String.valueOf(statusCode), i,AssertionType);
				}else{
					verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
				}
			}else if(APIMethod.equalsIgnoreCase("PUT")){
				Response response=hitPutRequest(loginId,APIUrl, APIBody);
				statusCode=response.getStatusCode();
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				if(AssertionType.equalsIgnoreCase("RESP_CODE")){
					verifyExecutionResult(xls, sheetName, String.valueOf(statusCode), i,AssertionType);
				}else{
					verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
				}
			}else if(APIMethod.equalsIgnoreCase("DELETE")){
				Response response=hitPutRequest(loginId,APIUrl, APIBody);
				statusCode=response.getStatusCode();
				printStatement("Status Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				if(AssertionType.equalsIgnoreCase("RESP_CODE")){
					verifyExecutionResult(xls, sheetName, String.valueOf(statusCode), i,AssertionType);
				}else{
					verifyExecutionResult(xls, sheetName, response.body().asString(), i,AssertionType);
				}
			}
		}
	}

	// Do not execute Below Function
	//	public void createAnnouncement(){
	//		sheetName="homework_create_uuid";
	//		APIName="http://commevent.fliplearn.com:8084/event";
	//		int rowCount=xls.getRowCount(sheetName);
	//		for(int i=2;i<rowCount+1;i++){
	//			APIBody= getRequestJson(i, sheetName, "HOMEWORK_CREATE");
	//			printStatement(APIName);
	//			printStatement(APIBody);
	//			Response response=hitPostRequest(APIName, APIBody);
	//			System.out.println("Response is-->>> "+response.asString());
	//			int statusCode=response.getStatusCode();
	//			printStatement("Status Code is "+statusCode);
	//			xls.setCellData(sheetName, "Result", i, response.asString());
	//		}
	//	}
	//
	//
	//	@Test
	//	public void hitTopicIds(){
	//		String Base_url="https://stgtoc.fliplearn.com:8087/";
	//		String topicId,resourceId,resourceName,type,title,wouzaUrl;
	//		int counter=2;
	//		Response response = null;
	//		JSONArray jsonArray = null;
	//		JSONObject jsonObject;
	//		xls=new Xls_Reader();
	//		Logger logger;
	//		try {  
	//			FileHandler fh;
	//			fh = new FileHandler("/Users/satya/Desktop/Logger/logger.txt");  
	//			logger = Logger.getLogger("MyLog");  
	//			logger.setUseParentHandlers(false);
	//			logger.addHandler(fh);
	//			SimpleFormatter formatter = new SimpleFormatter();  
	//			fh.setFormatter(formatter);
	//			for(int i=2;i<xls.getRowCount("topicId");i++){
	//				topicId=xls.getCellData("topicId", "topic_Id", i);
	//				System.out.println("\nRunning for topic id-->> "+topicId);
	//				response = hitGetRequest("satya.staging", Base_url+"v1/resource?topicId="+topicId+"");
	//				try {
	//					jsonObject = new JSONObject(response.body().asString());
	//					jsonArray = jsonObject.getJSONArray("response");
	//				} catch (Exception e) {}
	//
	//				for(int j=0;j<jsonArray.length();j++){
	//					//System.out.println(response.asString());	
	//					logger.info("\n"+topicId+";"+jsonArray.getJSONObject(j).get("type").toString()+";"
	//							+jsonArray.getJSONObject(j).get("title").toString()+";"+jsonArray.getJSONObject(j).get("wouzaUrl").toString());  							
	//				}
	//			}
	//		}
	//		catch(Exception e){
	//			e.printStackTrace();
	//		}
	//	}
	//	@Test
	//	public void createAdmissionNumber(){
	//		load=new LoadProperty();
	//		xls=new Xls_Reader();
	//		sheetName="ExecutionSheet";
	//		int statusCode=200;
	//		int rowCount=xls.getRowCount(sheetName);
	//		//System.out.println("Row count"+rowCount);
	//		for(int i=2;i<=rowCount;i++){
	//			loginId=(String) load.getProperty(xls.getCellData(sheetName, "RoleType", i));
	//			//getSessionToken(loginId);
	//			//getProfileCode();
	//			APIName=xls.getCellData(sheetName, "API_NAME", i);
	//			printStatement("\n*********************************************");
	//			printStatement("Executing for "+APIName+"");
	//			printStatement("*********************************************");
	//			APIUrl = baseUlr + xls.getCellData(sheetName, "API_URL", i);
	//			APIBody = xls.getCellData(sheetName, "API_REQUEST", i).trim();
	//			APIMethod=xls.getCellData(sheetName, "API_METHOD", i).trim();
	//			if(APIMethod.equalsIgnoreCase("POST")){
	//				Response response=hitPostRequest(loginId,APIUrl, APIBody);
	//				try {
	//					statusCode=response.getStatusCode();
	//				} catch (Exception e) {}
	//				printStatement("Status Code is "+statusCode);
	//				printStatement(APIBody);
	//				printStatement(response.body().asString()+"\n");
	//				//setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
	//			}
	//		}
	//	}
}