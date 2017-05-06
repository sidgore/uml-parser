
import java.util.*;

import com.github.javaparser.ast.*;

public class JavaToString {
	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	final String input;

	String code;
	HashMap<String, Boolean> map;
	HashMap<String, String> Connect;

	JavaToString(String input) {
		map = new HashMap<String, Boolean>();
		Connect = new HashMap<String, String>();

		this.input = input;

		code = "";
	}

	public String execute() {
		UMLHelper helper = new UMLHelper();

		linkedList = helper.javaParser(input);
		map = helper.getClassorInterfaceMap(linkedList);

		code = code + helper.convertToString(linkedList, Connect, map)+helper.parsingextended(Connect, map);
		//code = code + helper.parsingextended(Connect, map);
		// code += parseAdditions();
		code = yumlCodeUniquer(code);
		// for (String key : map.keySet()) {
		// System.out.println(key + " " + map.get(key));
		// }
		System.out.println("Unique Code: " + code);
		// System.out.println("Out path: " + output);
		return code;
	}

	private String yumlCodeUniquer(String code) {
		String[] codeLines = code.split(",");
		System.out.println("UniqueRRR: " + code);
		String[] uniqueCodeLines = new LinkedHashSet<String>(Arrays.asList(codeLines)).toArray(new String[0]);
		String result = String.join(",", uniqueCodeLines);
		return result;
	}

}
