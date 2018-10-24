
public class Parser {
	LexicalAnalyzer lex;
	Token tok;
	SymbolTable symboltable = new SymbolTable();
	
	public Parser(LexicalAnalyzer lex) {
		this.lex = lex;
		tok = lex.next();

	}
	
	public void parse() {
		program();
	}
	
	public void program() {
		while (tok.token!=TokenType.EOF) {
			System.out.println(tok.token);
			stmt();
			if (tok.token != TokenType.EOL) {
				System.out.println("You forgot to end the line");
				while (tok.token != TokenType.EOL && tok.token!=TokenType.EOF)
					tok = lex.next();
			}	
			tok = lex.next();
		}	
		System.out.println("Program Complete");
	}
	
	public void stmt() {
		switch (tok.token) {
		case PRINT:
			tok = lex.next();
			printStmt();
			break;
		case INPUT:
			tok = lex.next();
			inputStmt();
			break;
		case IDENT:
			assign();
			break;
		case STOP:
			tok = lex.next();			
			stopStmt();
			break;
		case INT:
			tok = lex.next();
			intStmt();
			break;
		case EOL:				//  An end-of-line (EOL) indicates an empty or blank statement
			tok = lex.next();	//  This is necessary to allow blank lines
			break;
		default:
			System.out.println("Syntax error");
			System.exit(1);
		}
	}

	private void expect(TokenType token) {
		if (tok.token!=token) {
			System.out.println("Error! "+token+" expected, found "+tok.token);
			System.exit(1);
		}
		else
			tok = lex.next();
	}
	private void stopStmt() {
		// TODO Auto-generated method stub
		
	}

	private void assign() {
		Variable var;
		var = variable();
		expect(TokenType.ASSIGNMENT);
		var.intval = expression();
	}

	private void inputStmt() {
		variable();	
	}

	private Variable variable() {
		Variable var;
		if (tok.token!=TokenType.IDENT)
			error("Identifier expected as a variable name");
		var = symboltable.findVariable(tok.image);
		if (var==null)
			error("Undefined indentifier");
		tok = lex.next();
		return var;
	}

	private void printStmt() {
		int value;
		switch (tok.token) {
		case LPAREN:
		case IDENT:
		case INTNUM:
		case REALNUM:
			value = expression();
			System.out.print("  "+value);
			while (tok.token==TokenType.SEMICOLON) {
				tok = lex.next();
				value = expression();
				System.out.print("  "+value);
			}
			break;
		}
		System.out.println();	
	}

	private int expression() {
		int value=0;
		value = term();
		while(tok.token==TokenType.PLUS || tok.token==TokenType.MINUS) {
			if (tok.token==TokenType.PLUS) {
				tok = lex.next();
				value += term();
			}
			else {
				tok = lex.next();
				value -= term();
			}
		}
		return value;
	}

	private int term() {
		int value=0;
		value = factor();
		while(tok.token==TokenType.MULT || tok.token==TokenType.DIVD) {
			if (tok.token==TokenType.MULT) {
				tok = lex.next();
				value *= factor();
			}
			else {
				tok = lex.next();
				value /= factor();
			}
		}
		return value;
		
	}

	private int factor() {
		int value=0;
		switch (tok.token) {
		case IDENT:
			value = variable().intval;
			break;
		case INTNUM:
			value = tok.ival;
			tok = lex.next();
			break;
		case LPAREN:
			expect(TokenType.LPAREN);
			value = expression();
			expect(TokenType.RPAREN);
			break;
		}
		return value;
	}
	
	private void intStmt() {
		if (tok.token!=TokenType.IDENT)
			error("Expected and indentifier to be declared in the int statement");
		if (symboltable.findVariable(tok.image)!=null)
			error("Identifier already in use");
		symboltable.newVariable(tok.image);
	}
	
	private void error(String msg) {
		System.out.println("Error!  "+msg);
		System.exit(1);
	}
}
