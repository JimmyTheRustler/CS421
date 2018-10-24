
public class Parser {
	Lexan lex;
	Token tok;
	final boolean debug = false;
	SymbolTable symtable = new SymbolTable();

	public Parser(Lexan lex) {
		this.lex = lex;
		tok = lex.next();
	}

	public void program() {
		if (debug) System.out.println("Program "+tok);
		while (tok==Token.Print || tok==Token.Ident 
				|| tok==Token.Ifsym || tok==Token.eol) {
			stmt();
		}
		expect(Token.eof);
	}

	public void stmt() {
		if (debug) System.out.println("Statement "+tok);
		switch (tok) {
		case Ifsym:
			if (debug) System.out.println("Found If symbol");
			ifStmt();
			break;
		case Print:
			printStmt();
			break;
		case Ident:
			assignment();
			break;
		case eol:
			tok = lex.next();
			break;
		default:
			error("Illegal beginning of a statement");	
		}
		//expect(Token.eol);
	}

	public void ifStmt() {
		ArithmeticNode value = null;
		tok = lex.next();
		expect(Token.Lparen);
		//value = expression();
		expect(Token.Rparen);
		if (debug) System.out.println("If expression = "+value);
		//		if (value!=0) {
		//			stmt();
		//			//src.pos = 0;
		//		}
		//else
		while (tok != Token.eol) tok = lex.next();
	}

	public void printStmt() {
		double value;
		expect(Token.Print);
		if (tok==Token.Literal) {
			System.out.println(lex.getLiteral());
			tok = lex.next();
		}
		else {
			//value = expression();
			//System.out.println(value);
		}
	}

	public void assignment() {
		AssignmentStmt stmt = new AssignmentStmt();
		SymbolEntry p;
		p=varRef();
		if (p==null) p=symtable.add(lex.getIdent());
		expect(Token.Equal);
		stmt.expression = expression();
	}

	public ArithmeticNode expression() {
		ArithmeticNode root;
		ArithmeticNode p;
		root = term();
		while (tok==Token.Plus || tok==Token.Minus) {
			p = new ArithmeticNode();
			p.operator = tok;
			tok = lex.next();
			p.left = root;
			p.right = term();
			root = p;
		}
		return root;
	}

	public ArithmeticNode term() {
		ArithmeticNode root, p;
		root = factor();
		while (tok==Token.Mult || tok==Token.Divd) {
			p = new ArithmeticNode();
			p.operator = tok;
			p.left = root;
			tok = lex.next();
			p.right = factor();
			root = p;
		}
		return root;
	}

	public ArithmeticNode factor() {
		ArithmeticNode operand = null;
		switch (tok) {
		case Integer:
			operand = new ArithmeticNode();
			operand.operator = Token.Constant;
			operand.value = lex.getRnum();
			tok = lex.next();
			break;
		case Ident:
			operand = new ArithmeticNode();
			operand.operator = Token.Operand;
			operand.ptr = varRef();
			break;
		case Lparen:
			tok = lex.next();
			operand = expression();
			expect(Token.Rparen);
			break;
		default:
			error("Illegal token in an expression");
		}
		return operand;
	}

	public SymbolEntry varRef() {
		SymbolEntry p;
		p = symtable.find(lex.getIdent());
		if (p==null)
			p = symtable.add(lex.getIdent());
		expect(Token.Ident);
		return p;
	}

	public void expect(Token sy) {
		if (debug) System.out.println("Expect "+sy+ "  Found "+tok);
		if (tok == sy) {
			tok = lex.next();
		} else 
			error("Expected "+sy+", not found");
	}

	public void error(String errmsg) {
		System.out.println("Error! "+errmsg);
	}

}
