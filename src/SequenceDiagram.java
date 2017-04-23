import java.util.HashMap;
import java.util.LinkedList;

import com.github.javaparser.ast.CompilationUnit;

public class SequenceDiagram {

	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	final String input;
	final String output;
	String code;
	HashMap<String, Boolean> map;
	HashMap<String, String> Connect;

	SequenceDiagram(String input, String output,String ClassName,String FunctionName) {
		
		
		
	
       
		map = new HashMap<String, Boolean>();
		Connect = new HashMap<String, String>();

		this.input = input;
		this.output = input + "/" + output + ".png";

		code = "";
	
	
	
	
	
}
