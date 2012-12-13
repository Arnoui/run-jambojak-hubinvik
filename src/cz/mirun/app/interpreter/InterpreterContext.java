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
	private Stack<ValuePair> stack;
	
	private InterpreterContext() {
		varPool = new HashMap<String, ValuePair>();
		stack = new Stack<ValuePair>();
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
	
	public ValuePair getFromVarPool(String key) {
		return this.varPool.get(key);
	}
	
	public void insertIntoVarPool(String key, ValuePair val) {
		this.varPool.put(key, val);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void cleanContext() {
		stack.clear();
		varPool.clear();
	}

	public Map<String, ValuePair> getVarPool() {
		return varPool;
	}

	public Stack<ValuePair> getStack() {
		return stack;
	}
	
	
	
}
