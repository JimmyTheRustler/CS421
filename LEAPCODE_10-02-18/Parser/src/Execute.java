
public class Execute {

	Node parseTree;
	
	public Execute(Node pTree) {
		parseTree = pTree;
	}
	
	public void run() {
		perform(parseTree);
	}
	
	void perform(Node p) {
		if (p==null) return;
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
							performPrint((PrintNode)p);
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
		if (performExpress((OpNode)p.condition) !=0)
			perform(p.left);
		else
			perform(p.right);
		return;
	}
	
	void performWhile(WhileNode p) {
		while (performExpress((OpNode)(p.condition)) !=0)
			perform(p.left);
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
	
	int performExpress(OpNode p){
		int results=0,operand1, operand2;
		if (p.left instanceof OpNode)
			operand1 = performExpress((OpNode)p.left);
		else
			operand1 = getOperand((Operand)p.left);
		results = operand1;
		if (p.right instanceof OpNode)
			operand2 = performExpress((OpNode)p.right);
		else
			operand2 = getOperand((Operand)p.right);
			
		
		switch (p.operator) {
			case equals:
				storeOperand((Operand)p.left, operand2);
				//System.out.println("Execute storing: "+operand2);
				return operand2;
			case plus:
				results = operand1 + operand2;
				return results;
			case minus:
				results = operand1 - operand2;
				return results;
			case mult:
				results = operand1 * operand2;
				return results;
			case divd:
				results = operand1 / operand2;
				return results;
			case equ:
				if (operand1 == operand2)
					return 1; else return 0;
			case neq:
				if (operand1 != operand2)
					return 1; else return 0;
			case lss:
				if (operand1 < operand2)
					return 1; else return 0;
			case gtr:
				if (operand1 > operand2)
					return 1; else return 0;
			case leq:
				if (operand1 <= operand2)
					return 1; else return 0;
			case geq:
				if (operand1 >= operand2)
					return 1; else return 0;
			default:
				System.out.println("Parser Error in an expression");
		}
		return results;
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
}
