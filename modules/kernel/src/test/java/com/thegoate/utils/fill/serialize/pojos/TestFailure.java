package com.thegoate.utils.fill.serialize.pojos;

import com.thegoate.utils.fill.serialize.Kid;

public class TestFailure extends Kid {
	private String testName;
	private int numberOfFailures;
	public TestFailure() {
	}
	public String getTestName() {
		return testName;
	}
	public TestFailure setTestName(String testName) {
		this.testName = testName;
		return this;
	}
	public int getNumberOfFailures() {
		return numberOfFailures;
	}
	public TestFailure setNumberOfFailures(int numberOfFailures) {
		this.numberOfFailures = numberOfFailures;
		return this;
	}
}