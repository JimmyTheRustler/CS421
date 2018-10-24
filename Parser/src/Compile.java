
public class Compile {

	Node parseTree;
	Instruction codehead = null, codetail = null;

	public Compile(Node pTree) {
		parseTree = pTree;
		Instruction start = new Instruction();
		start.label = new Label("start");
		start.opcode = Instructions.nop;
		addcode(start);
	}

	private void addcode(Instruction code) {
		if (codehead==null)
			codehead = code;
		else
			codetail.next = code;
		codetail = code;
		code.next = null;
		//print(code);
	}

	public Instruction run() {
		perform(parseTree);
		addcode(new Instruction(Instructions.halt));
		return codehead;
	}

	void perform(Node p) {
		if (p==null) return;
		addcode(new Instruction(p.sourceLine));
		if (p instanceof BlockNode)
			performBlock((BlockNode)p);
		else
			if (p instanceof IfNode)
				performIf((IfNode)p);
			else
				if (p instanceof WhileNode)
					performWhile((WhileNode)p);
				else
					if (p instanceof OpNode)
						performExpress((OpNode)p);
					else
						if (p instanceof PrintNode)
							;//performPrint((PrintNode)p);
						else
							System.out.println("Node not implemented");
		return;
	}

	void performBlock(BlockNode p) {
		perform(p.left);
		if (p.right!=null)
			performBlock((BlockNode)p.right);
	}

	void performIf(IfNode p) {
		Operand condition;
		Label L1 = new Label();
		Label L2 = new Label();
		condition =performExpress((OpNode)p.condition);
		addcode(new Instruction(Instructions.test,condition));
		addcode(new Instruction(Instructions.jz,L1));
		perform(p.left);
		addcode(new Instruction(Instructions.jmp,L2));
		addcode(new Instruction(L1));
		perform(p.right);
		addcode(new Instruction(L2));
		return;
	}

	void performWhile(WhileNode p) {
		Operand condition;
		Label L1 = new Label();
		Label L2 = new Label();
		Instruction firstInst = new Instruction(L1);
		firstInst.lineNumber = p.sourceLine;
		addcode(firstInst);
		condition =performExpress((OpNode)p.condition);
		addcode(new Instruction(Instructions.test,condition));
		addcode(new Instruction(Instructions.jz,L2));
		perform(p.left);
		addcode(new Instruction(Instructions.jmp,L1));
		addcode(new Instruction(L2));
		return;

	}

	void performPrint(PrintNode p) {
		while (p!=null) {
			if (p.left!=null) {
				if (p.left instanceof LiteralNode) {
					System.out.print(((LiteralNode)p.left).literal);
				} else
					if (p.left instanceof Operand)
						System.out.print(getOperand((Operand)p.left));
					else
						if (p.left instanceof OpNode)
							System.out.print(performExpress((OpNode)p.left));
			}
			p = (PrintNode)p.right;
		}
		System.out.println();
	}

	Operand performExpress(OpNode p){
		Operand eax,ebx,operand1, operand2;
		Label L1;

		if (p.left instanceof OpNode)
			operand1 = performExpress((OpNode)p.left);
		else
			operand1 = (Operand)p.left;
		if (p.right instanceof OpNode)
			operand2 = performExpress((OpNode)p.right);
		else
			operand2 = (Operand)p.right;

		eax = Operand.reg(1);
		if (operand1.mode == Addrmode.stack) 
			addcode(new Instruction(Instructions.popl, eax));
		else
			if (p.operator!=Symbol.equals)
				addcode(new Instruction(Instructions.movl, eax, operand1));
		operand1 = eax;	

		if (operand2.mode == Addrmode.stack) {
			ebx = Operand.reg(2);
			addcode(new Instruction(Instructions.popl, ebx));
			operand2 = ebx;
		}



		switch (p.operator) {
		case equals:
			ebx = Operand.reg(2);
			if (operand2.mode == Addrmode.register) {
				addcode(new Instruction(Instructions.movl,(Operand)p.left, operand2));
				return operand2;
			}
			else {
				addcode(new Instruction(Instructions.movl,ebx,operand2));
				addcode(new Instruction(Instructions.movl,(Operand)p.left, ebx));
			}
			return ebx;
		case plus:
			addcode(new Instruction(Instructions.addl, operand1, operand2));
			addcode(new Instruction(Instructions.pushl, operand1));
			return new Operand(operand1,Addrmode.stack);
		case minus:
			addcode(new Instruction(Instructions.subl,  operand1, operand2));
			addcode(new Instruction(Instructions.pushl, operand1));
			return new Operand(operand1, Addrmode.stack);
		case mult:
			addcode(new Instruction(Instructions.mull, operand1, operand2));
			addcode(new Instruction(Instructions.pushl, operand1));
			return new Operand(operand1, Addrmode.stack);		case divd:
				addcode(new Instruction(Instructions.divl, operand1, operand2));
				addcode(new Instruction(Instructions.pushl, operand1));
				return new Operand(operand1, Addrmode.stack);		case equ:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,operand1, operand2));
					addcode(new Instruction(Instructions.jz,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				case neq:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,(Operand)p.left, operand2));
					addcode(new Instruction(Instructions.jnz,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				case lss:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,(Operand)p.left, operand2));
					addcode(new Instruction(Instructions.jls,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				case gtr:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,(Operand)p.left, operand2));
					addcode(new Instruction(Instructions.jgt,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				case leq:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,(Operand)p.left, operand2));
					addcode(new Instruction(Instructions.jle,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				case geq:
					L1 = new Label();
					eax = Operand.reg(1);
					addcode(new Instruction(Instructions.movl,Operand.constant(1),eax));
					addcode(new Instruction(Instructions.cmp,(Operand)p.left, operand2));
					addcode(new Instruction(Instructions.jge,L1));
					addcode(new Instruction(Instructions.movl,Operand.constant(0),eax));
					addcode(new Instruction(L1));
					return eax;
				default:
					System.out.println("Parser Error in an expression");
		}
		return null;
	}

	int getOperand(Operand operand) {
		/*		operand.kind = operand.link.kind;
		operand.value= (int)operand.link.value;

			operand.value = lex.inum;
			operand.kind = SymbolType.constant;
			operand.link = null;

		 */	
		if (operand.kind == SymbolType.constant)
			return operand.value;
		return (int)operand.link.value;
	}

	void storeOperand(Operand operand, int value) {
		operand.link.value = value;
	}

	void print(Instruction ip){
		if (ip.label!=null) System.out.print(ip.label.getLabel()+":  ");
		else System.out.print("       ");
		System.out.print(ip.opcode);
		System.out.print("   ");
		if (ip.operand1!=null) System.out.print(ip.operand1.getString());
		if (ip.operand2!=null) System.out.print(","+ip.operand2.getString());
		System.out.println();
	}
}
