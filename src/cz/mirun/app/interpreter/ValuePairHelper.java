package cz.mirun.app.interpreter;

import cz.mirun.app.interpreter.frames.MethodFrame;

public class ValuePairHelper {

	public static void storeToVar(ValuePair value, String key, MethodFrame context) {
		ValuePair var = context.getFromVarPool(key);
		Object toStore = value.getFirst();
		if (value.getSecond().equals("int")) toStore = Integer.parseInt(toStore.toString());
		var.setFirst(toStore);
	}
	
	public static void storeToArray(ValuePair value, String key, int index, MethodFrame context) {
		ValuePair var = context.getFromVarPool(key);
		Object[] array = (Object[]) var.getFirst();
		Object toStore = value.getFirst();
		if (array instanceof Integer[]) toStore = Integer.parseInt(toStore.toString());
		array[index] = toStore;
	}
	
	public static Object loadFromVar(String key, MethodFrame context) {
		return context.getFromVarPool(key).getFirst();		
	}
	
	public static Object loadFromArray(String key, int index, MethodFrame context) {
		ValuePair var = context.getFromVarPool(key);
		Object[] array = (Object[]) var.getFirst();
		return array[index];
	}
	
	public static void storeVar(Object var, String key, String type, MethodFrame context) {
		context.insertIntoVarPool(key, new ValuePair(var,type));
	}

	public static String getObjectType(Object returnObj) {
		if (returnObj instanceof String) return "String";
		else if (returnObj instanceof Integer) return "int";
		else if (returnObj instanceof Integer[]) return "int_array";
		else if (returnObj instanceof String[]) return "string_array";
		else throw new UnsupportedOperationException("Neznamy typ promenne " + returnObj.getClass());
	}
	
	
}
