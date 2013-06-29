package com.dd.test.catpaw.reports.reporter.runtimereport;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import com.dd.test.catpaw.platform.config.ServiceLoaderManager;
import com.dd.test.catpaw.reports.reporter.html.ReportDataGenerator;


/**
 * RuntimeReporter is a listener which gets invoked when a test method or
 * configuration method starts or completed.
 * 
 */
public class RuntimeReporterListener implements IResultListener2 {

    String outputDirectory;
    RuntimeReportXMLHelper helper;

    private static volatile boolean bInitConfig = false;

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    public RuntimeReporterListener() {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
//    //        // logger.log(Level.SEVERE, e.getMessage(), e);
        }

        Document doc = docBuilder.newDocument();
        helper = new RuntimeReportXMLHelper(doc);

        Element rootElement = helper.createElement("Runtime_Reporter");
        doc.appendChild(rootElement);

    }

    public void onTestStart(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });

        updateTestDetails(result);

//        // logger.exiting();

    }

    public void onTestSuccess(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });

        updateTestDetails(result);

//        // logger.exiting();

    }

    public void onTestFailure(ITestResult result) {

//        // logger.entering(new Object[] { result });

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }
        updateTestDetails(result);

//        // logger.exiting();

    }

    public void onTestSkipped(ITestResult result) {

//        // logger.entering(new Object[] { result });

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }
        updateTestDetails(result);

//        // logger.exiting();
    }

    public void updateTestDetails(ITestResult result) {

//        // logger.entering(new Object[] { result });

        List<String> groups = Arrays.asList(result.getMethod().getGroups());
        if (result.getTestContext().getIncludedGroups() == null || result.getTestContext().getIncludedGroups().length==0) {
            // No Groups for this method
            helper.insertTestMethod(result.getTestContext().getSuite().getName(), result.getTestContext()
                    .getCurrentXmlTest().getName(), null, result.getTestClass().getName(), result);

        } else {
            // Adding method in all the Groups
            for (String tempGroup : groups) {
                if (result.getTestContext().getIncludedGroups() != null
                        && Arrays.asList(result.getTestContext().getIncludedGroups()).contains(tempGroup)) {
                    helper.insertTestMethod(result.getTestContext().getSuite().getName(), result.getTestContext()
                            .getCurrentXmlTest().getName(), tempGroup, result.getTestClass().getName(), result);
                }
            }

        }

        helper.writeXML(outputDirectory);

//        // logger.exiting();

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    public void onStart(ITestContext context) {

//        // logger.entering(new Object[] { context });

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

        if (!bInitConfig) {

            bInitConfig = true;

            File outFile = new File(context.getOutputDirectory());
            outputDirectory = outFile.getParent() + File.separator + "RuntimeReporter";
//    //        // logger.info("Runtime Report : " + outputDirectory + File.separator + "index.html");
//    //        // logger.info("This report is best viewed in Internet Explorer, Firefox & Safari");
            RuntimeReportResourceManager resourceMgr = new RuntimeReportResourceManager();
            resourceMgr.copyResources(outFile.getParent());

            ReportDataGenerator.fetchReportData();

            Element configElement = helper.createElement("Config");
            helper.getDocument().getDocumentElement().appendChild(configElement);

            Element currentDate = helper.createElement("Current_Date");
            currentDate.setTextContent(ReportDataGenerator.currentDate);
            configElement.appendChild(currentDate);

            Element stage = helper.createElement("Stage");
            stage.setTextContent(ReportDataGenerator.stage);
            configElement.appendChild(stage);

            Element browser = helper.createElement("Browser");
            browser.setTextContent(ReportDataGenerator.browser);
            configElement.appendChild(browser);

            Element userName = helper.createElement("User_Name");
            userName.setTextContent(ReportDataGenerator.userName);
            configElement.appendChild(userName);

            Element releaseVersion = helper.createElement("Release_Version");
            releaseVersion.setTextContent(ReportDataGenerator.releaseVersion);
            configElement.appendChild(releaseVersion);

            Element buildId = helper.createElement("Build_ID");
            buildId.setTextContent(ReportDataGenerator.buildId);
            configElement.appendChild(buildId);

            Element streamName = helper.createElement("Stream_Name");
            streamName.setTextContent(ReportDataGenerator.streamName);
            configElement.appendChild(streamName);

            Element dbVersion = helper.createElement("DB_Version");
            dbVersion.setTextContent(ReportDataGenerator.dbVersion);
            configElement.appendChild(dbVersion);

        }

//        // logger.exiting();

    }

    public void onFinish(ITestContext context) {

    }

    public void onConfigurationSuccess(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });

        updateConfigDetails(result);

//        // logger.exiting();

    }

    public void onConfigurationFailure(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });

        updateConfigDetails(result);

//        // logger.exiting();

    }

    public void onConfigurationSkip(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });
        updateConfigDetails(result);
//        // logger.exiting();

    }

    public void beforeConfiguration(ITestResult result) {

        if (!ServiceLoaderManager.executeCurrentMethod(this)) {
//    //        // logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
            return;
        }

//        // logger.entering(new Object[] { result });

        updateConfigDetails(result);

//        // logger.exiting();

    }

    public void updateConfigDetails(ITestResult result) {

//        // logger.entering(new Object[] { result });

        List<String> groups = Arrays.asList(result.getMethod().getGroups());
        if (result.getTestContext().getIncludedGroups() == null || result.getTestContext().getIncludedGroups().length==0) {
            helper.insertConfigMethod(result.getTestContext().getSuite().getName(), result.getTestContext()
                    .getCurrentXmlTest().getName(), null, result.getTestClass().getName(), result);

        } else {

            for (String tempGroup : groups) {
                if (result.getTestContext().getIncludedGroups() != null
                        && Arrays.asList(result.getTestContext().getIncludedGroups()).contains(tempGroup)) {
                    helper.insertConfigMethod(result.getTestContext().getSuite().getName(), result.getTestContext()
                            .getCurrentXmlTest().getName(), tempGroup, result.getTestClass().getName(), result);
                }
            }

        }

        helper.writeXML(outputDirectory);
//        // logger.exiting();

    }

}
