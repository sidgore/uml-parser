
public class Main {

	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		// String test="";
//String input="/Users/sidgore/Downloads/downloads/paul 202 github/umlparser/uml-parser-test-4";
		 if (args[0].equals("class")) {
		JavaToString obj = new JavaToString(args[0]);
		String code= obj.execute();

		System.out.println(code);
		
		String output = args[0] + "/" + "diag.png";
		System.out.println(output);
		StringToUmlParser t = new StringToUmlParser();
		t.createDiagram(code, output);
		 }
		 else {
			 System.out.println("Invalid Entry!!!");
		 }
		 
		// System.out.println(y);
	}
}
