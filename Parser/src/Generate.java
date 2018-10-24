
public class Generate {

	public static void listProgram(Instruction head) {
		Instruction ip = head;
		while (ip!=null) {
			if (ip.label!=null) System.out.print(ip.label.getLabel()+":  ");
			else System.out.print("       ");
			if (ip.opcode != Instructions.nocode) {
				System.out.print(ip.opcode);
				if (ip.opcode != Instructions.pushl)
					System.out.print(" ");
			}
			System.out.print("   ");
			if (ip.operand1!=null) System.out.print(ip.operand1.getString());
			if (ip.operand2!=null) System.out.print(","+ip.operand2.getString());
			if (ip.lineNumber >0) System.out.print("       // **** "+Parser.src.getLine(ip.lineNumber));
			System.out.println();
			ip = ip.next;
		}
	}
}
