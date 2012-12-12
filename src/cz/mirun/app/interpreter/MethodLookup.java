package cz.mirun.app.interpreter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
		Method meth = lookupMethod(methodName, params);
	}
	
	/**
	 * We are gonna use a very limited number of classes/methods, so this construct is probably sufficient
	 * 
	 * @param methodName
	 * @return
	 */
	
	private static Method lookupMethod(String methodName, ValuePair[] params) {
		Method method = null;
		return method;
	}
	
	private static Class getRequestedClass(String className) {
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return clazz;
	}
	
}
