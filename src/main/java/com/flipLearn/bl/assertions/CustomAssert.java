package com.flipLearn.bl.assertions;

import com.flipLearn.bl.logger.Logger;
import org.testng.Assert;

public class CustomAssert {

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
        Logger.logPass(message + " = <b>Pass</b>");
    }

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertNotEquals(String actual, String expected, String message) {
        Assert.assertNotEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }


}

