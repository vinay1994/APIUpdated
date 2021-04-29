package com.flipLearn.bl.generic;

import com.flipLearn.bl.extentreports.ExtentManager;
import com.flipLearn.bl.global.GlobalData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class GenericFunctions {

    public static synchronized void initExtentReports() {
        ExtentManager.createInstance();
    }

    public synchronized static void initDirectories() {
        try {
            File reportDirectory = new File(GlobalData.OUTPUT_FOLDER_REPORT);
            FileUtils.forceMkdir(reportDirectory);
            FileUtils.cleanDirectory(reportDirectory);
            File screenshotsDirectory = new File(GlobalData.OUTPUT_FOLDER_REPORT + GlobalData.OUTPUT_FOLDER_SCREENSHOTS);
            FileUtils.forceMkdir(screenshotsDirectory);
            FileUtils.cleanDirectory(screenshotsDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addExecutionDetailsExtentReport() {
        ExtentManager.addSystemInfo("Environment", StringUtils.capitalize(GlobalData.ENVIRONMENT));
        ExtentManager.addSystemInfo("SuiteType", StringUtils.capitalize(GlobalData.SUITE_TYPE));
        if (!GlobalData.SUITE_TYPE.equalsIgnoreCase("system"))
            ExtentManager.addSystemInfo("Names", StringUtils.capitalize(GlobalData.NAMES));
    }

}
