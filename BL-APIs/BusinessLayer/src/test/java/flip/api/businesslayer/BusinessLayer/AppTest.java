package flip.api.businesslayer.BusinessLayer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import util.LoadProperty;
import util.Xls_Reader;

import com.jayway.restassured.response.Response;

public class AppTest extends App{
	Xls_Reader xls;
	LoadProperty load;
	@BeforeClass
	public void initialize() throws ClassNotFoundException, SQLException, IOException{
		xls=new Xls_Reader();
		load=new LoadProperty();
	}
	@AfterClass
	public void CloseProcesses() throws ClassNotFoundException, SQLException{
	}


	@Test
	public void executeAndVerifyResult(){
		loginId=load.getProperty("Student").toString();
		sheetName="ExecutionSheet";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			getSessionToken(loginId);
			getProfileCode();
			APIName=xls.getCellData(sheetName, "API_NAME", i);
			printStatement("\n*********************************************");
			System.out.println("Executing for "+APIName+"");
			printStatement("*********************************************");
			APIUrl = xls.getCellData(sheetName, "API_URL", i);
			APIBody = xls.getCellData(sheetName, "API_REQUEST", i).trim();
			APIResponse=xls.getCellData(sheetName, "API_RESPONSE_Expected", i).trim();
			APIMethod=xls.getCellData(sheetName, "API_METHOD", i).trim();
			if(APIMethod.equalsIgnoreCase("GET")){
				Response response=hitGetRequest(loginId,APIUrl);
				int statusCode=response.getStatusCode();
				printStatement("\nStatus Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i);
			}else if(APIMethod.equalsIgnoreCase("POST")){
				Response response=hitPostRequest(loginId,APIUrl, APIBody);
				int statusCode=response.getStatusCode();
				printStatement("\nStatus Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i);
			}else if(APIMethod.equalsIgnoreCase("PUT")){
				Response response=hitPutRequest(loginId,APIUrl, APIBody);
				int statusCode=response.getStatusCode();
				printStatement("\nStatus Code is "+statusCode);
				setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
				verifyExecutionResult(xls, sheetName, response.body().asString(), i);
			}
		}
	}






























	////@Testiority=1)
	public void loginRequest() throws JSONException,InterruptedException {
		loginId=load.getProperty("Student").toString();
		getSessionToken(loginId);
		printStatement("\n*********************************************");
		System.out.println("Executing for loginRequest");
		printStatement("*********************************************");
		sheetName="Login";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl = xls.getCellData(sheetName, "API_URL", i);
			APIBody = xls.getCellData(sheetName, "API_REQUEST", i).trim();
			APIResponse=xls.getCellData(sheetName, "API_RESPONSE_Expected", i).trim();
			Response response=hitPostRequest(loginId,APIUrl, APIBody);
			int statusCode=response.getStatusCode();
			JSONObject JSONResponseBody = new JSONObject(response.body().asString());
			setResponseCodeWithResponseData(sheetName,i,statusCode,response.body().asString());
			//System.out.println(response.body().asString());
			if(statusCode==200 && (!response.body().asString().contains(":40"))){
				System.out.println("\nStatus Code is "+statusCode);
				sessionToken=JSONResponseBody.getJSONObject("data").getString("sessionToken");
				uuid=JSONResponseBody.getJSONObject("data").getString("uuid");
				System.out.println("\nsessionToke is "+sessionToken);
				AssertJUnit.assertEquals(uuid, load.getProperty("UUIDStu").toString());
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			}else if(response.body().asString().contains(":40")){
				errorMessage=JSONResponseBody.getJSONObject("error").getString("errorMessage");
				errorCode=JSONResponseBody.getJSONObject("error").getInt("errorCode");
				System.out.println("\nerror Code is "+errorCode);
				System.out.println(errorMessage);
				AssertJUnit.assertEquals(errorMessage, xls.getCellData(sheetName, "API_RESPONSE_Expected	", i));
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			}
		}
	}

	////@Testiority=2)
	public void getUserProfile() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n***********************************");
		System.out.println("Executing for getUserProfile");
		printStatement("*************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getUserProfile";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			JSONObject jsonObject = new JSONObject(response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			JSONArray jsonArray = jsonObject.getJSONArray("response");
			profileCode = jsonArray.getJSONObject(0).getString("profileCode");
			printStatement("\nProfile code is "+profileCode);
			try {
				AssertJUnit.assertEquals(profileCode, load.getProperty("ProfileCodeStu").toString());
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void getClassTeacherDetailsBySectionCodes() throws JSONException{
		loginId=load.getProperty("Teacher").toString();
		printStatement("\n************************************************");
		System.out.println("Executing for getClassTeacherDetailsBySectionCodes");
		printStatement("************************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getClassTeacherBySectionCode";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void getClassesBySchoolCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getClassesBySchoolCode");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getClassesBySchoolCode";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void addClass() throws JSONException{
		loginId=load.getProperty("Admin").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for addClass");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="addClass";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitPostRequest(loginId, APIUrl, xls.getCellData(sheetName, "API_REQUEST", i));
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void getSectionsWithSubjectsByClass() throws JSONException{
		loginId=load.getProperty("Admin").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getSectionsWithSubjectsByClass***");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getSectionsWithSubjectsByClass";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	

	}

	//@Test
	public void getUserByUuid() throws JSONException{
		loginId=load.getProperty("Teacher").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getUserByUuid");
		printStatement("***********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getUserByUuid";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			if(response.body().asString().contains(":40")){
				try {
					AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
					xls.setCellData(sheetName, "RESULT", i, "Pass");
				} catch (AssertionError e) {
					xls.setCellData(sheetName, "RESULT", i, "Fail");
				}
			}else{
				JSONObject jsonObject = new JSONObject(response.body().asString());
				printStatement("\nStatus Code is "+statusCode);
				JSONArray jsonArray = jsonObject.getJSONArray("user");
				String loginId_new = jsonArray.getJSONObject(0).getString("loginId");
				String UUID_new = jsonArray.getJSONObject(0).getString("uuid");
				try {
					AssertJUnit.assertEquals(loginId_new, loginId, "");
					AssertJUnit.assertEquals(UUID_new, uuid, "");
					xls.setCellData(sheetName, "RESULT", i, "Pass");
				} catch (AssertionError e) {
					xls.setCellData(sheetName, "RESULT", i, "Fail");
				}
			}	
		}	
	}

	//@Test
	public void getUserProfileAccountView() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getUserProfileAccountView");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getUserProfileAccountView";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			JSONObject jsonObject = new JSONObject(response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			String loginId_new=jsonObject.getJSONObject("userAccountView").getString("loginId");
			String UUID_new=jsonObject.getJSONObject("userAccountView").getString("uuid");
			try {
				AssertJUnit.assertEquals(loginId_new, loginId, "");
				AssertJUnit.assertEquals(UUID_new, uuid, "");
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void isUserLoggedIn() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for isUserLoggedIn");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="isUserLoggedIn";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitPostRequest(loginId, APIUrl, xls.getCellData(sheetName, "API_REQUEST", i));
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void getStaticContent() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getStaticContent");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getStaticContent";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void checkifLoginIdExists() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for checkifLoginIdExists");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="checkifLoginIdExists";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void getUserByMobile() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getUserByMobile");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getUserByMobile";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}	
	}

	//@Test
	public void getUserByEmail() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getUserByEmail");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getUserByEmail";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test	
	public void setUserPassword() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for setUserPassword");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="setUserPassword";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitPutRequest(loginId, APIUrl, xls.getCellData(sheetName, "API_REQUEST", i));
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void getSchoolDetailBySchoolInviteCode(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getSchoolDetailBySchoolInviteCode");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getSchoolDetailByInviteCode";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void getInviteCodesForRoles(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getInviteCodesForRoles");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getInviteCodesForRoles";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void getFlipMasterSubjectList(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for getFlipMasterSubjectList");
		printStatement("*********************************************");
		getSessionToken(loginId);
		getProfileCode();
		sheetName="getFlipMasterSubjectList";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			try {
				AssertJUnit.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(sheetName, "RESULT", i, "Pass");
			} catch (AssertionError e) {
				xls.setCellData(sheetName, "RESULT", i, "Fail");
			}
		}
	}

	//@Test
	public void unlinkParentToStudent(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for unlinkParentToStudent");
		printStatement("*********************************************");
	}


	//@Test
	public void addBulkSectionsForClass(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for addBulkSectionsForClass");
		printStatement("*********************************************");
	}

	//@Test
	public void addBulkSubjectsForSchool(){
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for addBulkSubjectsForSchool");
		printStatement("*********************************************");
	}


	//@Test
	public void verifyEmailViaOtp() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for verifyEmailViaOtp");
		printStatement("*********************************************");

	}

	//@Test
	public void verifyMobileViaOtp() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for verifyMobileViaOtp");
		printStatement("*********************************************");

	}

	//@Test
	public void sendMobileVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for sendMobileVerificationCode");
		printStatement("*********************************************");

	}

	//@Test
	public void sendEmailVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for sendMobileVerificationCode");
		printStatement("*********************************************");

	}

	//@Test
	public void verifyForgotPasswordCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for sendMobileVerificationCode");
		printStatement("*********************************************");

	}

	//@Test
	public void setForgetUserPassword() throws JSONException{
		loginId=load.getProperty("Student").toString();
		printStatement("\n*********************************************");
		System.out.println("Executing for sendMobileVerificationCode");
		printStatement("*********************************************");

	}

	public void getClassSubjects() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getUserByInviteCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}


	public void setClassTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void addSection() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void removeClassTeacherBySectionCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	//Pending for Review
	public void getSubjectDetailsBySectionCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void addAsSubjectTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void removeAsSubjectTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void registerUser() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}




	public void sendForgetVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}




	public void markAttendance() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("Executing for loginRequest***");
		getSessionToken(loginId);
		getProfileCode();

	}
}
