
public class SymbolTable {
	Variable[] table = new Variable[100];
	int variablecount=0;
	
	public Variable newVariable(String name) {
		Variable var = new Variable(name);
		table[variablecount++] = var;
		return var;
	}
	
	public Variable findVariable(String name) {
		int i;
		for (i=0; i<variablecount; i++)
			if (name.equals(table[i].name))
				return table[i];
		return null;
	}
}
