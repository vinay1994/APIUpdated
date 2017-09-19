package flip.api.businesslayer.BusinessLayer;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.testng.Assert;
import util.DBConnection;
import util.Xls_Reader;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class App {
	public static String loginId="";
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
				System.out.println(sessionToken);
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
				profileCode=rs.getString("base_profile_code");
				System.out.println(profileCode);
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


	public void verifyExecutionResult(Xls_Reader xls, String Sheetname, String actual, int i){
		try {
			Assert.assertEquals(actual, xls.getCellData(Sheetname, "API_RESPONSE_Expected", i).toString(), "");		
			xls.setCellData(Sheetname, "RESULT", i, "Pass");
		} catch (AssertionError e) {
			xls.setCellData(Sheetname, "RESULT", i, "Fail");
		}
	}


}
