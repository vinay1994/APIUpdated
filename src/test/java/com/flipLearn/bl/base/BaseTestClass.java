package com.flipLearn.bl.base;

import com.flipLearn.bl.generic.GenericFunctions;
import com.flipLearn.bl.listeners.TestNGReportListener;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestNGReportListener.class)
public class BaseTestClass {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("================= Inside Before Suite =================");
        GenericFunctions.initDirectories();
        GenericFunctions.initExtentReports();
        GenericFunctions.addExecutionDetailsExtentReport();
    }
}
