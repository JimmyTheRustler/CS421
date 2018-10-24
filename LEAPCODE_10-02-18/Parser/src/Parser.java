public class Parser {
	static Lexical lex;
	static Symbol sy;
	static SourceHandler src;
	static SymbolTable symtable = new SymbolTable();
	static Node programRoot = null;

	public static void main(String[] args) {
		src = new SourceHandler("test.txt");
		src.reset();
		src.nextch();
		lex = new Lexical(src);
		
		lex.reset();	
		sy = lex.nextToken();
		System.out.println("\n\n\n========= Starting your program ==========");
		program();
		symtable.print();
		//Execute program = new Execute(programRoot);
		//program.run();
		Instruction codeSequence;
		Compile program = new Compile(programRoot);
		codeSequence = program.run();
		//Optimizer.peepHole(codeSequence);
		Generate.listProgram(codeSequence);
	}

	public static void expect(Symbol s) {
		if (sy == s) sy = lex.nextToken();
		else
			src.error("Expected symbol "+s+", found "+sy+" instead.");
	}
	
	public static void program() {
		expect(Symbol.ident);
		expect(Symbol.lparen);
		expect(Symbol.rparen);
		expect(Symbol.eoln);
		programRoot = block();
	}

	public static BlockNode block() {
		BlockNode head = null;
		BlockNode tail = null;
		BlockNode p;
		expect(Symbol.lbrace);
		if (sy==Symbol.eoln) sy = lex.nextToken();
		while (sy != Symbol.rbrace) {
			p = new BlockNode();
			p.left = statement();
			expect(Symbol.semicolon);
			if (sy==Symbol.eoln) sy = lex.nextToken();
			p.right = null;
			if (head==null)
				head = p;
			else 
				tail.right = p;
			tail = p;
		}
		expect(Symbol.rbrace);
		return head;
	}
	
	public static Node statement() {
		Node root = null;
		int stmtLineNumber = src.currentLineNumber();
		switch (sy) {
		case print: 
			root = printStatement();
			break;
		case gotostmt:
			gotoStatement();
			break;
		case ifstmt:
			root = ifStatement();
			break;
		case whilestmt:
			root = whileStatement();
			break;
		case ident:
			root = assignment();
			break;
		case lbrace: 
			root = block();
			break;
		case intsym:
		case doublesym:
			variableDeclaration();
			break;
		case label:
			labelDeclaration();
			break;
		default:
			src.error("Unrecognized Statement: [begins with] "+sy);
		}
		if (root!=null) root.sourceLine = stmtLineNumber;
		return root;
	}
	
	public static void labelDeclaration() {
		sy = lex.nextToken();
		if (sy != Symbol.ident) {
			src.error("Identifier expected for label definition");
		}
		symtable.add(lex.ident, SymbolType.label, DataType.labelType, src.currentLineNumber()+1);
		sy = lex.nextToken();
	}
	
	public static void variableDeclaration() {
		switch (sy) {
		case intsym:
			sy = lex.nextToken();
			if (sy != Symbol.ident)
				src.error("Identifier expected");
				symtable.add(lex.ident, SymbolType.variable, DataType.intType, 0);
				sy = lex.nextToken();
				break;
		case doublesym:
			sy = lex.nextToken();
			if (sy != Symbol.ident)
				src.error("Identifier expected");
				symtable.add(lex.ident, SymbolType.variable, DataType.doubleType, 0);
				sy = lex.nextToken();
				break;

		default: src.error("Data type not yet implemented.");
		}
	}
	
	public static PrintNode printStatement() {
		PrintNode head = new PrintNode();
		PrintNode tail;
		tail = head;
		head.left = head.right = null;
		sy = lex.nextToken();
		while (sy != Symbol.semicolon){
			tail.left = expression();
			if (sy==Symbol.comma){
				tail.right = new PrintNode();
				tail = (PrintNode)tail.right;
				tail.left = tail.right = null;
				sy = lex.nextToken();
			}
		}
		return head;
	}

	public static void gotoStatement() {
		String label;
		int lineNumber;
		sy = lex.nextToken();
		label = lex.ident;
		expect(Symbol.ident);
		lineNumber = (int)symtable.find(label).value;
		src.reset(lineNumber);
	}
	
	public static Node ifStatement() {
		IfNode root = new IfNode();
		sy = lex.nextToken();
		root.condition = expression();
		root.left =	statement();
		if (sy == Symbol.elsesym) {
			sy = lex.nextToken();
			root.right = statement();
		}
		return root;
		}
	
	public static Node whileStatement() {
		WhileNode root = new WhileNode();
		sy = lex.nextToken();
		expect(Symbol.lparen);
		root.condition = expression();
		expect(Symbol.rparen);
		root.left = statement();
		return root;
	}
	
	public static OpNode assignment() {
		String variableName = lex.ident;
		OpNode assn = new OpNode(Symbol.equals);
		sy = lex.nextToken();
		expect(Symbol.equals);
		Operand lvalue = new Operand();
		lvalue.link = symtable.find(variableName);
		lvalue.mode = Addrmode.absolute;
		assn.left = lvalue;
		assn.right= expression();
		return assn;
	}
	
	public static Node expression() {
		Node simpleExp;
		simpleExp = simpleExpression();
		switch (sy) {
		case equals:
			if (!(simpleExp instanceof Operand))
				src.error("Illegal l-value in an assignment");
			sy = lex.nextToken();
			OpNode assnExp = new OpNode();
			assnExp.type = DataType.intType;;
			assnExp.operator = Symbol.equals;
			assnExp.left = simpleExp;
			assnExp.right= expression();
			return assnExp;
		case equ:
		case lss:
		case gtr:
		case leq:
		case geq:
		case neq:
			OpNode relationalExp = new OpNode();
			relationalExp.type = DataType.intType;
			relationalExp.operator = sy;
			sy = lex.nextToken();
			relationalExp.left = simpleExp;
			relationalExp.right= simpleExpression();
			return relationalExp;
		default:
			break;
		}
		return simpleExp;
	}
	
	public static Node simpleExpression() {
		Node root,firstTerm;
		root = term();
		while (sy == Symbol.plus || sy == Symbol.minus)
		{
			Symbol operator = sy;
			sy = lex.nextToken();
			switch (operator){
			case plus:
				firstTerm = root;
				root = new OpNode(Symbol.plus);
				root.type = firstTerm.type;
				root.left  = firstTerm;
				root.right = term();
				break;
			case minus:
				firstTerm = root;
				root = new OpNode(Symbol.minus);
				root.type = firstTerm.type;
				root.left  = firstTerm;
				root.right = term();
				break;
			default:
				break;
			}
			if (root.left.type != root.right.type)
				src.error("Type missmatch of operands");
		}
		return root;
	}
	
	public static Node term() {
		Node root,firstTerm;
		root = factor();
		while (sy == Symbol.mult || sy == Symbol.divd)
		{
			firstTerm = root;
			root = new OpNode(sy);
			root.type = firstTerm.type;
			sy = lex.nextToken();
			root.left = firstTerm;
			root.right = factor();
			if (root.left.type != root.right.type)
				src.error("Type missmatch of operands");
		}
		return root;
	}
	
	public static Node factor() {
		Node root = null;
		Operand operand;
		switch (sy) {
		case ident:
			operand = new Operand();
			//operand.value = symtable.load(lex.ident);
			operand.link = symtable.find(lex.ident);
			if (operand.link == null)
			{
				src.error("Unknown identifier, not found in symbol table");
				return null;
			}
			operand.mode = Addrmode.absolute;
			operand.kind = operand.link.kind;
			operand.value= (int)operand.link.value;
			operand.type = operand.link.type;
			sy = lex.nextToken();
			
			return operand;
		case lparen:
			sy = lex.nextToken();
			root = expression();
			expect(Symbol.rparen);
			return root;
		case intnum:
			operand = new Operand();
			operand.value = lex.inum;
			operand.kind = SymbolType.constant;
			operand.type = DataType.intType;
			operand.mode = Addrmode.immediate;
			operand.link = null;
			sy = lex.nextToken();
			return operand;
		case realnum:
			operand = new Operand();
			operand.value = (int)lex.rnum;
			operand.kind = SymbolType.constant;
			operand.type = DataType.doubleType;
			operand.mode = Addrmode.immediate;
			operand.link = null;
			sy = lex.nextToken();
			return operand;	
		case literal: 
			LiteralNode lit_operand = new LiteralNode();
			lit_operand.literal = lex.literal;
			sy = lex.nextToken();
			return lit_operand;
		default:
			src.error("Unrecognized item in an expression");
		}
		return root;
	}
}
