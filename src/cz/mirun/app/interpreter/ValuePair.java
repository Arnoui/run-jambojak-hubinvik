package cz.mirun.app.interpreter;

import java.lang.reflect.Constructor;

/**
 * Wrapper class to hold both object value and type
 * 
 * @author Jakub Jambor
 *
 */

public class ValuePair {
	
	private Object first; // object value
	private String second; // object type gained from the byte code
	
	public ValuePair(Object first, String second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	public Object getFirst() {
		return first;
	}
	public void setFirst(Object first) {
		this.first = first;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	
	public Class getClassOfValue() {
		if (second.equals("int")) return java.lang.Integer.class;
		return null;
	}
	
}
