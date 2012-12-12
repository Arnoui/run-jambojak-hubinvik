package cz.mirun.app;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class ByteCode {
	
	public List<Instruction> instructions;
	
	public ByteCode()
	{
		instructions = new ArrayList<Instruction>();
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
			System.out.println(i + ": " + ins.opcode + "" + (ins.operandsToString().length() > 0 ? " " + ins.operandsToString() : "") + "" + (ins.label != -1 ? " " + ins.label : ""));
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
		this.instructions.get(indexOfInstruction).operands.add(indexOfOperand, operand);
	}

}
