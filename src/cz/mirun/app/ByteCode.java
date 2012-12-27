package cz.mirun.app;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import cz.mirun.app.Instruction.InsSet;

public class ByteCode {
	
	public List<Instruction> instructions;
	
	public ByteCode()
	{
		instructions = new ArrayList<Instruction>();
	}
	
	public ByteCode(List<Instruction> instructions)
	{
		this.instructions = instructions;
	}
	
	public void addInstruction(Instruction ins)
	{
		instructions.add(ins);
	}
	
	public Instruction getInstruction(int index)
	{
		return (instructions.get(index));
	}
	
	public int getInstructionIndexByLabel(String label)
	{
		for (int i = 0; i < instructions.size(); i++)
		{
			if (instructions.get(i).equals(label))
			{
				return (i);
			}
		}
		
		return (-1);
	}
	
	public void print()
	{
		int i = 0;
		for (Instruction ins : instructions)
		{
			System.out.println(i + ": " + ins.opcode + "" + (ins.operandsToString().length() > 0 ? " " + ins.operandsToString() : "") + "" + (ins.label != null && ins.label != -1 ? " " + ins.label : ""));
			i++;
		}
	}
	
	public StringReader printToReader() {
		StringWriter sw = new StringWriter();
		int i = 0;
		for (Instruction ins : instructions)
		{
			sw.write(i + ": " + ins.opcode + "" + (ins.operandsToString().length() > 0 ? " " + ins.operandsToString() : "") + "" + (ins.label != -1 ? " " + ins.label : "") + "\r\n");
			i++;
		}
		StringReader sr = new StringReader(sw.toString());
		return sr;
	}
	
	public int size()
	{
		return this.instructions.size();
	}
	
	public void changeOperand(int indexOfInstruction, int indexOfOperand, String operand)
	{
		this.instructions.get(indexOfInstruction).operands.remove(indexOfOperand);
		this.instructions.get(indexOfInstruction).operands.add(operand); 
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}

	public static Instruction getInstructionFromString(String str) {
		String[] line = str.split(" ");
		//System.out.println("Strline " + str);
		String params = "";
		for (int i = 2; i < line.length; i++) params += line[i] + " ";
		Instruction ins = new Instruction(Instruction.InsSet.valueOf(line[1]), params);
		return ins;
	}
	
	public String insToString(Instruction ins) {
		String out = this.getInstructions().indexOf(ins) + ": " + ins.opcode + " ";
		for (String op : ins.operands)
		{
			if (!op.isEmpty()) out = out.concat(op + " ");
		}
		out += ins.label == -1 ? "" : ins.label;
		return out;
	}
	
	public ByteCode clone() {
		return new ByteCode(this.instructions);
	}
	
}
