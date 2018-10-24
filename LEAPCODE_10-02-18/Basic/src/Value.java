
public class Value {
	
	DataType type = DataType.noType;
	int    intval  = 0;
	double realval = 0.0;
	String literal = "";
	
	public Value(int intval) {
		type = DataType.intType;
		this.intval = intval;
	}
	
	public Value(VarRef ref) {
		type = ref.type;
		this.intval = ref.val.intval;
	}
}
