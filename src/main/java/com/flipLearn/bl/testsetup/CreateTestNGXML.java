package com.flipLearn.bl.testsetup;

import com.flipLearn.bl.constants.Constants;
import com.flipLearn.bl.global.GlobalData;
import org.testng.xml.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTestNGXML {

    public static void main(String[] args) throws Exception {
        createXml();
    }

    private static void createXml() throws Exception {
        String suiteType = GlobalData.SUITE_TYPE;
        String names = GlobalData.NAMES;
        //int threadCount = GlobalData.THREAD_COUNT;

        XmlSuite suite = new XmlSuite();
        suite.setName(Constants.TESTNG_XML_SUITE_NAME);
        suite.setParallel(XmlSuite.ParallelMode.CLASSES);
        //suite.setDataProviderThreadCount(threadCount);
        //suite.setThreadCount(threadCount);
        suite.setPreserveOrder(true);

        // Add Test Suite
        XmlTest test = new XmlTest(suite);
        test.setName(Constants.TESTNG_XML_TEST_NAME);
        //test.setThreadCount(threadCount);

        List<XmlPackage> packages;
        List<XmlClass> classes;

        switch (suiteType) {
            case "system":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(".*"));
                test.setPackages(packages);
                break;

            case "package":
                packages = new ArrayList<>();
                if (names == null) {
                    packages.add(new XmlPackage(".*"));
                } else {
                    String[] packageArr = names.split(",");
                    for (String s : packageArr) {
                        packages.add(new XmlPackage(Constants.BASE_PACKAGE + "." + s));
                    }
                }
                test.setPackages(packages);
                break;

            case "group":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(".*"));
                test.setPackages(packages);
                String[] groups = names.split(",");
                test.setIncludedGroups(new ArrayList<>(Arrays.asList(groups)));
                break;

            case "class":
                classes = new ArrayList<>();
                String[] classArr = names.split(",");
                for (String s : classArr) {
                    XmlClass xmlClass = new XmlClass();
                    xmlClass.setName(Constants.BASE_PACKAGE + "." + s);
                    classes.add(xmlClass);
                }
                test.setClasses(classes);
                break;

            case "method":
                classes = new ArrayList<>();
                String[] pair = names.split(":");
                XmlClass testClass = new XmlClass();
                testClass.setName(Constants.BASE_PACKAGE + "." + pair[0]);
                List<XmlInclude> methods = new ArrayList<>();
                methods.add(new XmlInclude(pair[1]));
                testClass.setIncludedMethods(methods);
                classes.add(testClass);
                test.setClasses(classes);
                break;

            default:
                break;
        }

        FileWriter writer = new FileWriter(new File(Constants.FILENAME_TESTNG_XML));
        writer.write(suite.toXml());
        writer.flush();
        writer.close();

        System.out.println("--------- Created TestNG XML ----------");
    }
}