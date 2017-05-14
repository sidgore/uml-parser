
public class Main {

	@SuppressWarnings("static-access")
	public static void main(String args[]) throws Exception {
		// String test="";
		// String input =
		// "/Users/sidgore/Desktop/workspace/uml-parser/Test-Cases/uml-sequence-test";
		// String output="seqdiag";
		// String FunctionName="main";
		// String ClassName="Main";
		if (args[0].equals("class")) {
			System.out.println(args[1]);

			String output = args[1] + "/" + args[2] + ".png";
			JavaToString obj = new JavaToString(args[1]);
			String code = obj.execute();
			StringToUmlParser t = new StringToUmlParser();
			t.createDiagram(code, output);

		} else if (args[0].equals("sequence")) {
			String output = args[1] + "/" + args[4] + ".png";
			SequenceDiagram seq = new SequenceDiagram(args[1], args[2], args[3]);
			String code = seq.execute(args[1]);

			SequenceHelper seqhelp = new SequenceHelper();
			seqhelp.generateSequenceDiagram(code, output);

			System.out.println(output);
		} else
			System.out.println("Invalid entries!!");

	}
}
