package flip.api.businesslayer.BusinessLayer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import util.DBConnection;
import util.LoadProperty;
import util.Xls_Reader;

import com.jayway.restassured.internal.path.json.JSONAssertion;
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

	@Test(priority=1)
	public void loginRequest() throws JSONException,InterruptedException {
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
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
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
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
			Assert.assertEquals(profileCode, load.getProperty("ProfileCodeStu").toString());
			xls.setCellData(sheetName, "RESULT", i, "Pass");
		}
	}

	public void getClassTeacherDetailsBySectionCodes() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	@Test
	public void getClassesBySchoolCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("\n***Executing for getClassesBySchoolCode***\n");
		sheetName="getClassesBySchoolCode";
		int rowCount=xls.getRowCount(sheetName);
		for(int i=2;i<rowCount+1;i++){
			APIUrl=xls.getCellData(sheetName, "API_URL", i);
			Response response=hitGetRequest(loginId,APIUrl);
			int statusCode=response.getStatusCode();
			setResponseCodeWithResponseData(sheetName,i, statusCode, response.body().asString());
			printStatement("\nStatus Code is "+statusCode);
			System.out.println(response.body().asString());
			System.out.println("\n"+xls.getCellData(sheetName, "API_RESPONSE_Expected", i));
			Assert.assertEquals(response.body().asString(), xls.getCellData(sheetName, "API_RESPONSE_Expected", i).toString(), "");		
			xls.setCellData(sheetName, "RESULT", i, "Pass");
		}	
	}

	public void addClass() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getSectionsWithSubjectsByClass() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void setClassTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void addSection() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void removeClassTeacherBySectionCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getSubjectDetailsBySectionCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void addAsSubjectTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void removeAsSubjectTeacher() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void registerUser() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getUserByUuid() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void isUserLoggedIn() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void sendForgetVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getStaticContent() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void checkifLoginIdExists() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void verifyEmailViaOtp() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void verifyMobileViaOtp() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void sendMobileVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void sendEmailVerificationCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void verifyForgotPasswordCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void setUserPassword() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void setForgetUserPassword() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getUserByMobile() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getUserByEmail() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getUserByInviteCode() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void getClassSubjects() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}

	public void markAttendance() throws JSONException{
		loginId=load.getProperty("Student").toString();
		System.out.println("***\nExecuting for loginRequest***\n");
		getSessionToken(loginId);
		getProfileCode();

	}
}
