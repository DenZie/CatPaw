package com.dd.test.catpaw.reports.reporter.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

import com.dd.test.catpaw.annotations.NeedsAttention;

import com.dd.test.catpaw.platform.config.CatPawConfig;
import com.dd.test.catpaw.platform.config.CatPawConfig.CatPawConfigProperty;
import com.dd.test.catpaw.platform.config.ServiceLoaderManager;
import com.dd.test.catpaw.reports.model.WebLog;
import com.dd.test.catpaw.reports.reporter.html.filter.Filter;
import com.dd.test.catpaw.reports.reporter.html.filter.StateFilter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByClassSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByGroupSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByMethodSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByPackageSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByTestCaseSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.Line;


public class HtmlReporterListener implements IReporter{

	private PrintWriter out;
	private VelocityEngine ve;
	private String outputDir;
	private Map<Integer, String> statusMap = new HashMap<Integer, String>();
	String qaMachine;


	public HtmlReporterListener() {
		
		ve = new VelocityEngine();

		ve.setProperty("resource.loader", "class");
		ve.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		try {
			ve.init();
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			// logger.log(Level.SEVERE, "Velocity engine initialization failed. " + re.getMessage(), re);
			throw re;
		}
		statusMap.put(ITestResult.SUCCESS, "passed");
		statusMap.put(ITestResult.FAILURE, "failed");
		statusMap.put(ITestResult.SKIP, "skipped");
	}

	@NeedsAttention("Address the TODO items noted in this method.")
	public void generateReport(List<XmlSuite> xmlSuite, List<ISuite> suites, String outputDir) {
		// logger.entering(new Object[] { xmlSuite, suites, outputDir });
		if (ServiceLoaderManager.executeCurrentMethod(this) == false) {
			// logger.exiting(ServiceLoaderManager.THREAD_EXCLUSION_MSG);
			return;
		}

		qaMachine = "NA";
		// TODO horrible.
		String m = suites.get(0).getParameter("stage");
		if (m != null && !"".equals(m)) {
			qaMachine = m;
		}
		m = suites.get(0).getParameter("pool");
		if (m != null && !"".equals(m)) {
			qaMachine = m;
		}
		this.outputDir = outputDir;
		ReportDataGenerator.initReportData(suites);

		out = createWriter(outputDir);
		startHtml(out);

	
		if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.CAL_LOG)) {
			CatPawConfig.getConfigProperty(CatPawConfigProperty.STAGE_NAME);
			// TODO :: getCalLog returns a list of test cases which CAL info could
			// not be acquired for. We should do possibly do something with this list??
//			TestCaseCalData.getCalLog(suites);
		}

		List<Line> lines = createSummary(suites);
		createDetail(lines);
		createMethodContent(suites, outputDir);

		endHtml(out);

		out.flush();
		out.close();
		copyResources(outputDir);
		// logger.exiting();
	}

	private void copyResources(String outputFolder) {
		Properties resourceListToCopy = new Properties();
		try {
			ClassLoader localClassLoader = this.getClass().getClassLoader();
			// Any new resources being added under src/main/resources/templates folder
			// would need to have an entry in the Resources.properties file
			// so that it can be copied over to the testng output folder.
			resourceListToCopy.load(localClassLoader.getResourceAsStream("Resources.properties"));
			Enumeration<Object> keys = resourceListToCopy.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String fileName = resourceListToCopy.getProperty(key);
				writeStreamToFile(localClassLoader.getResourceAsStream("templates/" + fileName), fileName, outputFolder);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeStreamToFile(InputStream isr, String fileName, String outputFolder) throws IOException {
		if (isr == null) {
			// logger.log(Level.SEVERE, "InputStream was null. Aborting..");
			return;
		}
		File outFile = new File(outputFolder + File.separator + fileName);
		if (!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}
		FileOutputStream outStream = new FileOutputStream(outFile);
		byte[] bytes = new byte[1024];
		int readLength = 0;
		while ((readLength = isr.read(bytes)) != -1) {
			outStream.write(bytes, 0, readLength);
		}
		isr.close();
		outStream.flush();
		outStream.close();
	}

	private void createDetail(List<Line> lines) {
		// logger.entering(lines);
		for (Line line : lines) {
			createContent(line);
		}
		// logger.exiting();
	}

	private void createContent(Line line) {
		// logger.entering(line);
		try {
			File f = new File(outputDir + "/html/", line.getId() + ".html");
//			logger.fine("generating method " + f.getAbsolutePath());
			Writer fileSystemWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "UTF8")));

			Map<ITestNGMethod, List<ITestResult>> resultByMethod = new HashMap<ITestNGMethod, List<ITestResult>>();

			// find all methods
			for (ITestResult result : line.getAssociatedResults()) {
				List<ITestResult> list = resultByMethod.get(result.getMethod());
				if (list == null) {
					list = new ArrayList<ITestResult>();
					resultByMethod.put(result.getMethod(), list);
				}
				list.add(result);
			}

			// for each method, find all the status
			for (ITestNGMethod method : resultByMethod.keySet()) {

				List<ITestResult> passed = new ArrayList<ITestResult>();
				List<ITestResult> failed = new ArrayList<ITestResult>();
				List<ITestResult> skipped = new ArrayList<ITestResult>();
				List<ITestResult> results = resultByMethod.get(method);
				for (ITestResult result : results) {
					switch (result.getStatus()) {
					case ITestResult.SUCCESS:
						passed.add(result);
						break;
					case ITestResult.FAILURE:
						failed.add(result);
						break;
					case ITestResult.SKIP:
						skipped.add(result);
						break;
					default:
						RuntimeException e = new RuntimeException(
								"Implementation exists only for tests with status as : Success, Failure and Skipped");
						// logger.log(Level.SEVERE, e.getMessage(), e);
						throw e;
					}
				}

				// for each status // method, create the html
				if (passed.size() != 0) {
					Template t = ve.getTemplate("/templates/method.part.html");
					VelocityContext context = new VelocityContext();
					context.put("status", "passed");
					context.put("method", passed.get(0).getMethod());
					StringBuffer buff = new StringBuffer();
					for (ITestResult result : passed) {
						buff.append(getContent(result));
					}
					context.put("content", buff.toString());
					StringWriter writer = new StringWriter();
					t.merge(context, writer);
					fileSystemWriter.write(writer.toString());
				}

				if (failed.size() != 0) {
					Template t = ve.getTemplate("/templates/method.part.html");
					VelocityContext context = new VelocityContext();
					context.put("status", "failed");
					context.put("method", failed.get(0).getMethod());
					StringBuffer buff = new StringBuffer();
					for (ITestResult result : failed) {
						buff.append(getContent(result));
					}
					context.put("content", buff.toString());
					StringWriter writer = new StringWriter();
					t.merge(context, writer);
					fileSystemWriter.write(writer.toString());
				}
				if (skipped.size() != 0) {
					Template t = ve.getTemplate("/templates/method.part.html");
					VelocityContext context = new VelocityContext();
					context.put("status", "skipped");
					context.put("method", skipped.get(0).getMethod());
					StringBuffer buff = new StringBuffer();
					for (ITestResult result : skipped) {
						buff.append(getContent(result));
					}
					context.put("content", buff.toString());
					StringWriter writer = new StringWriter();
					t.merge(context, writer);
					fileSystemWriter.write(writer.toString());
				}
			}
			fileSystemWriter.flush();
			fileSystemWriter.close();
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			// logger.log(Level.SEVERE, e.getMessage(), re);
			throw re;
		}
		// logger.exiting();

	}

	private void createMethodContent(List<ISuite> suites, String outdir) {
		// logger.entering(new Object[] { suites, outdir });
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext ctx = r2.getTestContext();
				ITestNGMethod[] methods = ctx.getAllTestMethods();
				for (int i = 0; i < methods.length; i++) {
					createMethod(ctx, methods[i], outdir);
				}
			}
		}
		// logger.exiting();
	}

	private String getContent(ITestResult result) {
		// logger.entering(result);

		StringBuffer contentBuffer = new StringBuffer();
		Boolean retried = (Boolean) result.getAttribute("retried");

		if (retried != null) {
			Integer retriedCount = ((Integer) result.getAttribute("retriedCount")) / 2;
			if (retried) {
				contentBuffer.append("<div style='text-align: center;'><b>This test  will be automatically retried ("
						+ retriedCount + ")</b></div></br>");
			} else {
				contentBuffer.append("<div style='text-align: center;'><b>That was the last try :( (" + retriedCount
						+ ")</b></div></br>");
			}

		}

		contentBuffer.append(String.format("Total duration of this instance run : %02d sec. ",
				(result.getEndMillis() - result.getStartMillis()) / 1000));
		Object[] parameters = result.getParameters();
		boolean hasParameters = parameters != null && parameters.length > 0;
		List<String> msgs = Reporter.getOutput(result);
		boolean hasReporterOutput = msgs.size() > 0;
		Throwable exception = result.getThrowable();
		boolean hasThrowable = exception != null;
		List<String> imgForFilmStrip = new ArrayList<String>();
		if (hasReporterOutput || hasThrowable) {
			if (hasParameters) {
				contentBuffer.append("<h2 class='yuk_grey_midpnl_ltitle'>");
				for (int i = 0; i < parameters.length; i++) {
					Object p = parameters[i];
					String paramAsString = "null";
					if (p != null) {
						paramAsString = p.toString() + "<i>(" + p.getClass().getSimpleName() + ")</i> , ";
					}
					contentBuffer.append(paramAsString);
				}
				contentBuffer.append("</h2>");
			}

			if (hasReporterOutput || hasThrowable) {
				contentBuffer.append("<div class='leftContent' style='float: left; width: 70%;'>");
				contentBuffer.append("<h3>Test Log</h3>");
				// Generate link to CAL if CAL is set to true
				if (CatPawConfig.getBoolConfigProperty(CatPawConfigProperty.CAL_LOG)) {
					if (result.getAttribute("calLink") != null) {
						String calLogResponse = result.getAttribute("calLink").toString();
						String calLogLink = "<b><a href='" + calLogResponse + "' >" + "CAL Log" + "</a></b>";
						contentBuffer.append(calLogLink);
						contentBuffer.append("<br/><br/>");
					}
				}
				for (String line : msgs) {

					WebLog logLine = new WebLog(line);
					if (logLine.getScreen() != null) {
						imgForFilmStrip.add(logLine.getScreenURL());
					}
					String htmllog;
					if (logLine.getHref() != null) {
						htmllog = "<a href='../" + logLine.getHref().replaceAll("\\\\", "/") + "' title='"
								+ logLine.getLocation() + "' >" + logLine.getMsg() + "</a>";
					} else {
						htmllog = logLine.getMsg();
					}
					// Don't output blank message w/o any Href.
					if ((logLine.getHref() != null) || (logLine.getMsg() != null) && !logLine.getMsg().isEmpty()) {
						contentBuffer.append(htmllog);
						contentBuffer.append("<br/>");

					}

				}
				if (hasThrowable) {
					generateExceptionReport(exception, result.getMethod(), contentBuffer);
				}
			}
			contentBuffer.append("</div>"); // end of
			// leftContent

			contentBuffer.append("<div class='filmStripContainer' style='float: right; width: 100%;'>");
			contentBuffer.append("<b>Preview</b>	");
			contentBuffer.append("	<div class=\"filmStrip\">");
			contentBuffer.append("	<ul>");
			for (String imgPath : imgForFilmStrip) {
				contentBuffer.append("<li>");
				contentBuffer.append("<a href=\"../" + imgPath + "\" > <img	src=\"../" + imgPath
						+ "\" width=\"200\" height=\"200\" /> </a>");
				contentBuffer.append("</li>");
			}

			contentBuffer.append("</ul>");

			contentBuffer.append("</div>");
			contentBuffer.append("</div>");

		}

		contentBuffer.append("<div class='clear_both'></div>");
		// Not logging the return value, because it will clog the logs
		// logger.exiting();
		return contentBuffer.toString();
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method, StringBuffer contentBuffer) {
		// logger.entering(new Object[] { exception, method, contentBuffer });
		Throwable fortile = exception;

		String title = fortile.getMessage();
		if (title == null) {
			try {
				title = fortile.getCause().getMessage();
			} catch (Throwable e) {
				title = "pb finding a meaningfull title";
			}
		}
		generateExceptionReport(exception, method, title, contentBuffer);
		// logger.exiting();
	}

	private void generateExceptionReport(Throwable exception, ITestNGMethod method, String title,
			StringBuffer contentBuffer) {
		generateTheStackTrace(exception, method, title, contentBuffer);
	}

	private void generateTheStackTrace(Throwable exception, ITestNGMethod method, String title,
			StringBuffer contentBuffer) {
		// logger.entering(new Object[] { exception, method, title, contentBuffer });
		contentBuffer.append(" <div class='stContainer' >" + exception.getClass() + ":" + title// escape(title)
				+ "<a  class='exceptionlnk'>(+)</a>");

		contentBuffer.append("<div class='exception' style='display:none'>");

		StackTraceElement[] s1 = exception.getStackTrace();
		Throwable t2 = exception.getCause();
		if (t2 == exception) {
			t2 = null;
		}

		for (int x = 0; x < s1.length; x++) {
			contentBuffer.append((x > 0 ? "<br/>at " : "") + escape(s1[x].toString()));
		}

		if (t2 != null) {
			generateExceptionReport(t2, method, "Caused by " + t2.getLocalizedMessage(), contentBuffer);
		}
		contentBuffer.append("</div></div>");
		// logger.exiting();
	}

	private static String escape(String string) {
		if (null == string)
			return string;
		return string.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	private void createMethod(ITestContext ctx, ITestNGMethod method, String outdir) {
		// logger.entering(new Object[] { ctx, method, outdir });
		try {
			File f = new File(outdir + "/html/", method.getId() + ".html");
//			logger.fine("generating method " + f.getAbsolutePath());
			Writer fileSystemWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f), "UTF8")));
			Template t = ve.getTemplate("/templates/method.part.html");

			Set<ITestResult> passed = ctx.getPassedTests().getResults(method);

			for (ITestResult result : passed) {
				VelocityContext context = new VelocityContext();
				context.put("method", method);
				context.put("status", "passed");
				context.put("result", result);
				context.put("content", getContent(result));
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				fileSystemWriter.write(writer.toString());
			}

			Set<ITestResult> failed = ctx.getFailedTests().getResults(method);
			for (ITestResult result : failed) {
				VelocityContext context = new VelocityContext();
				context.put("method", method);
				context.put("status", "failed");
				context.put("result", result);
				context.put("content", getContent(result));
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				fileSystemWriter.write(writer.toString());
			}

			Set<ITestResult> skipped = ctx.getSkippedTests().getResults(method);
			for (ITestResult result : skipped) {
				VelocityContext context = new VelocityContext();
				context.put("method", method);
				context.put("status", "skipped");
				context.put("result", result);
				context.put("content", getContent(result));
				StringWriter writer = new StringWriter();
				t.merge(context, writer);
				fileSystemWriter.write(writer.toString());
			}

			fileSystemWriter.flush();
			fileSystemWriter.close();
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			// logger.log(Level.SEVERE, e.getMessage(), re);
			throw re;
		}
		// logger.exiting();

	}

	private List<Line> createSummary(List<ISuite> suites) {
		// logger.entering(suites);
		try {

			Template t = ve.getTemplate("/templates/summaryTabs.part.html");
			VelocityContext context = new VelocityContext();

			List<GroupingView> views = new ArrayList<GroupingView>();

			GroupingView view = new GroupingView("managerView", "per class", "Overview organized per class", ve,
					suites, new ByClassSplitter());
			views.add(view);

			GroupingView view2 = new GroupingView("managerView2", "per package", "Overview organized per package", ve,
					suites, new ByPackageSplitter());
			views.add(view2);

			GroupingView view3 = new GroupingView("managerView3", "per method", "Overview organized per method", ve,
					suites, new ByMethodSplitter());
			views.add(view3);
			GroupingView view9 = new GroupingView("managerView9", "per Test Case", "Overview organized per Test Case",
					ve, suites, new ByTestCaseSplitter());
			views.add(view9);
			/*********************************/

			Filter f2 = new StateFilter(ITestResult.FAILURE);
			GroupingView view6 = new GroupingView("managerView6", "failed methods only",
					"Overview organized per failed methods", ve, suites, new ByMethodSplitter(), f2);
			views.add(view6);

			List<String> areas = new ArrayList<String>();
			areas.add("Seller Registration");
			areas.add("BUYER_REG");
			GroupingView view7 = new GroupingView("managerView7", "per group ( area )", "Overview organized per group",
					ve, suites, new ByGroupSplitter(areas));
			views.add(view7);

			context.put("views", views);

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			out.write(writer.toString());
			List<Line> lines = new ArrayList<Line>();
			for (GroupingView v : views) {
				for (Line line : v.getSplitter().getLines().values()) {
					lines.add(line);
				}
			}
			// logger.exiting(lines);
			return lines;

		} catch (Exception e) {
			RuntimeException re = new RuntimeException("Error occurred while generating report summary");
			// logger.log(Level.SEVERE, re.getMessage(), re);
			throw re;
		}
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		// logger.entering(out);
		try {

			Template t = ve.getTemplate("/templates/header.part.html");
			VelocityContext context = new VelocityContext();

			context.put("date", ReportDataGenerator.currentDate);
			context.put("stage", ReportDataGenerator.stage);
			context.put("browser", ReportDataGenerator.browser);
			context.put("userId", ReportDataGenerator.userName);
			context.put("releaseVersion", ReportDataGenerator.releaseVersion);
			context.put("buildId", ReportDataGenerator.buildId);
			context.put("streamName", ReportDataGenerator.streamName);
			context.put("dbVersion", ReportDataGenerator.dbVersion);

			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			out.write(writer.toString());

		} catch (Exception e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
		}
		// logger.exiting();

	}

	private void endHtml(PrintWriter out) {
		// logger.entering(out);
		try {
			Template t = ve.getTemplate("/templates/footer.part.html");
			VelocityContext context = new VelocityContext();
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			out.write(writer.toString());
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			// logger.log(Level.SEVERE, e.getMessage(), re);
			throw re;
		}
		// logger.exiting();
	}

	protected PrintWriter createWriter(String outdir) {
		// logger.entering(outdir);
		File f = new File(outdir + "/html/", "report.html");
		if (f.exists()) {
			Format formatter = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
			String currentDate = formatter.format(new Date());
			f.renameTo(new File(outdir + "/html/", "report-" + currentDate + ".html"));
		}
		// logger.info("generating report " + f.getAbsolutePath());
		try {
			PrintWriter pw = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF8")));
			// logger.exiting(pw);
			return pw;
		} catch (Exception e) {
			RuntimeException re = new RuntimeException(e);
			// logger.log(Level.SEVERE, e.getMessage(), re);
			throw re;
		}
	}
	
}
