package cz.mirun.app.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A singleton object holding both the app stack and variable pool
 * 
 * @author Jakub Jambor
 *
 */

public class InterpreterContext {

	private static InterpreterContext instance;
	
	private Map<String, ValuePair> varPool;
	private Map<String, Integer> varMappings;
	private Stack<ValuePair> stack;
	private Stack<Integer> returnStack;
	
	private InterpreterContext() {
		varPool = new HashMap<String, ValuePair>();
		stack = new Stack<ValuePair>();
		returnStack = new Stack<Integer>();
		varMappings = new HashMap<String, Integer>();
	}
	
	public static InterpreterContext getInstance() {
		if (instance == null) instance = new InterpreterContext();
		return instance;
	}
	
	public void pushToStack(ValuePair obj) {
		this.stack.push(obj);
	}
	
	public ValuePair popFromStack() {
		return this.stack.pop();
	}
	
	public void pushToRetStack(Integer obj) {
		this.returnStack.push(obj);
	}
	
	public Integer popFromRetStack() {
		return this.returnStack.pop();
	}
	
	public ValuePair getFromVarPool(String key) {
		return this.varPool.get(key);
	}
	
	public void insertIntoVarPool(String key, ValuePair val) {
		this.varPool.put(key, val);
	}
	
	public Integer getFromVarMappings(String key) {
		return this.varMappings.get(key);
	}
	
	public void insertIntoVarMappings(String key, Integer val) {
		this.varMappings.put(key, val);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void cleanContext() {
		varPool.clear();
		varMappings.clear();
		returnStack.clear();
		stack.clear();
	}

	public Map<String, ValuePair> getVarPool() {
		return varPool;
	}
	
	public Map<String, Integer> getVarMappings() {
		return varMappings;
	}

	public Stack<ValuePair> getStack() {
		return stack;
	}
	
	
	
}
