
public class SymbolTable {
	TableEntry[] table = new TableEntry[100];
	int tableSize=0;

	public void add(String ident, SymbolType kind, DataType type, double value) {
		for (int i=0; i<tableSize; i++)
			if (ident.equals(table[i].name))
			{
					System.out.println("Error: Symbol already defined.");
					System.exit(1);
				return;
			}
		table[tableSize] = new TableEntry();
		table[tableSize].name = ident;
		table[tableSize].kind = kind;
		table[tableSize].type = type;
		table[tableSize].value = value;
		tableSize++;
		return;
	}
	
	public TableEntry find(String ident) {
		for (int i=0; i<tableSize; i++)
			if (ident.equals(table[i].name))
			{
				return table[i];
			}
		System.out.println("Error, identifier "+ident+" does not exist!");
		//src.error("Undeclared variable: "+ident);
		return null;
	}


	public void store(String ident, double value) {
		for (int i=0; i<tableSize; i++)
			if (ident.equals(table[i].name))
			{
				if (table[i].kind != SymbolType.variable)
				{
					System.out.println("Cannot store a value in something that is not a variable");
					System.exit(1);
				}
				table[i].value = value;
				//System.out.println("Storing: "+ident+" = "+value+" : "+i);
				return;
			}
		table[tableSize] = new TableEntry();
		table[tableSize].name = ident;
		table[tableSize].value = value;
		System.out.println("Creating: "+ident+" = "+value+" : "+tableSize);
		tableSize++;
		return;
	}

	public double load(String ident) {
		for (int i=0; i<tableSize; i++)
			if (ident.equals(table[i].name))
			{
				System.out.println("Loading: "+ident+" = "+table[i].value+" : "+i);
				return table[i].value;
			}
		System.out.println("Error, variable "+ident+" does not exist!");
		//src.error("Undeclared variable: "+ident);
		return 0.0;
	}

	public void print() {
		System.out.println("\n\nSymbol Table");
		System.out.println();
		System.out.println("Name   Value");
		System.out.println("____   _____");
		for (int i=0; i<tableSize; i++)
		{
			System.out.println(table[i].name+":     "+table[i].value);
		}
	}

	public class TableEntry {
		String name;
		SymbolType kind;
		DataType type;
		double value;
	}
}
