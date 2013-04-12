package com.dd.test.catpaw.reports.reporter.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;


import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
//import com.dd.test.jaws.execution.Execution;
//import com.dd.test.jaws.execution.Execution.ExecutionOutput;
//import com.dd.test.jaws.execution.ExecutionFailedException;
//import com.dd.test.jaws.execution.ExecutionTimedOutException;


public class ReportDataGenerator {

	private static boolean isReportInitialized = false;
	private static final String NOTAVAILABLE = "Info not available";
	private static final String releaseKey = "release_product_number";
	private static final String buildIdKey = "release_build_number";
	private static final String streamNameKey = "release_branch_name";
	public static String currentDate = null;
	public static String userName = null;
	public static String buildId = NOTAVAILABLE;
	public static String releaseVersion = NOTAVAILABLE;
	public static String streamName = NOTAVAILABLE;
	public static String dbVersion = NOTAVAILABLE;
	public static String browser = null;
	public static String stage = null;

//	private static SimpleLogger logger = null;

	static {
//		logger = CatPawLogger.getLogger();
		browser = CatPawConfig.getConfigProperty(CatPawConfigProperty.BROWSER).substring(1);
		stage = CatPawConfig.getConfigProperty(CatPawConfigProperty.STAGE_NAME);
	}

	/**
	 * init the uniques id for the methods , needed to create the navigation.
	 * 
	 * @param suites
	 */
	public static void initReportData(List<ISuite> suites) {
		// logger.entering(suites);
		if (!isReportInitialized) {
			for (ISuite suite : suites) {
				Map<String, ISuiteResult> r = suite.getResults();
				for (ISuiteResult r2 : r.values()) {
					ITestContext tc = r2.getTestContext();
					ITestNGMethod[] methods = tc.getAllTestMethods();
					for (ITestNGMethod method : methods) {
						method.setId(UUID.randomUUID().toString());
					}
				}
			}
			fetchReportData();
			isReportInitialized = true;
		}
		// logger.exiting();
	}

	/**
	 * Method to fetch the test execution environment data.
	 */
	public static void fetchReportData() {

		// logger.entering();

		currentDate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date());
		userName = CatPawConfig.getConfigProperty(CatPawConfigProperty.SSH_USER);

		if (stage == null) {
			stage = NOTAVAILABLE;
		} else {
//			if (stage.startsWith("STAGE")) {
//				String getBuildInfocommand = "cat /x/web/" + stage + "/web/paypal.com/htdocs/version.txt";
//
//				try {
//					ExecutionOutput output = Execution.executeCommandOnStage(getBuildInfocommand);
//					Execution.verifyExecution(output);
//
//					List<String> buildInfo = output.getStdOut();
//					if (buildInfo.size() >= 3) {
//						for (String s : buildInfo) {
//							if (s.contains(releaseKey)) {
//								releaseVersion = s.replace(releaseKey + "=", "");
//							}
//							if (s.contains(buildIdKey)) {
//								buildId = s.replace(buildIdKey + "=", "");
//							}
//							if (s.contains(streamNameKey)) {
//								streamName = s.replace(streamNameKey + "=", "");
//							}
//						}
//					}
//				} catch (ExecutionTimedOutException e) {
//					// do nothing
//				} catch (ExecutionFailedException e) {
//					// do nothing
//				}
//				dbVersion = getStageDBVersion(stage);
//			}
		}
		// logger.exiting();
	}

	/**
	 * Provides the latest DB clone Version for the stage.
	 * 
	 * @param stageName
	 *            - Stage Name for which the DB version information is required
	 * @return dbVersion - The latest DB clone Version for the stage
	 */
	private static String getStageDBVersion(String stageName) {

		// logger.entering(stageName);
		String dbVersion = NOTAVAILABLE;
		try {
			URL url = new URL("http://gws.corp.ebay.com/SupportServices/CheckExclusive.asmx/GetStageDBVersion");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			String data = URLEncoder.encode("stageName", "UTF-8") + "=" + URLEncoder.encode(stageName, "UTF-8");
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				Pattern pattern = Pattern.compile(">(.*?)<");
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					dbVersion = matcher.group(1);
				}
			}
			wr.close();
			rd.close();
		} catch (IOException e) {
			// logger.log(Level.WARNING, "An error occured while trying to retrive the stage DB Version. Error message: "
//					+ e.getMessage(), e);
		}
		// logger.exiting(dbVersion);
		return dbVersion;
	}
}
