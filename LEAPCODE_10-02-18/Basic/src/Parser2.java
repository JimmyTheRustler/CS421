
public class Parser2 {
	LexicalAnalyzer lex;
	Token tok;

	public Parser2(LexicalAnalyzer lex) {
		this.lex = lex;
		tok = lex.next();

	}

	public void parse() {
		program();
	}

	public void program() {
		Basic.debug = false;
		while (tok.token!=TokenType.EOF) {
			if (Basic.debug) System.out.println(tok.token);
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
			tok = lex.next();
			assign();
			break;
		case STOP:
			tok = lex.next();			
			stopStmt();
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
		expect(TokenType.ASSIGNMENT);
		expression();
	}

	private void inputStmt() {
		variable();	
	}

	private VarRef variable() {
		VarRef ref = new VarRef(tok.image);
		expect(TokenType.IDENT);
		return ref;
	}

	private void printStmt() {
		switch (tok.token) {
		case INTNUM:
			/* System.out.print("  "+tok.ival);
			tok = lex.next();
			break;
			*/
		case LPAREN:
		case IDENT:
		case REALNUM:
			Value val;
			val = expression();
			System.out.print(val.intval);
			while (tok.token==TokenType.SEMICOLON) {
				tok = lex.next();
				val = expression();
				System.out.print("   "+val.intval);
			}
			break;
		default:
			System.out.println("Error - Illegal token in print statement");
			break;
		}
		if (tok.token==TokenType.SEMICOLON)
			tok = lex.next();
		else
			System.out.println();

	}

	private Value expression() {
		Value result = null;
		result = term();
		while(tok.token==TokenType.PLUS || tok.token==TokenType.MINUS) {
			switch ( tok.token) {
			case PLUS:
				tok = lex.next();
				result.intval = result.intval + term().intval;
				break;
			case MINUS:
				tok = lex.next();
				result.intval = result.intval - term().intval;
				break;
			default:
				/*  This should never happen */
			}

		}
		return result;
	}

	private Value term() {
		Value result = null;
		result = factor();
		switch ( tok.token) {
		case MULT:
			tok = lex.next();
			result.intval = result.intval * term().intval;
			break;
		case DIVD:
			tok = lex.next();
			result.intval = result.intval / term().intval;
			break;
		default:
			/*  This should never happen */
		}
		return result;

	}

	private Value factor() {
		Value result = null;
		switch (tok.token) {
		case IDENT:
			VarRef ref;
			ref = variable();
			result = new Value(ref);
			break;
		case INTNUM:
			result = new Value(tok.ival);
			expect(TokenType.INTNUM);
			break;
		case LPAREN:
			expect(TokenType.LPAREN);
			result = expression();
			expect(TokenType.RPAREN);
			break;
		}
		return result;
	}


}
