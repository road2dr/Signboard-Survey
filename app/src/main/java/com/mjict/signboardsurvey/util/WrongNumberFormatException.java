package com.mjict.signboardsurvey.util;

public class WrongNumberFormatException extends Exception {
	private static final long serialVersionUID = 2843993658515857333L;
	String causeValue;
	
	public WrongNumberFormatException(String cause) {
		causeValue = cause;
	}
	
	public String getCauseValue() {
		return causeValue;
	}
}
