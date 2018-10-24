
public class Instruction {
	Label label;
	Instructions opcode;
	Operand operand1, operand2;
	int lineNumber = 0;
	Instruction next;
	
	public Instruction() {
		label = null;
		opcode = Instructions.nop;
		operand1 = operand2 = null;
		next = null;
	}
	
	public Instruction(int line) {
		label = null;
		opcode = Instructions.nocode;
		operand1 =  operand2 = null;
		next = null;
		lineNumber = line;
	}
	
	public Instruction(Label lbl) {
		label = lbl;
		opcode = Instructions.nocode;
		operand1 = operand2 = null;
		next = null;
	}
	
	public Instruction(Instructions op) {
		label = null;
		opcode = op;
		operand1 = operand2 = null;
		next = null;
	}
	
	public Instruction(Instructions op, Label lbl) {
		label = null;
		opcode = op;
		operand1 = new Operand(lbl);
		operand2 = null;
		next = null;
	}
	
	public Instruction(Instructions op, Operand opr1) {
		label = null;
		opcode = op;
		operand1 = opr1;
		operand2 = null;
		next = null;
	}
	
	public Instruction(Instructions op, Operand opr1, Operand opr2) {
		label = null;
		opcode = op;
		operand1 = opr1;
		operand2 = opr2;
		next = null;
	}
	
}
