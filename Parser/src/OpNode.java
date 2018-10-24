
public class OpNode extends Node{

		public Symbol operator;
		
		public OpNode() {
			operator = Symbol.nosym;
		}
		public OpNode(Symbol operator) {
			this.operator = operator;
		}
}
