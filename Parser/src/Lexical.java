
public class Lexical {
	/*
	public enum Symbol {nosym, ident, intnum, realnum, print, input, ifstmt, gotostmt,
		equals, plus, minus, mult, divd, semicolon, period, lparen, rparen, dblquote,
		comma, literal, rbrace,  lbrace, eoln, eof};
	 */
	String[] reswrd = {"else","goto","if","input","print","int","double","label","while"};
	Symbol[] ressym= {Symbol.elsesym,Symbol.gotostmt,Symbol.ifstmt,Symbol.input,
						Symbol.print,Symbol.intsym,Symbol.doublesym,Symbol.label,Symbol.whilestmt};
	Symbol[] firstchar = new Symbol[256];
	char ch = ' ';
	public String ident;
	public int  inum;
	public String literal;
	public double rnum;
	SourceHandler src;

	public Lexical(SourceHandler s) {
		int  i;
		for (i=0; i<256; i++) firstchar[i] = Symbol.nosym;
		for (i='A'; i<='Z'; i++) firstchar[i] = Symbol.ident;
		for (i='a'; i<='z'; i++) firstchar[i] = Symbol.ident;
		for (i='0'; i<='9'; i++) firstchar[i] = Symbol.intnum;
		firstchar['='] = Symbol.equals;
		firstchar['+'] = Symbol.plus;
		firstchar['-'] = Symbol.minus;
		firstchar['*'] = Symbol.mult;
		firstchar['/'] = Symbol.divd;
		firstchar['<'] = Symbol.lss;
		firstchar['>'] = Symbol.gtr;
		firstchar['!'] = Symbol.not;
		firstchar[';'] = Symbol.semicolon;
		firstchar['.'] = Symbol.period;
		firstchar[','] = Symbol.comma;
		firstchar['('] = Symbol.lparen;
		firstchar[')'] = Symbol.rparen;
		firstchar['"'] = Symbol.dblquote;
		firstchar['{'] = Symbol.lbrace;
		firstchar['}'] = Symbol.rbrace;
		firstchar['\26']  = Symbol.eof;
		firstchar['\n']= Symbol.eoln;

		//firstchar[''] = Symbols.;
		src = s;
		ch = src.nextch();
	}

	public Symbol nextToken() {
		Symbol token;
		while (ch==' ' || ch == '\t') ch = src.nextch();
		token = firstchar[ch];
		switch (token) {
		case ident:
			ident = "";
			while (firstchar[ch]==Symbol.ident || firstchar[ch]==Symbol.intnum)
			{
				ident += ch;
				ch = src.nextch();
			}
			for (int i=0; i<reswrd.length; i++)
				if (ident.equals(reswrd[i]))
				{
					token = ressym[i];
					break;
				}
			break;
		case intnum:
			inum = 0;
			while (firstchar[ch]==Symbol.intnum) {
				inum = inum*10 + ch-'0';
				ch = src.nextch();
			}
			rnum = inum;
			if (ch=='.')
			{
				token = Symbol.realnum;
				double divisor = 10.0;
				ch = src.nextch();
				while (firstchar[ch]==Symbol.intnum) {
					rnum += (ch-'0')/divisor;
					divisor *= 10;
					ch = src.nextch();
				}
			}
			break;
		case dblquote:
			ch = src.nextch();
			literal = "";
			while (ch != '"') {
				if (ch=='\\') {
					ch = src.nextch();
					if (ch=='n') ch = '\n';
					if (ch=='t') ch = '\t';
				}
				literal += ch;
				ch = src.nextch();
			}
			ch = src.nextch();
			token = Symbol.literal;
			break;
		case lss:
			ch = src.nextch();
			if (ch=='='){
				token = Symbol.leq;
				ch = src.nextch();
			}
			break;
		case gtr:
			ch = src.nextch();
			if (ch=='='){
				token = Symbol.geq;
				ch = src.nextch();
			}
			break;
		case not:
			ch = src.nextch();
			if (ch=='='){
				token = Symbol.neq;
				ch = src.nextch();
			}
			break;
		case equals:
			ch = src.nextch();
			if (ch=='='){
				token = Symbol.equ;
				ch = src.nextch();
			}
			break;
		default:
			ch = src.nextch();
		}
		return token;
	}

	public void reset() {
		src.reset();
		ch = src.nextch();

	}
}
