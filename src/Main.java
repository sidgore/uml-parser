
public class Main {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		// String test="";
String input="/Users/sidgore/Downloads/downloads/paul 202 github/umlparser/uml-parser-test-1";
		JavaToString obj = new JavaToString(input);
		String code= obj.execute();

		System.out.println(code);
		
		String output = input + "/" + "diag.png";
		System.out.println(output);
		StringToUmlParser t = new StringToUmlParser();
		t.createDiagram(code, output);
		// System.out.println(y);
	}
}
