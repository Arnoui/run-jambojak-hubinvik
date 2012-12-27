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
import java.lang.reflect.Method;
import java.util.ArrayList;

import cz.mirun.app.ByteCode;
import cz.mirun.app.interpreter.frames.MethodFrame;
import cz.mirun.app.interpreter.frames.MethodFrameFactory;

/**
 * Main interpreter class 
 * 
 * @author Jakub Jambor
 *
 */

public class InterpreterMain {

	static ByteCode instructions;
	static int maxPC, currPC;
	
	public static void main(String[] args) throws Exception {
				
		 //if (args.length != 1) {
	         //System.out.println("BAD USAGE : call with compileSource.bat <filename>");
	         //System.exit(1);
	     //}
		String filename = "out.txt";
		
		instructions = new ByteCode();
		maxPC = 0;
		currPC = 0;
		
		FileInputStream fis = new FileInputStream(new File(filename));
		DataInputStream in = new DataInputStream(fis);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		scanInstructions(br);
		
		in.close();
		try {
			MethodFrame main = MethodFrameFactory.getInstance().getNewFrame("main");
			main.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Error("Severe error while interpreting bytecode! Possibly invalid bytecode structure? \nOriginal stack trace above");
		}
		//ValuePair returnResult = InterpreterContext.getInstance().popFromStack();
		//String[] result = (String[]) InterpreterContext.getInstance().getFromVarPool("2").getFirst();
		//for (int i = 0; i < result.length; i++) System.out.println(result[i]);
		
		//writeOutput(filename, returnResult);
		
		InterpreterContext.getInstance().cleanContext();
		
	}
	
	 private static void writeOutput(String filename, ValuePair result) throws IOException {
		 BufferedWriter out = new BufferedWriter(new FileWriter(filename +".out"));

		 out.write("Output of the sample program " + filename + "\n");
		 out.write("Output is : " + result.getFirst().toString());

		 out.close();
	 }
	
	private static void scanInstructions(BufferedReader br) throws IOException {
		String strLine, methodHeader = null;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.split(" ")[1].equals("FUNSTART")) {
				if (methodHeader != null) {
					MethodFrameFactory.getInstance().addNewPrototype(instructions, methodHeader, methodHeader.split(" ")[2]);
					instructions.getInstructions().clear();
				}
				methodHeader = strLine;
			}
			else instructions.addInstruction(ByteCode.getInstructionFromString(strLine));			
		}
		MethodFrameFactory.getInstance().addNewPrototype(instructions.clone(), methodHeader, methodHeader.split(" ")[2]);
		//instructions.getInstructions().clear();

	}
		
}
	
