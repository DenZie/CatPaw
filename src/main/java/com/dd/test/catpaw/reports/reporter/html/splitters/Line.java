package com.dd.test.catpaw.reports.reporter.html.splitters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;



public class Line {
	private String id = null;
	private String label = "NA";

	private Set<ITestNGMethod> methods = new HashSet<ITestNGMethod>();
	private CollectionSplitter splitter;

	private int instancePassed = 0;
	private int instancePassedEnvt = 0;
	private int instanceFailed = 0;
	private int instanceFailedEnvt = 0;
	private int instanceSkipped = 0;
	private int instanceSkippedEnvt = 0;

	private List<ITestResult> associatedResults = new ArrayList<ITestResult>();
	
	public Line(String label, CollectionSplitter splitter) {
		this.splitter = splitter;
		id = UUID.randomUUID().toString();
		this.label = label;
	}

	public int getTotalMethods() {
		return methods.size();
	}

	public void add(ITestResult result) {
//		CatPawLogger.getLogger().entering(result);
		associatedResults.add(result);
		methods.add(result.getMethod());

		boolean envt = Arrays.asList(result.getMethod().getGroups()).contains(
				"envt");
		switch (result.getStatus()) {
		case ITestResult.SUCCESS:
			if (envt) {
				instancePassedEnvt++;
				splitter.totalInstancePassedEnvt++;
			} else {
				instancePassed++;
				splitter.totalInstancePassed++;
			}
			break;
		case ITestResult.FAILURE:
			if (envt) {
				instanceFailedEnvt++;
				splitter.totalInstanceFailedEnvt++;
			} else {
				instanceFailed++;
				splitter.totalInstanceFailed++;
			}
			break;
		case ITestResult.SKIP:
			if (envt) {
				instanceSkippedEnvt++;
				splitter.totalInstanceSkippedEnvt++;
			} else {
				instanceSkipped++;
				splitter.totalInstanceSkipped++;
			}
			break;
		default:
			throw new RuntimeException("NI");
		}
//		CatPawLogger.getLogger().exiting();
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public int getInstancePassed() {
		return instancePassed;
	}

	public int getInstancePassedEnvt() {
		return instancePassedEnvt;
	}

	public int getInstanceFailed() {
		return instanceFailed;
	}

	public int getInstanceFailedEnvt() {
		return instanceFailedEnvt;
	}

	public int getInstanceSkipped() {
		return instanceSkipped;
	}

	public int getInstanceSkippedEnvt() {
		return instanceSkippedEnvt;
	}

	public List<ITestResult> getAssociatedResults() {
		return associatedResults;
	}

	public CollectionSplitter getSplitter() {
		return splitter;
	}
}
