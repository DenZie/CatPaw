package com.dd.test.catpaw.reports.reporter.html;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;


import com.dd.test.catpaw.reports.reporter.html.filter.BlankFilter;
import com.dd.test.catpaw.reports.reporter.html.filter.EnvironementFilter;
import com.dd.test.catpaw.reports.reporter.html.filter.Filter;
import com.dd.test.catpaw.reports.reporter.html.splitters.ByMethodSplitter;
import com.dd.test.catpaw.reports.reporter.html.splitters.CollectionSplitter;


/**
 * view that takes a list of result, and group then based on an OrgType, and
 * display the result in a table, 1 line for all the test that match a given
 * criteria
 * 
 */
public class GroupingView implements View {

	private List<ISuite> suites;
	private String id;
	private VelocityEngine ve;
	private String title;
	private String description;
	private CollectionSplitter splitter;
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	// filter for the stuff in bracker in the report
	private Filter detailFilter = new EnvironementFilter();

	public GroupingView(String id, String title, String description,
			VelocityEngine ve, List<ISuite> suites, CollectionSplitter splitter) {
		this(id, title, description, ve, suites, splitter, new BlankFilter());
	}

	public GroupingView(String id, String title, String description,
			VelocityEngine ve, List<ISuite> suites,
			CollectionSplitter splitter, Filter filter) {
		this.id = id;
		this.ve = ve;
		this.title = title;
		this.description = description;
		this.suites = suites;
		this.splitter = splitter;
		this.splitter.setFilter(filter);
	}


	public String getId() {
		return id;
	}


	public String getTitle() {
		return title;
	}


	public String getContent() {
		// logger.entering();
		try {
			Template t = ve
					.getTemplate("/templates/ManagerViewTable.part.html");

			StringWriter writer = new StringWriter();
			VelocityContext context = new VelocityContext();

			context.put("title", title);
			context.put("description", description);
			context.put("view", this);

			for (ISuite suite : suites) {
				context.put("suiteName", suite.getName());
				splitter.setSuite(suite);
				splitter.organize();
				context.put("lines", splitter.getLines().values());
			}

			t.merge(context, writer);
			//Not logging the return value
			// logger.exiting();
			return writer.toString();

		} catch (Throwable e) {
			// logger.log(Level.SEVERE, e.getMessage(), e);
			String returnValue = "Error generating manager view " + e.getMessage();
			// logger.exiting(returnValue);
			return returnValue;
		}
	}

	public CollectionSplitter getSplitter() {
		return splitter;
	}

	public int getTotal(String key, Map<String, List<ITestResult>> collection,
			boolean useCurrentFilter) {
		// logger.entering(new Object[]{key, collection, useCurrentFilter});
		Collection<ITestResult> results;
		if (key == null) {
			results = new ArrayList<ITestResult>();
			for (List<ITestResult> lr : collection.values()) {
				results.addAll(lr);
			}
		} else {
			results = collection.get(key);
		}

		if (results == null) {
			// logger.exiting(0);
			return 0;
		} else {
			int cpt = 0;
			for (ITestResult result : results) {
				if (useCurrentFilter == detailFilter.isValid(result)) {
					cpt++;
				}
			}
			// logger.exiting(cpt);
			return cpt;
		}
	}

	public int getTotal(Map<String, List<ITestResult>> collection,
			boolean useCurrentFilter) {
		return getTotal(null, collection, useCurrentFilter);
	}


	public void setData(List<ISuite> suites) {
		this.suites = suites;
	}

	public String getId(String key) {
		// logger.entering(key);
		String returnValue = "Not Implemented";
		if (splitter instanceof ByMethodSplitter) {
			ByMethodSplitter s = (ByMethodSplitter) splitter;
			ITestNGMethod method = s.getAssociatedMethod(key);
			returnValue = method.getId();
		} 
		// logger.exiting(returnValue);
		return returnValue;
	}
}
