package flip.api.businesslayer.BusinessLayer;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.testng.Assert;

import util.DBConnection;
import util.LoadProperty;
import util.Xls_Reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.model.Event;
import com.model.Payload;

public class App {
	public static String baseUlr="";
	public static String loginId="teacher_2.chabr_517788";
	public static String sessionToken="";
	public static String uuid="";
	public static String profileCode="";
	public static String SupportedApiVersion="1";
	public static final String Content_Type="application/json";
	public static String APIName="";
	public static String APIMethod="";
	public static String APIUrl="";
	public static String APIBody="";
	public static String APIResponse="";
	public static String errorMessage="";
	public static int errorCode=200;
	public static String sheetName="ExecutionSheet";
	public static String AssertionType="";


	public String setBaseUrl(){
		LoadProperty load = new LoadProperty();
		baseUlr =(String) load.getProperty("BaseUrl");
		System.out.println("Base Urls is "+baseUlr);
		return baseUlr;
	}




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


	public Response hitPostRequest(String loginid, String APIUrl,String APIBody){
		Response response = null;
		try {
			RequestSpecBuilder builder = new RequestSpecBuilder();
			builder.setBody(APIBody);			
			builder.setContentType(Content_Type);			
			RequestSpecification requestSpec = builder.build();
			response = given().headers("loginId", ""+loginid+"", "sessionToken",""+sessionToken+"",
					"profileCode",""+profileCode+"","SupportedApiVersion",""+SupportedApiVersion+"")
					.authentication().preemptive().basic("", "").spec(requestSpec).when().post(APIUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Response hitPutRequest(String loginid, String APIUrl,String APIBody){
		Response response = null;
		try {
			RequestSpecBuilder builder = new RequestSpecBuilder();
			builder.setBody(APIBody);			
			builder.setContentType(Content_Type);			
			RequestSpecification requestSpec = builder.build();
			response = given().headers("loginId", ""+loginid+"", "sessionToken",""+sessionToken+"",
					"profileCode",""+profileCode+"","SupportedApiVersion",""+SupportedApiVersion+"")
					.authentication().preemptive().basic("", "").spec(requestSpec).when().put(APIUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}



	public Response hitGetRequest(String loginid, String APIUrl){
		Response response = null;
		try {
			response = given().headers("loginId", ""+loginid+"", "sessionToken",""+sessionToken+"",
					"profileCode",""+profileCode+"","SupportedApiVersion",""+SupportedApiVersion+"")
					.get(APIUrl);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Response hitDeleteRequest(String loginid, String APIUrl,String APIBody){
		Response response = null;
		try {
			RequestSpecBuilder builder = new RequestSpecBuilder();
			builder.setBody(APIBody);			
			builder.setContentType(Content_Type);			
			RequestSpecification requestSpec = builder.build();
			response = given().headers("loginId", ""+loginid+"", "sessionToken",""+sessionToken+"",
					"profileCode",""+profileCode+"","SupportedApiVersion",""+SupportedApiVersion+"")
					.authentication().preemptive().basic("", "").spec(requestSpec).when().delete(APIUrl);
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

	public String getSessionToken(String loginId){
		DBConnection.connectUmsDB();
		try {
			ResultSet rs = DBConnection.executeQuery("SELECT * FROM ums_api.user_session where uuid in "
					+ "(select uuid from ums_api.user_master where login_id='"+loginId+"') order by updated_date desc limit 1");
			while(rs.next()){
				sessionToken=rs.getString("session_token");
				uuid=rs.getString("uuid");
				//System.out.println(sessionToken);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBConnection.disconnectDB("ums");
		}
		return sessionToken;
	}

	public String getProfileCode(){
		DBConnection.connectGroupDB();
		try {
			ResultSet rs = DBConnection.executeQuery("SELECT * from group_api.group_user_roles where group_user_id in "
					+ "(select id from group_api.group_users where uuid='"+uuid+"' and is_active=1) order by updated desc limit 1");
			while(rs.next()){
				//profileCode=rs.getString("base_profile_code");
				//System.out.println(profileCode);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DBConnection.disconnectDB("group");
		}
		return profileCode;
	}

	public void printStatement(String statement){
		System.out.println(statement);
	}

	public String getUUIDByLoginId(String loginid){
		String loginId="";

		return loginId;
	}


	public void verifyExecutionResult(Xls_Reader xls, String Sheetname, String actual, int i, String assertionType){
		try {
			if(assertionType.equalsIgnoreCase("Response")){
				Assert.assertEquals(actual, xls.getCellData(Sheetname, "API_RESPONSE_Expected", i).toString(), "");		
				xls.setCellData(Sheetname, "RESULT", i, "Pass");
				printStatement("Pass");
			}else if(assertionType.equalsIgnoreCase("Key-value")){
				Assert.assertTrue(actual.contains(xls.getCellData(Sheetname, "API_RESPONSE_Expected", i)));	
				xls.setCellData(Sheetname, "RESULT", i, "Pass");
				printStatement("Pass");
			}else if(assertionType.equalsIgnoreCase("RESP_CODE")){
				Assert.assertTrue(actual.contains(xls.getCellData(Sheetname, "API_RESPONSE_Expected", i)));	
				xls.setCellData(Sheetname, "RESULT", i, "Pass");
				printStatement("Pass");
			}
		} catch (AssertionError e) {
			xls.setCellData(Sheetname, "RESULT", i, "Fail");
			printStatement("Fail");
		}
	}


	public String getRequestJson(int i, String sheetName, String eventType){
		Xls_Reader xls=new Xls_Reader();
		Event event = new Event();
		Payload payload = new Payload();
		String jsonStr = null;
		payload.setSchoolCode(xls.getCellData(sheetName, "school_code", i));
		payload.setAyid(xls.getCellData(sheetName, "ayid", i));
		payload.setGroupCodeList(new ArrayList<String>(Arrays.asList(xls.getCellData(sheetName, "groupCodeList", i).split(","))));
		payload.setMessageCode(xls.getCellData(sheetName, "message_code", i));
		payload.setUuidList(new ArrayList<String>(Arrays.asList(xls.getCellData(sheetName, "uuid", i).split(","))));
		payload.setSmsEnabled(xls.getCellData(sheetName, "sendsms", i).equals("1") ? true : false);
		//		 event.setEventTime(new Date("2017-09-12T17:58:43.511Z"));

		String s = "2017-10-04T22:58:00.000Z";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
		Date d = null;
		try {
			d = formatter.parse(s);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("Formatted Date in current time zone = " + formatter.format(d));

		TimeZone tx=TimeZone.getTimeZone("Asia/Calcutta");
		formatter.setTimeZone(tx);
		System.out.println("Formatted date in IST = " + formatter.format(d));

		Instant instant = Instant.now();
		System.out.println(instant.toString());
		String in = instant.toString();

		event.setEventType("HOMEWORK_CREATE");
		event.setEventDate(in);
		ObjectMapper payloadMapper = new ObjectMapper();
		String payloadJson = "";
		try {
			// get event object as a json string
			payloadJson= payloadMapper.writeValueAsString(payload);
			//System.out.println(payloadJson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event.setPayload(payloadJson);
		ObjectMapper mapperObj = new ObjectMapper();

		try {
			// get event object as a json string
			jsonStr = mapperObj.writeValueAsString(event);
			//System.out.println(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}










}
