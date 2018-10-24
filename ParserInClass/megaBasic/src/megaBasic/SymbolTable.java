
public class SymbolTable {
	SymbolEntry[] table = new SymbolEntry[26];
	
	public SymbolTable() {
		for (int i=0; i<table.length; i++)
			table[i]=null;
	}
	
	public SymbolEntry add(String name) {
		SymbolEntry p;
		p = find(name);
		if (p==null)
			p = new SymbolEntry();
		else
			return p;
		p.name = name;
		p.value = 0.0;
		table[(name.charAt(0)-'A')%table.length] = p;
		return p;
	}
	
	public SymbolEntry find(String name) {
		SymbolEntry p;
		p = table[(name.charAt(0)-'A')%table.length];
		if (p==null) return p;
		if (p.name.equals(name)) return p;
		return null;
	}
}
