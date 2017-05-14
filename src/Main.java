
public class Main {

	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		// String test="";
		String input = "/Users/sidgore/Downloads/downloads/paul-202-github/umlparser/uml-sequence-test";
		//String output="seqdiag";
		String FunctionName="main";
		String ClassName="Main";
		// if (args[0].equals("class")) {
		// System.out.println(args[1]);
		
		
		//JavaToString obj = new JavaToString(input);
        //String code = obj.execute();

		//ParseSeqEngine seq=new ParseSeqEngine( input,ClassName, FunctionName, output) ;
		SequenceDiagram seq=new SequenceDiagram( input,ClassName, FunctionName) ;
		String code=seq.execute(input);
		String output = input + "/seqdiag.png";
		SequenceHelper seqhelp=new SequenceHelper();
		seqhelp.generateSequenceDiagram(code, output);
		
		System.out.println(output);
		//StringToUmlParser t = new StringToUmlParser();
		//t.createDiagram(code, output);
		// }
		// else {
		// System.out.println("Invalid Entry!!!");
		// }

		// System.out.println(y);
	}
}
