package cz.mirun.app.interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Main interpreter class 
 * 
 * @author Jakub Jambor
 *
 */

public class InterpreterMain {

	static ArrayList<String> instructions;
	static int maxPC, currPC;
	
	public static void main(String[] args) throws Exception {
				
		 //if (args.length != 1) {
	         //System.out.println("BAD USAGE : call with compileSource.bat <filename>");
	         //System.exit(1);
	     //}
		String filename = "out.txt";
		
		instructions = new ArrayList<String>();
		maxPC = 0;
		currPC = 0;
		
		FileInputStream fis = new FileInputStream(new File(filename));
		DataInputStream in = new DataInputStream(fis);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		scanInstructions(br);
		
		in.close();
		
		executeByteCode();
	
		//ValuePair returnResult = InterpreterContext.getInstance().popFromStack();
		//System.out.println(returnResult.getFirst().toString() + " " + returnResult.getSecond().toString());
	
		//writeOutput(filename, returnResult);
		
		System.out.println(InterpreterContext.getInstance().getVarPool().get("4").getFirst());
		InterpreterContext.getInstance().cleanContext();
		}
	
	 private static void writeOutput(String filename, ValuePair result) throws IOException {
		 BufferedWriter out = new BufferedWriter(new FileWriter(filename +".out"));

		 out.write("Output of the sample program " + filename + "\n");
		 out.write("Output is : " + result.getFirst().toString());

		 out.close();
	 }
	
	/**
	 * Checks all instructions to determine final program counter 
	 * 
	 * @param br
	 * @throws IOException
	 */
	
	private static void scanInstructions(BufferedReader br) throws IOException {
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			instructions.add(strLine);
			
		}
		maxPC = instructions.size();
	}
	
	/**
	 * Executes corresponding line of instructions, reacts to jumps
	 * 
	 */
	
	private static void executeByteCode() {
		for (; currPC < maxPC; currPC++) {
			String line = instructions.get(currPC);
			String[] lineParams = line.split(" ");
			if (currPC > 0 && lineParams[1].equals("FUNSTART")) break; // Program should never reach this on its own
			handleParams(lineParams);
		}
	}

	/**
	 * Parameter handling method, calls various stack/varPool/method handling operations, based on the instruction specified
	 * 
	 * @param lineParams
	 */
	
	private static void handleParams(String[] lineParams) {
		String instr = lineParams[1]; // prvni je cislo radky
		String instrParam;
		String type;
		System.out.println(instr);
		if (lineParams.length == 2) { // Jen jedna hodnota, coz je nazev instrukce bajtkodu bez dalsich hodnot
			if (instr.equals("MULTIPLY") || instr.equals("PLUS") || instr.equals("MINUS")) 
				InterpreterContext.getInstance().pushToStack(new ValuePair(MethodLookup.performArithmetic(instr), "int"));
			else if (instr.equals("RETURN")) {
				currPC = InterpreterContext.getInstance().popFromRetStack();
			}
		}	
		else if (lineParams.length >= 2 && instr.equals("FUNCALL")) { 
			String funName = lineParams[2];
			ValuePair [] params = new ValuePair[lineParams.length - 2];
			for (int i = 3; i < lineParams.length; i++) params[i-3] = InterpreterContext.getInstance().popFromStack();
		}
		else if (lineParams.length >= 2 && instr.equals("NEW_ARRAY")) { 
			createNewArray(lineParams);
		}
		else if (lineParams.length == 3) { // Dve hodnoty, nazev instrukce a parametr
			instrParam = lineParams[2];
			if (instr.equals("PUSH_NUMBER")) InterpreterContext.getInstance().pushToStack(new ValuePair(instrParam, "int"));
			else if (instr.equals("PUSH_STRING")) InterpreterContext.getInstance().pushToStack(new ValuePair(instrParam, "String"));
			else if (isLogicalCondition(instr)) {
				handleLogicalCondition(instr, instrParam);
			}
			else if (instr.equals("JUMP")) currPC = Integer.parseInt(instrParam)-1; // gotta decrement because the PC is automatically incremented in executeByteCode for iteration
			else if (instr.equals("FUNJUMP")) {
				InterpreterContext.getInstance().pushToRetStack(currPC);
				currPC = Integer.parseInt(instrParam);
			}
			else if (instr.equals("NOP") || instr.equals("FUNSTART")) return;
			else if (instr.equals("LOAD_VAR")) {
				ValuePair varVal = InterpreterContext.getInstance().getFromVarPool(instrParam);
				InterpreterContext.getInstance().pushToStack(varVal);
			}
		}
		
		else if (lineParams.length == 4) { // Nazev instrukce, parametr a typ parametru
			instrParam = lineParams[2];
			type = lineParams[3];
			if (instr.equals("STORE_VAR")) {
				Object varVal = InterpreterContext.getInstance().popFromStack();
				if (varVal instanceof ValuePair) InterpreterContext.getInstance().insertIntoVarPool(instrParam, (ValuePair) varVal);
				else InterpreterContext.getInstance().insertIntoVarPool(instrParam, new ValuePair(varVal, type));
			}
			else if (instr.equals("STORE_ARRAY")) {
				ValuePair valPair = InterpreterContext.getInstance().popFromStack();
				Integer index = Integer.parseInt(lineParams[3]);
				ValuePairHelper.storeToArray(valPair, lineParams[2], index);
			}
		}
		
	}
	
	private static void createNewArray(String[] lineParams) {
		String instrParam = lineParams[2];
		String arrType = lineParams[3];
		if (lineParams.length == 4) {
			ValuePair valPair = InterpreterContext.getInstance().popFromStack();
			Integer arrLen;
			if (valPair.getFirst() instanceof String) arrLen = Integer.parseInt(valPair.getFirst().toString());
			else arrLen = (Integer) valPair.getFirst();
			InterpreterContext.getInstance().insertIntoVarPool(instrParam, new ValuePair(new Integer[arrLen], arrType));
		} else {
			Integer arrLen = Integer.parseInt(lineParams[3]);
			arrType = lineParams[4];
			InterpreterContext.getInstance().insertIntoVarPool(instrParam, new ValuePair(new Integer[arrLen], arrType));
		}
		
	}

	/**
	 * A method handling jumps based on the satisfaction of the specified logical expression 
	 * 
	 * @param instr
	 * @param instrParam
	 */
	
	private static void handleLogicalCondition(String instr, String instrParam) {
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
		
		if (jump) currPC = PC-1;
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
	
