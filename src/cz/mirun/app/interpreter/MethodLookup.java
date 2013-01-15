package cz.mirun.app.interpreter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cz.mirun.app.interpreter.frames.MethodFrame;
import cz.mirun.app.interpreter.frames.MethodFrameFactory;

/**
 * Class for calling methods, depending on their types and/or reflection method name searches
 * 
 * @author Jakub Jambor
 *
 */

public class MethodLookup {

	public MethodLookup() {
		
	}
		
	/**
	 * Performs simple arithmetics
	 * 
	 * @param operation
	 * @return
	 */
	
	public static Object performArithmetic(String operation) {
		Object result = null;
		Integer operand2 = Integer.parseInt(InterpreterContext.getInstance().popFromStack().getFirst().toString());
		Integer operand1 = Integer.parseInt(InterpreterContext.getInstance().popFromStack().getFirst().toString()); // Arithmetics, we assume numbers
		
		if (operation.equals("PLUS")) result = operand1+operand2;
		else if (operation.equals("MINUS")) result = operand1-operand2;
		else if (operation.equals("MULTIPLY")) result = operand1*operand2;		
		
		return result;
	}
	
	public static void callMethod(String methodName, ValuePair[] params) {
		MethodFrame method = MethodFrameFactory.getInstance().getNewFrame(methodName);
		if (method != null ) {
			method.init();
			method.execute(); // Calls internal method
		}
		else { // Looking up the method in our bytecode failed
			Object[] pVals = extractParams(params);
			lookupMethod(methodName, pVals); // Calls external method
		}		
	}
	
	/**
	 * We are gonna use a very limited number of classes/methods, so this construct is probably sufficient
	 * 
	 * @param methodName
	 * @return
	 */
	
	private static void lookupMethod(String methodName, Object[] params) {
		Method method = null;
		Object returnObj = null;
		try {
			if (methodName.equals("print")) {
				params = evalParams(params, 1);
				final PrintStream printClass = System.out; 
				method = printClass.getClass().getMethod("println", params[0].toString().getClass());
				returnObj = method.invoke(printClass, params[0].toString());
			}
			else if (methodName.equals("split")) {
				params = evalParams(params, 2);
				Object toSplit = params[1];
				Object delim = params[0];
				method = toSplit.getClass().getMethod("split", delim.getClass());
				returnObj = method.invoke(toSplit, delim);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(returnObj);
		if (returnObj != null){
			// Volani ma navratovou hodnotu, hodime ji na stack
			InterpreterContext.getInstance().pushToStack(new ValuePair(returnObj, ValuePairHelper.getObjectType(returnObj)));
		}
	}
	
	private static Object[] evalParams(Object[] params, int paramsNeeded) {
		Object[] newParams = new Object[paramsNeeded];
		int check = paramsNeeded;
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) newParams[i] = InterpreterContext.getInstance().popFromStack().getFirst(); // Chybi nam nutny parametr, tak ho vezmeme ze stacku
			else newParams[i] = params[i];
			check--;
		}
		if (check > 0) {
			for (int i = params.length; i < paramsNeeded; i++) newParams[i] = InterpreterContext.getInstance().popFromStack().getFirst(); // Chybi nam nutny parametr, tak ho vezmeme ze stacku
		}
		return newParams;
	}

	private static Object[] extractParams(ValuePair[] params) {
		Object[] pVals = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			pVals[i] = params[i].getFirst();
		}
		return pVals;
	}
	
	/**
	private static Class getRequestedClass(String className) {
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return System.out.getClass();
	}
	**/
}
