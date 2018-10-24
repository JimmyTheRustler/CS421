
public class Operand extends Node{
	SymbolType kind;
	Addrmode mode;
	int register;
	int value;
	int size;
	SymbolTable.TableEntry link;
	
	public Operand() {

	}

	public Operand(Operand cp)
	{
		kind = cp.kind;
		mode = cp.mode;
		register = cp.register;
		value = cp.value;
		size = cp.size;
		link = cp.link;
	}
	
	public Operand(Label lbl) {
		kind = SymbolType.label;
		mode = Addrmode.absolute;
		value= lbl.labelnumber;
		link = null;
	}
	
	public Operand(Operand cp, Addrmode newmode)
	{
		kind = cp.kind;
		mode = newmode;
		register = cp.register;
		value = cp.value;
		size = cp.size;
		link = cp.link;
	}
	
	public static Operand constant(int i){
		Operand opr = new Operand();
		opr.value = i;
		opr.mode = Addrmode.immediate;
		opr.kind = SymbolType.constant;
		opr.size = 4;
		opr.link = null;
		return opr;
	}
	
	public static Operand reg(int regnum) {
		Operand opr = new Operand();
		opr.register = regnum;
		opr.value = 0;
		opr.mode = Addrmode.register;
		opr.kind = SymbolType.variable;
		opr.size = 4;
		opr.link = null;
		return opr;
	}
	
	public String getString() {
		String s = "";
		if (this == null) return "null";
		if (mode == null) return "null";
		if (kind == SymbolType.label) return "L"+value;
		switch (mode) {
		case immediate:
			s = "$"+value;
			break;
		case register:
			switch (this.register){
			case 1: s="%eax";
			break;
			case 2: s="%ebx";
			break;
			case 3: s="%ecx";
			break;
			case 4: s="%edx";
			break;
			}
			break;
		case absolute:
			s = link.name;
			break;
		case stack:
			s = "stack";
			break;
		default:
			s = "unknown";
				
		}
		return s;
	}
}
