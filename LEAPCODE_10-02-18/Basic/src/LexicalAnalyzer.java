public class LexicalAnalyzer {
	SourceCodeReader src;
	char ch=' ';
	TokenType[] tokens = new TokenType[128];
	String[] reswrd = {"print","input","if","else","while","for"};
	TokenType[] ressym= {TokenType.PRINT,TokenType.INPUT,TokenType.IF,TokenType.ELSE,
			TokenType.WHILE,TokenType.FOR};


	public LexicalAnalyzer(SourceCodeReader s) {
		src = s;
		for (int i=0; i<128; i++) tokens[i] = TokenType.NOSYM;
		for (int i='A'; i<='Z'; i++) tokens[i] = TokenType.IDENT;
		for (int i='a'; i<='z'; i++) tokens[i] = TokenType.IDENT;
		for (int i='0'; i<='9'; i++) tokens[i] = TokenType.INTNUM;
		tokens['+'] = TokenType.PLUS ;	
		tokens['-'] = TokenType.MINUS ;	
		tokens['*'] = TokenType.MULT ;	
		tokens['/'] = TokenType.DIVD ;	
		tokens['='] = TokenType.ASSIGNMENT ;
		tokens[','] = TokenType.COMMA;
		tokens['"'] = TokenType.DOUBLEQUOTE;
		tokens[';'] = TokenType.SEMICOLON;
		tokens['('] = TokenType.LPAREN;
		tokens[')'] = TokenType.RPAREN;
		tokens[13] = TokenType.EOL;
		tokens[4] = TokenType.EOF ;	
	}

	public Token next() {
		Token tok = new Token();
		// Ignore White Space
		while (ch==' ' || ch=='\t')
			ch = src.getChar();
		// Check for and read a word (ident)
		tok.token = tokens[ch];
		tok.image = ""+ch;
		ch = src.getChar();	
		switch (tok.token){
		case IDENT:
			while (Character.isLetter(ch)||Character.isDigit(ch)) {
				tok.image+=ch;
				ch = src.getChar();
			}
			switch (tok.image) {
			case "stop":
				tok.token = TokenType.STOP;
				break;
			case "print":
				tok.token = TokenType.PRINT;
				break;
			case "input":
				tok.token = TokenType.INPUT;
				break;
			case "int":
				tok.token = TokenType.INT;
				break;
			}
			break;
			// Check for and read a number
		case INTNUM:
			tok.ival = tok.image.charAt(0)-'0';;
			while (Character.isDigit(ch)){
				tok.image+=ch;
				tok.ival = tok.ival*10 + (ch-'0');
				ch = src.getChar();
			}
			if (ch=='.') {
				tok.token = TokenType.REALNUM;
				tok.rval = tok.ival;
				tok.ival = 0;
				double divisor = 10.0;
				ch = src.getChar();
				while (Character.isDigit(ch)) {
					tok.rval += (ch-'0')/divisor;
					ch = src.getChar();
					divisor /= 10;
				}
			}
			break;
			// Check for and read punctuation
		case ASSIGNMENT:
			if (ch=='=') {
				tok.token = TokenType.EQUALS;
				ch = src.getChar();
			}
			break;
		case DIVD:
			// Look for and Ignore Comment
				if (ch=='*') {
					do {
						while (ch!='*') {
							ch = src.getChar();
						}
						ch = src.getChar();
					} while (ch!='/');
					ch = src.getChar();
					tok = next();
				}
			break;
		case DOUBLEQUOTE:
			tok.token = TokenType.LITERAL;
			tok.image = "";
			while (ch!='\"') {
				tok.image += ch;
				ch = src.getChar();
			}
			ch = src.getChar();
		default:
			break;
		}
		if (Basic.debug)
			System.out.println("Lex: "+tok.token);
		return tok;
	}
}

