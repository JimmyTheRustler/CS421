import java.io.FileNotFoundException;

public class LeapFrog {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		SrcReader src = new SrcReader("test.txt");
		Lexan lex = new Lexan(src);
		parser Parser = new parser(lex);
		char ch;
		Token sy;
		
		Parser.trump();
		
		
		//comment out all other code
		
		/*
		ch = src.nextch();

		while (ch!= SrcReader.eof) {
			System.out.print(ch);
			ch = src.nextch();
		}
		System.out.println("End-of-file");
		*/
		sy = lex.next();
		while (sy!=Token.eof) {
			System.out.print(sy);
			if (sy==Token.Integer) System.out.print(": "+lex.getInum());
			if (sy==Token.Real) System.out.print(": "+lex.getRnum());
			if (sy==Token.Ident) System.out.print(": "+lex.getIdent());
			System.out.println();
			sy = lex.next();
		}
	}
	/*
	 ~~~IN-CLASS NOTES~~~
	 
	public void SimpleExp() {
		Term();
		if(tok == Token.Plus) {
			tok = lex.next();
		}
		
	}
	
	public void A() {
		Expect(Token.Sym1);
		Expect(Token.Sym2);
		Expect(Token.Sym3);
	}
	public void expect(Token etok) {
		if(tok == etok) {
			tok = lex.next();
			return;
		}
		else {
			throw(Exception(etok+"Expected"));
		}
	}
	*/

}
