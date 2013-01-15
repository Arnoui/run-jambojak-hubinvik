package cz.mirun.app.interpreter;

import cz.mirun.app.interpreter.frames.MethodFrame;

public class BytecodeExecutor {

	public static void executeInstruction(String instruction, MethodFrame context) {
		//System.out.println(instruction);
		String[] lineParams = instruction.split(" ");
		String instr = lineParams[1]; // prvni je cislo radky
		String type;
		//System.out.println(instr);
		if (instr.equals("MULTIPLY") || instr.equals("PLUS") || instr.equals("MINUS")) 
			InterpreterContext.getInstance().pushToStack(new ValuePair(MethodLookup.performArithmetic(instr), "int"));
		else if (instr.equals("RETURN")) {
			return;//context.setCurrPC(InterpreterContext.getInstance().popFromRetStack());
		}
		else if (instr.equals("FUNCALL")) { 
			String funName = lineParams[2];
			ValuePair [] params = new ValuePair[lineParams.length - 3];
			for (int i = 3; i < lineParams.length; i++) params[i-3] = context.getFromVarPool(lineParams[i]);
			//InterpreterContext.getInstance().pushToRetStack(context.getCurrPC());
			MethodLookup.callMethod(funName, params);
		}
		else if (instr.equals("NEW_ARRAY")) { 
			createNewArray(lineParams, context);
		}
		else if (instr.equals("PUSH_NUMBER")) InterpreterContext.getInstance().pushToStack(new ValuePair(lineParams[2], "int"));
		else if (instr.equals("PUSH_STRING")) {
			String param = lineParams[2];
			if (param.equals("~")) param = " ";// Znacka pro mezeru
			if (lineParams.length > 3) { // Nutna uprava, protoze v textu samozrejme muzou byt mezery, coz interpret rozparsuje jako dalsi parametry
				for (int i = 3; i < lineParams.length; i++) param+=" " + lineParams[i];
			}
			InterpreterContext.getInstance().pushToStack(new ValuePair(param, "string"));
		}
		else if (isLogicalCondition(instr)) {
			handleLogicalCondition(instr, lineParams[2], context);
		}
		else if (instr.equals("JUMP")) context.setCurrPC(Integer.parseInt(lineParams[2])-1); // gotta decrement because the PC is automatically incremented in executeByteCode for iteration
		else if (instr.equals("FUNJUMP")) {
			InterpreterContext.getInstance().pushToRetStack(context.getCurrPC());
			context.setCurrPC(Integer.parseInt(lineParams[2]));
		}
		else if (instr.equals("NOP") || instr.equals("FUNSTART")) return;
		else if (instr.equals("LOAD_VAR")) {
			ValuePair varVal = context.getFromVarPool(lineParams[2]);
			InterpreterContext.getInstance().pushToStack(varVal);
			//System.out.println("Loading " + varVal + " from " + lineParams[2]);
		}
		else if (instr.equals("STORE_VAR")) {
			type = lineParams[4];
			Object varVal = InterpreterContext.getInstance().popFromStack();
			if (varVal instanceof ValuePair) context.insertIntoVarPool(lineParams[2], (ValuePair) varVal);
			else context.insertIntoVarPool(lineParams[2], new ValuePair(varVal, type));
			//System.out.println("Storing " + varVal + " to " + lineParams[2]);
			InterpreterContext.getInstance().insertIntoVarMappings(lineParams[3], Integer.parseInt(lineParams[2]));	
		}
		else if (instr.equals("STORE_ARRAY")) {
			ValuePair valPair = InterpreterContext.getInstance().popFromStack();
			Integer index = null;
			try {
				index = Integer.parseInt(lineParams[3]);
			} catch (NumberFormatException nfe) {
				String varName = lineParams[3];
				String varIndex = InterpreterContext.getInstance().getFromVarMappings(varName).toString();
				index = Integer.parseInt(context.getFromVarPool(varIndex).getFirst().toString());
			}
			ValuePairHelper.storeToArray(valPair, lineParams[2], index, context);
		}
		else if (instr.equals("LOAD_ARRAY")) {
			Integer index = null;
			try {
				index = Integer.parseInt(lineParams[3]);
			} catch (NumberFormatException nfe) {
				String varName = lineParams[3];
				String varIndex = InterpreterContext.getInstance().getFromVarMappings(varName).toString();
				index = Integer.parseInt(context.getFromVarPool(varIndex).getFirst().toString());
			}
			Object[] array = (Object[]) context.getFromVarPool(lineParams[2]).getFirst();
			InterpreterContext.getInstance().pushToStack(new ValuePair(array[index], ""));
		}
		
		
	}
	
	private static void createNewArray(String[] lineParams, MethodFrame context) {
		String instrParam = lineParams[2];
		String arrType = lineParams[3];
		if (lineParams.length == 4) {
			ValuePair valPair = InterpreterContext.getInstance().popFromStack();
			Integer arrLen;
			if (valPair.getFirst() instanceof String) arrLen = Integer.parseInt(valPair.getFirst().toString());
			else arrLen = (Integer) valPair.getFirst();
			context.insertIntoVarPool(instrParam, new ValuePair(new Integer[arrLen], arrType));
		} else {
			Integer arrLen = Integer.parseInt(lineParams[3]);
			arrType = lineParams[4];
			context.insertIntoVarPool(instrParam, new ValuePair(new Integer[arrLen], arrType));
		}
		
	}

	/**
	 * A method handling jumps based on the satisfaction of the specified logical expression 
	 * 
	 * @param instr
	 * @param instrParam
	 */
	
	private static void handleLogicalCondition(String instr, String instrParam, MethodFrame context) {
		ValuePair second = InterpreterContext.getInstance().popFromStack();
		ValuePair first = InterpreterContext.getInstance().popFromStack();
		
		Integer operand2 = Integer.parseInt(second.getFirst().toString());
		Integer operand1 = Integer.parseInt(first.getFirst().toString()); // For our IFs,  we assume just numbers
		Integer PC = Integer.parseInt(instrParam);
		
		boolean jump = false;
		
		if (instr.equals("IF_GT_JUMP") && (operand1 > operand2)) jump = true; 
		else if (instr.equals("IF_LT_JUMP") && (operand1 < operand2)) jump = true; 
		else if (instr.equals("IF_EQ_JUMP") && (operand1 == operand2)) jump = true; 
		else if (instr.equals("IF_NEQ_JUMP") && (operand1 != operand2)) jump = true;
		else if (instr.equals("IF_GTE_JUMP") && (operand1 >= operand2)) jump = true; 
		else if (instr.equals("IF_LTE_JUMP") && (operand1 <= operand2)) jump = true; 
		
		if (jump) context.setCurrPC(PC-1);
	}
	
	/**
	 * Checks whether the given instruction is a conditional jump, serves mostly to maintain the code simpler
	 * 
	 * @param instr
	 * @return
	 */
	
	private static boolean isLogicalCondition(String instr) {
		if (instr.equals("IF_GT_JUMP") || instr.equals("IF_EQ_JUMP") || instr.equals("IF_LT_JUMP") || instr.equals("IF_NEQ_JUMP") || 
				instr.equals("IF_GTE_JUMP") || instr.equals("IF_LTE_JUMP")) return true;
		return false;
	}
	
	
	
}
