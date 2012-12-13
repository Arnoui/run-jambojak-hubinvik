package cz.mirun.app.interpreter;

public class ValuePairHelper {

	public static void storeToVar(ValuePair value, String key) {
		ValuePair var = InterpreterContext.getInstance().getFromVarPool(key);
		Object toStore = value.getFirst();
		if (value.getSecond().equals("int")) toStore = Integer.parseInt(toStore.toString());
		var.setFirst(toStore);
	}
	
	public static void storeToArray(ValuePair value, String key, int index) {
		ValuePair var = InterpreterContext.getInstance().getFromVarPool(key);
		Object[] array = (Object[]) var.getFirst();
		Object toStore = value.getFirst();
		if (value.getSecond().equals("int")) toStore = Integer.parseInt(toStore.toString());
		array[index] = toStore;
	}
	
	public static Object loadFromVar(String key) {
		return InterpreterContext.getInstance().getFromVarPool(key).getFirst();		
	}
	
	public static Object loadFromArray(String key, int index) {
		ValuePair var = InterpreterContext.getInstance().getFromVarPool(key);
		Object[] array = (Object[]) var.getFirst();
		return array[index];
	}
	
	public static void storeVar(Object var, String key, String type) {
		InterpreterContext.getInstance().insertIntoVarPool(key, new ValuePair(var,type));
	}
	
	
}
