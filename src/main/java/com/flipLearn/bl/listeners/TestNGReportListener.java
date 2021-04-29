package com.flipLearn.bl.listeners;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.flipLearn.bl.extentreports.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.HashMap;

public class TestNGReportListener implements ITestListener {

    public static synchronized String[][] getParameterArray(HashMap<String, String> hm) {
        String[][] parameters = new String[hm.size()][2];
        int row = 0;
        int column = 0;
        for (String str : hm.keySet()) {
            parameters[row][column] = "<b>" + str + "</b>";
            column++;
            parameters[row][column] = hm.get(str);
            row++;
            column = 0;
        }
        return parameters;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        addConsoleLogsToReport();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        //System.out.println("--------- Executing :- " + getMethodNameWithParams(result) + " ---------");
        ExtentManager.createTest(result.getName(), getTestDescription(result));
        addParametersInReport(result);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        logStatusToConsole(result);
        assignCategoryToTest(result);
        addExtentLabelToTest(result);
        ExtentManager.flush();
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        logStatusToConsole(result);
        assignCategoryToTest(result);
        assignExceptionToTest(result);
        addExtentLabelToTest(result);
        ExtentManager.flush();
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        if (result.wasRetried())
            removeRetriedTest();
        else {
            logStatusToConsole(result);
            assignCategoryToTest(result);
            assignExceptionToTest(result);
            addExtentLabelToTest(result);
            ExtentManager.flush();
        }
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }


    private synchronized void logStatusToConsole(ITestResult result) {
        String status = "";

        if (result.getStatus() == ITestResult.SUCCESS)
            status = "Pass";

        else if (result.getStatus() == ITestResult.FAILURE)
            status = "Fail";

        else if (result.getStatus() == ITestResult.SKIP)
            status = "Skip";

        System.out.println(getSimpleMethodName(result) + " = [" + status + "]" + System.lineSeparator());
        Reporter.log(getSimpleMethodName(result) + " = [" + status + "]<br>");
    }

    private synchronized String getSimpleClassName(ITestResult result) {
        return result.getMethod().getRealClass().getSimpleName();
    }

    private synchronized String getSimpleMethodName(ITestResult result) {
        return result.getName();
    }

    private synchronized String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription();
    }

    private synchronized void assignCategoryToTest(ITestResult result) {
        ExtentManager.getTest().assignCategory(getSimpleClassName(result));
    }

    private synchronized void assignExceptionToTest(ITestResult result) {
        ExtentManager.getTest().info(result.getThrowable());
    }

    private synchronized void addExtentLabelToTest(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS)
            ExtentManager.getTest().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));

        else if (result.getStatus() == ITestResult.FAILURE)
            ExtentManager.getTest().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));

        else
            ExtentManager.getTest().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));

    }

    private synchronized void addConsoleLogsToReport() {
        for (String s : Reporter.getOutput()) {
            ExtentManager.setTestRunnerOutput(s + System.lineSeparator());
        }
    }

    private synchronized String getMethodNameWithParams(ITestResult result) {
        String methodName = result.getName();

        if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
            methodName = methodName + result.getParameters()[0].toString();
        }

        return methodName;
    }

    private synchronized void addParametersInReport(ITestResult result) {
        if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
            ExtentManager.getTest().log(Status.PASS, MarkupHelper.createTable(getParameterArray((HashMap<String, String>) result.getParameters()[0])));
        }
    }

    private synchronized void removeRetriedTest() {
        ExtentManager.deleteCurrentTest();
        ExtentManager.flush();
    }


}