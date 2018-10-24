import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SourceHandler {
	private String filename;
	private File in;
	private Scanner infile;
	private String[] srcCode;
	int lineCount = 0;
	int charIndex = 0;
	
	public SourceHandler(String fn) {
		filename = fn;
		in = new File(filename);
		try {
			infile = new Scanner(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (infile.hasNext()) {
			infile.nextLine();
			lineCount++;
		}
		infile.close();
		srcCode = new String[lineCount];
		try {
			infile = new Scanner(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lineCount = 0;
		while (infile.hasNext()) {
			srcCode[lineCount] = infile.nextLine();
			System.out.println(""+lineCount+": "+srcCode[lineCount]);
			lineCount++;
		}
		infile.close();
		lineCount = 0;
		charIndex = 0;
	}
	
	public char nextch() {
		if (lineCount >= srcCode.length) return '\26';
		if (charIndex >= srcCode[lineCount].length())
		{
			lineCount++;
			if (lineCount >= srcCode.length) return '\26';
			charIndex = 0;
			return '\n';
		}
		else
			return srcCode[lineCount].charAt(charIndex++);
	}
	
	public int currentLineNumber() {
		return lineCount;
	}
	
	public String getLine(int line) {
		return srcCode[line];
	}
	
	public void reset() {
		lineCount = 0;
		charIndex = 0;
	}
	
	public void reset(int linenum) {
		lineCount = linenum;
		charIndex = 0;
	}
	
	public void error(String message) {
		System.out.println(""+lineCount+": "+srcCode[lineCount]);
		System.out.println("Error on line "+lineCount+": "+message);
		System.exit(1);
	}
}
