

public class Main {

public static void main(String[] args) throws Exception{
	//String test="";
	
	JavaToString obj = new JavaToString("/Users/sidgore/Downloads/downloads/paul 202 github/umlparser/uml-parser-test-1", "diagram");
    // obj.execute();
     
     
	System.out.println("Hello World");
	StringToUmlParser t=new StringToUmlParser();
	boolean y=t.createDiagram("ram","Shyam");
	System.out.println(y);
}	}
