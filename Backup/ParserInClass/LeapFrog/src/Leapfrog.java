import java.io.FileNotFoundException;

public class Leapfrog {

	public static void main(String[] args) throws FileNotFoundException {
		SrcReader src = new SrcReader("test.txt");
		Lexan lex = new Lexan(src);
		char ch;
		Token sy;
		ch = src.nextch();
		
//		while(ch != src.eof) {
//			System.out.print(ch);
//			ch = src.nextch();
//		}
		
		sy = lex.next();
		while(sy != Token.eof) {
			System.out.print(sy + " ");
			if(sy == Token.Number) System.out.println(": "+ lex.getInum());
			sy = lex.next();
		}
		System.out.println("\nEnd Of File");
	}

}
