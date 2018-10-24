
public class Label {
	private static int counter = 1;
	
	public int labelnumber = 0;
	private String labelname = "";
	
	public Label() {
		labelnumber = counter++;
		labelname = "L"+labelnumber;
	}
	
	public Label(String fixed) {
		labelname = fixed;
	}
	
	public String getLabel() {
		return labelname;
	}

}
