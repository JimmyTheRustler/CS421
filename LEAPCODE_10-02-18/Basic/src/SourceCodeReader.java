import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SourceCodeReader {
	String filename;
	String[] src = new String[100];
	File srcfile;
	Scanner srcscn;
	int lineCount=0;
	int lineNumber=0;
	int linePos=0;
	
	public SourceCodeReader(String name) {	
		filename = name;
		srcfile = new File(filename);
		try {
			srcscn  = new Scanner(srcfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not find file: "+filename);
			System.exit(1);
		}
		while (srcscn.hasNext()) {
			src[lineCount++] = srcscn.nextLine();
		}
		srcscn.close();
	}

	public char getChar() {
		char ch;
		if (src[lineNumber]==null) return (char)4;
		if (linePos<src[lineNumber].length())
			return src[lineNumber].charAt(linePos++);
		linePos = 0;
		lineNumber++;
		ch = (char)13;	// Return and end of line character
		return ch;
	}
}
