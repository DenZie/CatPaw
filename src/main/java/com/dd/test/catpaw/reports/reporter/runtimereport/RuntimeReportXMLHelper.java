package com.dd.test.catpaw.reports.reporter.runtimereport;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import com.dd.test.catpaw.reports.model.WebLog;


/**
 * RuntimeReportXMLHelper will provide methods to create xml documents which
 * contains information about list of test methods and configuration methods
 * executed and there corresponding status.
 * 
 */
public class RuntimeReportXMLHelper {
    private volatile Document doc;
    private int id = 0; // used to create unique id for each xml element.

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    public RuntimeReportXMLHelper(Document doc) {
        this.doc = doc;
    }

    public Document getDocument() {
        return doc;
    }

    /**
     * Used to create xml element with the given name.
     * 
     * @param name
     *            - name of the xml element to create
     * @return the created xml element.
     */
    public Element createElement(String name) {

//        // logger.entering(new Object[] { name });

        id = id + 1;
        Element e = doc.createElement(name);

        e.setAttribute("id", String.valueOf(id));
//        // logger.exiting(e);
        return e;
    }

    /**
     * Creates an element and add it as child to a parent element with tag as
     * type
     * 
     * @param parent
     *            - parent Element.
     * @param type
     *            - tag name of the element to be created
     * @param name
     *            - value of name attribute of the element created.
     * @return the newly created element.
     */
    public Element addElement(Element parent, String type, String name) {

//        // logger.entering(new Object[] { parent, type, name });

        Element clas = createElement(type);
        clas.setAttribute("name", name);
        parent.appendChild(clas);
//        // logger.exiting(clas);
        return clas;

    }

    public Element addSuite(Element parent, String suiteName) {

        return addElement(parent, "Suite", suiteName);
    }

    public Element addTest(Element parent, String testName) {
        return addElement(parent, "Test", testName);
    }

    public Element addClass(Element parent, String className) {

        return addElement(parent, "Class", className);
    }

    public Element addGroup(Element parent, String groupName) {

        return addElement(parent, "Group", groupName);
    }

    /**
     * Used to get element of tag "type" from the parent element.
     * 
     * @param parent
     *            - parent element whose descendants are searched.
     * @param type
     *            - tag name of the element to be searched (Suite, Test, Group
     *            or Class).
     * @param name
     *            - value for name attribute of the element
     * @return the element with tag name type or null
     */
    public Element getElement(Element parent, String type, String name) {
//        // logger.entering(new Object[] { parent, type, name });

        NodeList list = parent.getElementsByTagName(type);
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            if (temp.getAttribute("name").equals(name)) {
    //    //        // logger.exiting();
                return temp;
            }
        }
//        // logger.exiting();
        return null;
    }

    public Element getSuiteElement(Element parent, String suiteName) {

        return getElement(parent, "Suite", suiteName);
    }

    public Element getTestElement(Element parent, String testName) {

        return getElement(parent, "Test", testName);
    }

    public Element getGroupElement(Element parent, String groupName) {

        return getElement(parent, "Group", groupName);
    }

    public Element getClassElement(Element parent, String className) {

        return getElement(parent, "Class", className);
    }

    /**
     * Update the summary like number of testcase passed, number of testcase
     * failed, number of testcase skipped and number of testcase running in the
     * suite element, test element , group element and class element.
     * 
     * @param e
     *            - Suite element.
     */
    public void updateSummary(Element e) {

//        // logger.entering(new Object[] { e });

        updateTestMethodSummary(e);

        NodeList list = e.getElementsByTagName("Test");
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            updateTestMethodSummary(temp);
        }
        list = e.getElementsByTagName("Group");
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            updateTestMethodSummary(temp);
        }
        list = e.getElementsByTagName("Class");
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            updateTestMethodSummary(temp);
        }

//        // logger.exiting();

    }

    /**
     * Update the summary like number of testcase passed, number of testcase
     * failed, number of testcase skipped and number of testcase running in the
     * suite element or test element or group element or class element.
     * 
     * @param e
     *            -Suite Element or Test Element or Group Element or Class
     *            Element.
     */
    public void updateTestMethodSummary(Element e) {

//        // logger.entering(new Object[] { e });

        NodeList list = e.getElementsByTagName("Test_Method");
        int passed = 0, failed = 0, skipped = 0, running = 0;
        for (int i = 0; i < list.getLength(); i++) {
            Element temp = (Element) list.item(i);
            if (temp.getAttribute("Status").equals("Passed")) {
                passed = passed + 1;

            } else if (temp.getAttribute("Status").equals("Failed")) {
                failed = failed + 1;

            } else if (temp.getAttribute("Status").equals("Skipped")) {
                skipped = skipped + 1;
            } else if (temp.getAttribute("Status").equals("Running")) {
                running = running + 1;
            }
        }

        e.setAttribute("Total", String.valueOf(list.getLength()));
        e.setAttribute("Passed", String.valueOf(passed));
        e.setAttribute("Failed", String.valueOf(failed));
        e.setAttribute("Skipped", String.valueOf(skipped));
        e.setAttribute("Running", String.valueOf(running));

//        // logger.exiting();

    }

    /**
     * Creates a "Test_Method" element and adds it to the parent element. Adds
     * Test status (Passed or Failed or Skipped or Running), Start time, end
     * time, parameters, description , exception and logs to the "Test_Method"
     * elements as attributes.
     * 
     * @param parent
     *            Parent element for the "Test_Method" element.
     * @param result
     *            ITestResult of the test method.
     */
    public void addMethodDetails(Element parent, ITestResult result) {

//        // logger.entering(new Object[] { parent, result });

        NodeList list = parent.getChildNodes();
        Element testMethod = null;
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                Element temp = (Element) list.item(i);
                if (temp.getUserData("report") != null && temp.getUserData("report") == result) {
                    testMethod = temp;
                }
            }
        }
        if (testMethod == null) {
            testMethod = createElement("Test_Method");
        }

        testMethod.setAttribute("name", result.getName());
        testMethod.setAttribute("methodname", result.getMethod().getMethodName());

        if (result.getStatus() == ITestResult.SUCCESS) {
            testMethod.setAttribute("Status", "Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            testMethod.setAttribute("Status", "Failed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            testMethod.setAttribute("Status", "Skipped");
        } else if (result.getStatus() == ITestResult.STARTED) {
            testMethod.setAttribute("Status", "Running");
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(result.getStartMillis());
        testMethod.setAttribute("Start_Time", formatter.format(c.getTime()));

        Calendar endtime = Calendar.getInstance();

        endtime.setTimeInMillis(result.getEndMillis());
        testMethod.setAttribute("End_Time", formatter.format(endtime.getTime()));

        String param = "";
        for (Object temp : result.getParameters()) {
            param = param + temp.toString() + " ";
        }
        testMethod.setAttribute("Parameters", param);

        if (result.getMethod().getDescription() != null) {
            testMethod.setAttribute("Description", result.getMethod().getDescription());
        } else {
            testMethod.setAttribute("Description", "");
        }
        addExceptionDetails(testMethod, result);
        addLogDetails(testMethod, result);
        testMethod.setUserData("report", result, null);

        parent.appendChild(testMethod);

//        // logger.exiting();

    }

    /**
     * Creates a "Config" element and adds it to the parent element. Adds status
     * (Passed or Failed or Skipped or Running), Start time, end time,
     * parameters, description , exception and logs to the "Config" elements as
     * attributes.
     * 
     * @param parent
     *            Parent element for the "Config" element.
     * @param result
     *            ITestResult of the config method.
     */
    public void addConfigDetails(Element parent, ITestResult result, String type) {

//        // logger.entering(new Object[] { parent, result, type });

        NodeList list = parent.getChildNodes();
        Element testMethod = null;
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                Element temp = (Element) list.item(i);
                if (temp.getUserData("report") != null && temp.getUserData("report") == result) {
                    testMethod = temp;
                }
            }
        }
        if (testMethod == null) {
            testMethod = createElement("Config");
        }

        testMethod.setAttribute("name", result.getName());
        testMethod.setAttribute("methodname", result.getMethod().getMethodName());
        testMethod.setAttribute("classname", result.getClass().getName());

        testMethod.setAttribute("type", type);
        if (result.getStatus() == ITestResult.SUCCESS) {
            testMethod.setAttribute("Status", "Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            testMethod.setAttribute("Status", "Failed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            testMethod.setAttribute("Status", "Skipped");
        } else if (result.getStatus() == ITestResult.STARTED) {
            testMethod.setAttribute("Status", "Running");
        }

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(result.getStartMillis());
        testMethod.setAttribute("Start_Time", formatter.format(c.getTime()));

        Calendar endtime = Calendar.getInstance();

        endtime.setTimeInMillis(result.getEndMillis());

        testMethod.setAttribute("End_Time", formatter.format(endtime.getTime()));

        DateFormat resultFormatter = new SimpleDateFormat("hh:mm:ss");

        testMethod.setAttribute("Duration", resultFormatter.format(result.getStartMillis() - result.getEndMillis()));
        String param = "";
        for (Object temp : result.getParameters()) {
            param = param + temp.toString() + " ";
        }
        testMethod.setAttribute("Parameters", param);

        if (result.getMethod().getDescription() != null) {
            testMethod.setAttribute("Description", result.getMethod().getDescription());
        } else {
            testMethod.setAttribute("Description", "");
        }

        addExceptionDetails(testMethod, result);
        addLogDetails(testMethod, result);

        testMethod.setUserData("report", result, null);

        parent.appendChild(testMethod);
//        // logger.exiting();

    }

    /**
     * Adds log details such as log message, screenshot file path and source
     * file path to the "Test Method" or "Config" element.
     * 
     * @param parent
     *            - Test_Method or Config element.
     * @param result
     *            - ITestResult instance of Test method or Config method.
     */
    public void addLogDetails(Element parent, ITestResult result) {

//        // logger.entering(new Object[] { parent, result });
        String oneDirUp = "../";

        Element logs = getLogs(parent);
        if (logs == null) {
            logs = createElement("Logs");
        }

        for (String temp : Reporter.getOutput(result)) {
            WebLog logLine = new WebLog(temp);
            Element log = createElement("Log");

            if (logLine.getMsg() != null || !logLine.getMsg().isEmpty()) {
                log.appendChild(doc.createCDATASection(logLine.getMsg()));

            }

            if (logLine.getScreen() != null && !logLine.getScreen().isEmpty()) {
                log.setAttribute("screenshot", oneDirUp + logLine.getScreen());
            }

            if (logLine.getHref() != null && !logLine.getHref().isEmpty()) {
                log.setAttribute("source", oneDirUp + logLine.getHref());
            }
            logs.appendChild(log);
        }
        parent.appendChild(logs);
//        // logger.exiting();

    }

    /**
     * Finds and return elements with tag "Logs"
     * 
     * @param parent
     *            - the parent element whose descendants will be searched
     * @return element with tag name Logs or null
     */
    public Element getLogs(Element parent) {

//        // logger.entering(new Object[] { parent });

        NodeList list = parent.getElementsByTagName("Logs");
        if (list.getLength() > 0) {
//    //        // logger.exiting((Element) list.item(0));
            return (Element) list.item(0);
        }
//        // logger.exiting();
        return null;
    }

    /**
     * Used to return StackTrace of an Exception as String.
     * 
     * @param aThrowable
     * @return StackTrace as String
     */
    public String getStackTraceInfo(Throwable aThrowable) {

//        // logger.entering(new Object[] { aThrowable });

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
//        // logger.exiting(result.toString());
        return result.toString();
    }

    /**
     * This method adds exception details like exception type and corresponding
     * stack trace information to the "Test_Method" or "Config" element.
     * 
     * @param parent
     *            - Test_Method or Config element.
     * @param result
     *            - ITestResult of the Test method or Config method.
     */
    public void addExceptionDetails(Element parent, ITestResult result) {

//        // logger.entering(new Object[] { parent, result });

        Element exception = getExceptionElement(parent);
        Element stack = getStackTrace(parent);
        if (exception == null && stack == null) {
            exception = createElement("Exception");
            stack = createElement("StackTrace");
        }

        if (result.getThrowable() != null) {

            exception.appendChild(doc.createCDATASection(result.getThrowable().toString()));

            String[] out = getStackTraceInfo(result.getThrowable()).split("\n");
            for (String temp : out) {
                Element trace = createElement("Trace");
                trace.appendChild(doc.createCDATASection(temp));
                stack.appendChild(trace);
            }

        }
        parent.appendChild(exception);
        parent.appendChild(stack);

//        // logger.exiting();

    }

    /**
     * Finds and return element with tag "Exception".
     * 
     * @param parent
     *            - the parent element whose descendants will be searched.
     * @return element with tag name Exception or null
     */
    public Element getExceptionElement(Element parent) {

//        // logger.entering(new Object[] { parent });

        NodeList list = parent.getElementsByTagName("Exception");
        if (list.getLength() > 0) {
//    //        // logger.exiting((Element) list.item(0));
            return (Element) list.item(0);
        }
//        // logger.exiting();
        return null;
    }

    /**
     * Finds and return element with tag "StackTrace".
     * 
     * @param parent
     *            - the parent element whose descendants will be searched.
     * @return element with tag name StackTrace or null.
     */
    public Element getStackTrace(Element parent) {
//        // logger.entering(parent);

        NodeList list = parent.getElementsByTagName("StackTrace");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
//    //        // logger.exiting(e);
            return e;
        }
//        // logger.exiting();
        return null;
    }

    /**
     * This method is used to insert test method details in the xml document
     * based on the methods suite, test, groups and class name.
     * 
     * @param suite
     *            - suite name of the test method.
     * @param test
     *            - test name of the test method.
     * @param groups
     *            - group name of the test method. If the test method doesn't
     *            belong to any group then we should pass null.
     * @param classname
     *            - class name of the test method.
     * @param result
     *            - ITestResult instance of the test method.
     */
    public synchronized void insertTestMethod(String suite, String test, String groups, String classname,
            ITestResult result) {

//        // logger.entering(new Object[] { suite, test, groups, classname, result });
        Element suiteElement = getSuiteElement(doc.getDocumentElement(), suite);
        if (suiteElement != null) {
            Element testElement = getTestElement(suiteElement, test);
            if (testElement != null) {
                if (groups != null) {

                    Element groupElement = getGroupElement(testElement, groups);
                    if (groupElement != null) {
                        Element classElement = getClassElement(groupElement, classname);
                        if (classElement != null) {
                            addMethodDetails(classElement, result);

                        } else {
                            addMethodDetails(addClass(groupElement, classname), result);
                        }
                    } else {
                        addMethodDetails(addClass(addGroup(testElement, groups), classname), result);
                    }

                } else {
                    Element classElement = getClassElement(testElement, classname);
                    if (classElement != null) {
                        addMethodDetails(classElement, result);

                    } else {
                        addMethodDetails(addClass(testElement, classname), result);
                    }

                }

            } else {
                Element e = addTest(suiteElement, test);
                if (groups == null) {

                    addMethodDetails(addClass(addGroup(e, groups), classname), result);
                } else {
                    addMethodDetails(addClass(e, classname), result);
                }
            }
        } else {
            Element e = addTest(addSuite(doc.getDocumentElement(), suite), test);
            if (groups == null) {
                addMethodDetails(addClass(e, classname), result);

            } else {

                addMethodDetails(addClass(addGroup(e, groups), classname), result);
            }

        }
        updateSummary(getSuiteElement(doc.getDocumentElement(), suite));

//        // logger.exiting();

    }

    /**
     * This method is used to insert configuration method details in the xml
     * document based on the suite, test, groups and class name.
     * 
     * @param suite
     *            - suite name of the configuration method.
     * @param test
     *            - test name of the configuration method.
     * @param groups
     *            - group name of the configuration method. If the configuration
     *            method doesn't belong to any group then we should pass null.
     * @param classname
     *            - class name of the configuration method.
     * @param result
     *            - ITestResult instance of the configuration method.
     */
    public synchronized void insertConfigMethod(String suite, String test, String groups, String classname,
            ITestResult result) {
//        // logger.entering(new Object[] { suite, test, groups, classname, result });

        String type = null;
        if (result.getMethod().isBeforeSuiteConfiguration()) {
            type = "BeforeSuite";
        } else if (result.getMethod().isBeforeTestConfiguration()) {
            type = "BeforeTest";
        } else if (result.getMethod().isBeforeGroupsConfiguration()) {
            type = "BeforeGroup";
        } else if (result.getMethod().isBeforeClassConfiguration()) {
            type = "BeforeClass";
        } else if (result.getMethod().isBeforeMethodConfiguration()) {
            type = "BeforeMethod";
        } else if (result.getMethod().isAfterSuiteConfiguration()) {
            type = "AfterSuite";
        } else if (result.getMethod().isAfterTestConfiguration()) {
            type = "AfterTest";
        } else if (result.getMethod().isAfterGroupsConfiguration()) {
            type = "AfterGroup";
        } else if (result.getMethod().isAfterClassConfiguration()) {
            type = "AfterClass";
        } else if (result.getMethod().isAfterMethodConfiguration()) {
            type = "AfterMethod";
        }

        if (type.equals("BeforeSuite") || type.equals("AfterSuite")) {
            Element suiteElement = getSuiteElement(doc.getDocumentElement(), suite);
            if (suiteElement == null) {
                addSuite(doc.getDocumentElement(), suite);
            }
            addConfigDetails(suiteElement, result, type);
        } else if (type.equals("BeforeTest") || type.equals("AfterTest")) {
            Element suiteElement = getSuiteElement(doc.getDocumentElement(), suite);
            Element testElement = null;
            if (suiteElement == null) {

                testElement = addTest(addSuite(doc.getDocumentElement(), suite), test);
            } else {
                testElement = getTestElement(suiteElement, test);
                if (testElement == null) {
                    testElement = addTest(suiteElement, test);
                }
            }

            addConfigDetails(testElement, result, type);

        } else if (type.equals("BeforeGroup") || type.equals("AfterGroup")) {
            Element suiteElement = getSuiteElement(doc.getDocumentElement(), suite);
            Element testElement = null;
            Element groupElement = null;
            if (suiteElement == null) {
                groupElement = addGroup(addTest(addSuite(doc.getDocumentElement(), suite), test), groups);
            } else {
                testElement = getTestElement(suiteElement, test);
                if (testElement == null) {
                    groupElement = addGroup(addTest(suiteElement, test), groups);
                } else {
                    groupElement = getGroupElement(testElement, groups);
                    if (groupElement == null) {
                        groupElement = addGroup(testElement, groups);
                    }
                }
            }

            addConfigDetails(groupElement, result, type);

        } else if (type.equals("BeforeClass") || type.equals("AfterClass") || type.equals("BeforeMethod")
                || type.equals("AfterMethod")) {
            Element suiteElement = getSuiteElement(doc.getDocumentElement(), suite);
            Element testElement = null;
            Element groupElement = null;
            Element classElement = null;
            if (suiteElement == null) {
                if (groups == null) {
                    classElement = addClass(addTest(addSuite(doc.getDocumentElement(), suite), test), classname);
                } else {
                    classElement = addClass(addGroup(addTest(addSuite(doc.getDocumentElement(), suite), test), groups),
                            classname);
                }

            } else {
                testElement = getTestElement(suiteElement, test);
                if (testElement == null) {
                    if (groups == null) {
                        classElement = addClass(addTest(suiteElement, test), classname);
                    } else {
                        classElement = addClass(addGroup(addTest(suiteElement, test), groups), classname);
                    }

                } else {
                    if (groups == null) {
                        classElement = getClassElement(testElement, classname);
                        if (classElement == null) {
                            classElement = addClass(testElement, classname);
                        }

                    } else {
                        groupElement = getGroupElement(testElement, groups);
                        if (groupElement == null) {
                            groupElement = addGroup(testElement, groups);
                        }
                        classElement = getClassElement(groupElement, classname);
                        if (classElement == null) {
                            classElement = addClass(testElement, classname);
                        }

                    }

                }
            }

            addConfigDetails(classElement, result, type);

        }
//        // logger.exiting();

    }

    public void writeXML(String outputDirectory) {
//        // logger.entering(new Object[] { outputDirectory });
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);

            File outFile = new File(outputDirectory + File.separator + "ReporterData.xml");

            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            StreamResult result = new StreamResult(outFile);

            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
//    //        // logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (TransformerException e) {
//    //        // logger.log(Level.SEVERE, e.getMessage(), e);
        }

//        // logger.exiting();

    }

}
