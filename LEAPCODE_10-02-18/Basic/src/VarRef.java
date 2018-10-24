
public class VarRef {
	private static Value[] variables = new Value[256];
	
	DataType type = DataType.noType;
	Value val;
	
	public VarRef(String ident) {
		char ch = ident.charAt(0);
		val = variables[ch-'A'];
		this.type = val.type;
	}
	
	
	
}
