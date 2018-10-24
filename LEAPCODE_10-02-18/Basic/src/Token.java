public class Token {

	public TokenType token;
	public String image;
	public int ival;
	public double rval;
	public int line;
	public int pos;
	Token next;
	
	public Token() {
		token =TokenType.NOSYM;
		image = "";
		next = null;
	}
}
