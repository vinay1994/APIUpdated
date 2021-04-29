package com.flipLearn.bl.testsuite;

import com.flipLearn.bl.constants.Constants;
import com.flipLearn.bl.enums.CSVColumns;
import com.flipLearn.bl.generic.ApiFunctions;
import com.flipLearn.bl.xlsreader.XlsReader;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;

public class ExecutionSuite_ClassFilterAPI {

    @DataProvider
    public Iterator<Object> DataProvide_CLassFilter_API() {
        String sheetName = "filterclass";
        XlsReader xlsReader = new XlsReader(Constants.FilePath.FILEPATH_CLASS_FILTER_API);
        return xlsReader.convertToDataMap(sheetName).iterator();
    }

    @Test(dataProvider = "DataProvide_CLassFilter_API")
    public void TC_ClassFilter_001_ValidateResponse(HashMap<String, String> dataHashMap) {
        String apiUrl = dataHashMap.get(CSVColumns.API_URL.name());
        String apiHeaders = dataHashMap.get(CSVColumns.API_HEADERS.name());
        String expectedResponseCode = dataHashMap.get(CSVColumns.API_RESPONSE_CODE.name());
        String expectedResponseSchema = dataHashMap.get(CSVColumns.API_SCHEMA.name());
        String expectedResponseString = dataHashMap.get(CSVColumns.API_RESPONSE_STRING.name());
        String apiResponseAssertions = dataHashMap.get(CSVColumns.API_RESPONSE_ASSERTIONS.name());

        Response response = ApiFunctions.hitGetRequest(apiUrl, apiHeaders);

        ApiFunctions.performResponseCodeComparison(response, expectedResponseCode);
        ApiFunctions.performAPISchemaComparison(response, expectedResponseSchema);
        ApiFunctions.performAPIResponseComparison(response, expectedResponseString);
        ApiFunctions.performAPIResponseAssertions(response, apiResponseAssertions);
    }

}
