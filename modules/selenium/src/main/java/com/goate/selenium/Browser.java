package com.goate.selenium;

public enum Browser {
	CHROME("chrome"),
	EDGE("edge"),
	IE("ie"),
	FIREFOX("firefox"),
	OPERA("opera"),
	SAFARI("safari");

	String type;

	Browser(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

}
