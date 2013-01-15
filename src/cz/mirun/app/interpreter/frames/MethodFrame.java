package cz.mirun.app.interpreter.frames;

import java.util.HashMap;
import java.util.Map;

import cz.mirun.app.ByteCode;
import cz.mirun.app.interpreter.BytecodeExecutor;
import cz.mirun.app.interpreter.InterpreterContext;
import cz.mirun.app.interpreter.ValuePair;

public class MethodFrame implements Cloneable {

	String methodHeader;
	ByteCode byteCode;
	Map<String, ValuePair> localVarPool;
	int currPC, maxPC;
	
	public MethodFrame(ByteCode byteCode, String header) {
		this.byteCode = byteCode;
		this.methodHeader = header;
		//init();
		
	}
	
	public void init() {
		this.localVarPool = new HashMap<String, ValuePair>();
		this.currPC = 0;
		this.maxPC = this.byteCode.size();
		
		String[] hParams = methodHeader.split(" ");

//		System.out.println("Initialized " + byteCode.getInstructions().size());
	}
	
	public void execute() {
		for (; currPC < maxPC; currPC++) {
			//System.out.println("Executing, curr PC : " + currPC + ", maxPC is :" + maxPC);
			BytecodeExecutor.executeInstruction(byteCode.insToString(byteCode.getInstruction(currPC)), this);
		}
	}

	public ByteCode getByteCode() {
		return byteCode;
	}

	public void setByteCode(ByteCode byteCode) {
		this.byteCode = byteCode;
	}

	public Map<String, ValuePair> getLocalVarPool() {
		return localVarPool;
	}

	public void setLocalVarPool(Map<String, ValuePair> localVarPool) {
		this.localVarPool = localVarPool;
	}

	public int getCurrPC() {
		return currPC;
	}

	public void setCurrPC(int currPC) {
		this.currPC = currPC;
	}

	public int getMaxPC() {
		return maxPC;
	}

	public void setMaxPC(int maxPC) {
		this.maxPC = maxPC;
	}
	
	@Override
	public MethodFrame clone() {
		//System.out.println("Cloning.. " + this.byteCode.getInstructions().size());
		return new MethodFrame(this.byteCode.clone(), this.methodHeader);		
	}
	
	public ValuePair getFromVarPool(String key) {
		return this.localVarPool.get(key);
	}
	
	public void insertIntoVarPool(String key, ValuePair value) {
		this.localVarPool.put(key, value);
	}
	
}
