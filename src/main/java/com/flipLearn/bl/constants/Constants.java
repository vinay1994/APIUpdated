package com.flipLearn.bl.constants;

import com.flipLearn.bl.global.GlobalData;

public class Constants {
    public static final String TESTNG_XML_SUITE_NAME = "FlipLearn API ";
    public static final String TESTNG_XML_TEST_NAME = "API Automation Suite";
    public static final String FILENAME_TESTNG_XML = "testng.xml";
    public static final String BASE_PACKAGE = "testsuite";


    public static final class FilePath {
        private static final String FILEPATH_TEST_DATA_DIR = "./src/test/resources/testdata/" + GlobalData.ENVIRONMENT.toLowerCase() + "/";
        public static final String FILEPATH_CLASS_FILTER_API = FILEPATH_TEST_DATA_DIR + "TestCases_BLModule.xlsx";
    }
}
