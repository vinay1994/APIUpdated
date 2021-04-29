package com.flipLearn.bl.global;

public class GlobalData {

    public static final String EXTENT_REPORT_DOCUMENT_TITLE = "API Automation Reports";
    public static final String EXTENT_REPORT_REPORT_NAME = "Fliplearn API Automation Report";
    public static final String OUTPUT_FOLDER_REPORT = "extentreport/";
    public static final String OUTPUT_FOLDER_SCREENSHOTS = "screenshots/";
    public static final String REPORT_ENCODING = "utf-8";
    public static final String FILE_NAME_REPORT = "/extentreport.html";
    public static final String FILE_NAME_REPORT_SCREENSHOT = "reportscreenshot";

    public static final String SUITE_TYPE = System.getProperty("suitetype", "system");
    public static final String ENVIRONMENT = System.getProperty("environment", "live");
    public static final String NAMES = System.getProperty("names", "");

    public static String BASE_URL;

    static {
        System.out.println("Environment = " + ENVIRONMENT.toUpperCase());
        System.out.println("Suite Type = " + SUITE_TYPE.toUpperCase());
        if (NAMES.length() > 0)
            System.out.println("Names = " + NAMES.toUpperCase());
    }

}
