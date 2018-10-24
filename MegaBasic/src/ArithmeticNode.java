
class ArithmeticNode {
	Token operator;		// The arithmetic operator
	double value;
	SymbolEntry ptr;
	ArithmeticNode left, right;	// Links to the operands
}
