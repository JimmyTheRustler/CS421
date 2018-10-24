
public class Optimizer {

	public static void peepHole(Instruction head)
	{
		Instruction ip, ipp = null;
		ipp = head;
		ip = head.next;
		while (ip!=null) {
			if (ipp.opcode == Instructions.pushl && ip.opcode == Instructions.popl) {
				ipp.operand2 = ipp.operand1;
				ipp.operand1 = ip.operand1;
				ipp.opcode = Instructions.movl;
				ipp.next = ip.next;
				ip = ipp;
			}
			ipp = ip;
			ip = ip.next;
		}
	}
}
