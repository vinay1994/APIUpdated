package com.flipLearn.bl.logger;

import com.aventstack.extentreports.Status;
import com.flipLearn.bl.extentreports.ExtentManager;

public class Logger {

    public static void logInfo(String log) {
        if (ExtentManager.getTest() != null)
            ExtentManager.getTest().log(Status.INFO, log);
    }

    public synchronized static void logDebug(String log) {
        if (ExtentManager.getTest() != null)
            ExtentManager.getTest().log(Status.INFO, log);
    }

    public synchronized static void logPass(String log) {
        if (ExtentManager.getTest() != null)
            ExtentManager.getTest().log(Status.PASS, log);
    }
}
