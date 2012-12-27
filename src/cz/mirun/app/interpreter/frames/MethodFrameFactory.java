package cz.mirun.app.interpreter.frames;

import java.util.HashMap;
import java.util.Map;

import cz.mirun.app.ByteCode;

public class MethodFrameFactory {

	private Map<String, MethodFrame> knownMethodPrototypes;
	private static MethodFrameFactory instance;
	
	private MethodFrameFactory() {
		knownMethodPrototypes = new HashMap<String, MethodFrame>();
	}
	
	public static MethodFrameFactory getInstance() {
		if (instance == null) instance = new MethodFrameFactory();
		return instance;
	}
	
	public void addNewPrototype(ByteCode bCode, String header, String mName) {
		knownMethodPrototypes.put(mName, new MethodFrame(bCode, header));
		//System.out.println(bCode.getInstructions().size());
	}
	
	public MethodFrame getNewFrame(String methodName) {
		MethodFrame prototype = knownMethodPrototypes.get(methodName);
		if (prototype == null) return null;
		MethodFrame toReturn = prototype.clone();
		//System.out.println("Getting method " + methodName);
		return toReturn;
	}
	
}
