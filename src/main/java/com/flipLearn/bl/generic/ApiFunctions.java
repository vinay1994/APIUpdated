package com.flipLearn.bl.generic;

import com.flipLearn.bl.xlsreader.XlsReader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public class ApiFunctions {
    public static final String Content_Type = "application/json";
    public static String loginId = "satya.script";
    public static String sessionToken = "8Sne7oZLsskDfwkaxPOYsc8dN";
    public static String profileCode = "";
    public static String SupportedApiVersion = "1";

    public static Response hitGetRequest(String apiURL, String headers) {
        Response response = null;
        try {
            response = given().headers(getAPIHeaders(headers)).get(apiURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Response hitPostRequest(String apiURL, String headers, String requestBody) {
        Response response = null;
        try {
            response = given().headers(getAPIHeaders(headers)).body(requestBody).post(apiURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Response hitPutRequest(String apiURL, String headers, String requestBody) {
        Response response = null;
        try {
            response = given().headers(getAPIHeaders(headers)).body(requestBody).put(apiURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void performResponseCodeComparison(Response response, String expected) {
        int responseCode = Integer.parseInt(expected.split("\\.")[0]);
        Assert.assertEquals(response.getStatusCode(), responseCode, "Response StatusCode Comparison Failed !!");
    }

    public static void performAPIResponseComparison(Response response, String expectedResponse) {
        if (expectedResponse.length() > 2)
            Assert.assertEquals(response.body().asString(), expectedResponse, "Response String Comparison Failed !!");
    }

    public static void performAPISchemaComparison(Response response, String expectedSchema) {
        if (expectedSchema != null && expectedSchema.length() > 2)
            response.then().assertThat().body(matchesJsonSchema(expectedSchema));
    }

    public static void performAPIResponseAssertions(Response response, String assertions) {
        if (assertions != null && assertions.length() > 2) {
            SoftAssert softAssert = new SoftAssert();
            for (String assertion : assertions.split(System.lineSeparator())) {
                String jsonPath = assertion.split("=")[0];
                String expectedValue = assertion.split("=")[1];
                String actualValue = response.jsonPath().get(jsonPath).toString();
                softAssert.assertEquals(actualValue, expectedValue, "Failed for Value at JSONPATH ( " + jsonPath + " )");
            }
            softAssert.assertAll();
        }
    }

    public static Map<String, String> getAPIHeaders(String headers) {
        Map<String, String> headerMap = new HashMap<>();
        if (headers.length() > 2) {
            for (String header : headers.split("\\s+")) {
                headerMap.put(header.split(":")[0].trim(), header.split(":")[1].trim());
            }
        }
        return headerMap;
    }

    public Response hitPostRequest(String APIUrl, String APIBody) {
        Response response = null;
        try {
            RequestSpecBuilder builder = new RequestSpecBuilder();
            builder.setBody(APIBody);
            builder.setContentType(Content_Type);
            RequestSpecification requestSpec = builder.build();
            response = given().auth().preemptive().basic("", "").spec(requestSpec).when().post(APIUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response hitGetRequest(String apiURL) {
        Response response = null;
        try {
            response = given().headers("loginId", "" + loginId + "", "sessionToken", "" + sessionToken + "",
                    "profileCode", "" + profileCode + "", "SupportedApiVersion", "" + SupportedApiVersion + "")
                    .get(apiURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void setResponseCodeWithResponseData(String Sheetname, int i, int statusCode, String response) {
        XlsReader xls = new XlsReader("");
        xls.setCellData(Sheetname, "RESP_CODE", i, Integer.toString(statusCode));
        xls.setCellData(Sheetname, "API_RESPONSE", i, response);
    }

    public void printStatement(String statement) {
        System.out.println(statement);
    }
}
