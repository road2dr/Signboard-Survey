package com.mjict.signboardsurvey.util;

public 	class WrongDataInputtedException extends Exception {		
	private static final long serialVersionUID = 6589265756887258207L;
	int spot;	// res id
	int causeString;	// res id
	
	public WrongDataInputtedException(int s, int c) {
		spot = s;
		causeString = c;
	}
	
//	public WrongDataInputtedException(String s, int resid) {
//		spot = s;
//		causeString = activity.getString(resid);
//	}
//	
//	public WrongDataInputtedException(int sid, int cid) {
//		spot = activity.getString(sid);
//		causeString = activity.getString(cid);
//	}
	
	public int getSpot() {
		return spot;
	}
	
	public int getCauseString() {
		return causeString;
	}
}
