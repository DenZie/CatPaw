package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;


import com.dd.test.catpaw.reports.reporter.html.filter.Filter;


public abstract class CollectionSplitter {
	
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	private ISuite suite;
	private Filter filter;
	private Map<String, Line> lineById = new HashMap<String, Line>();
	int totalInstancePassed = 0;
	int totalInstancePassedEnvt = 0;
	int totalInstanceFailed = 0;
	int totalInstanceFailedEnvt = 0;
	int totalInstanceSkipped = 0;
	int totalInstanceSkippedEnvt = 0;

	/**
	 * Return the keys the result should be associated with. For instance, for a
	 * view where the result should be ordered by package, it should return the
	 * package name. It returns null is the result do not belong to any group,
	 * for instance if you want a failedByPackage view, and the test is not
	 * failed. It returns a list and not unique value in case the splitting is
	 * not unique. For package, class etc, the test will only have 1 key, but if
	 * you're working with groups for instance, you can have one tests tagged
	 * with both SYI and seller reg and buyer reg.
	 * 
	 * @param result
	 * @return list of keys
	 */
	public abstract List<String> getKeys(ITestResult result);

	public void setSuite(ISuite suite) {
		this.suite = suite;
	}

	public void organize() {
		// logger.entering();
		if (suite == null) {
			RuntimeException e = new RuntimeException("Bug. Suite cannot be null");
			// logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		for (ISuiteResult suiteResult : suite.getResults().values()) {
			ITestContext ctx = suiteResult.getTestContext();
			organize(ctx.getPassedTests().getAllResults());
			organize(ctx.getFailedTests().getAllResults());
			organize(ctx.getSkippedTests().getAllResults());
		}
		// logger.exiting();
	}

	private void organize(Collection<ITestResult> results) {
		// logger.entering(results);
		for (ITestResult result : results) {
			if (filter.isValid(result)) {
				for (String key : getKeys(result)) {
					if (key != null) {
						Line l = lineById.get(key);
						if (l == null) {
							l = new Line(key, this);
							lineById.put(key, l);
						}
						l.add(result);

					}
				}
			}
		}
		// logger.exiting();
	}

	public Map<String, Line> getLines() {
		return lineById;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;

	}

	public int getTotalInstancePassed() {
		return totalInstancePassed;
	}

	public int getTotalInstancePassedEnvt() {
		return totalInstancePassedEnvt;
	}

	public int getTotalInstanceFailed() {
		return totalInstanceFailed;
	}

	public int getTotalInstanceFailedEnvt() {
		return totalInstanceFailedEnvt;
	}

	public int getTotalInstanceSkipped() {
		return totalInstanceSkipped;
	}

	public int getTotalInstanceSkippedEnvt() {
		return totalInstanceSkippedEnvt;
	}

}
