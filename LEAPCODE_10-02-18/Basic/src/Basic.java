
public class Basic {
	public static boolean debug=false;
	
	public static void main(String[] args) {
		Token tok;
		SourceCodeReader src = new SourceCodeReader("text.txt");
		LexicalAnalyzer lex = new LexicalAnalyzer(src);
		Parser parser = new Parser(lex);

		System.out.println("Start parsing");
		parser.parse();
	}

}
