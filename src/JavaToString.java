
import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

import com.github.javaparser.ast.body.*;

public class JavaToString {
	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	final String input;
	final String output;
	String code;
	HashMap<String, Boolean> map;
	HashMap<String, String> Connect;

	JavaToString(String input, String output) {
		map = new HashMap<String, Boolean>();
		Connect = new HashMap<String, String>();

		this.input = input;
		this.output = input + "/" + output + ".png";

		code = "";
	}

	public void execute() {
		UMLHelper helper=new UMLHelper();
		
		int count = 0;

		FileInputStream in = null;
		linkedList=helper.javaParser(input);
	
	
		for (CompilationUnit c : linkedList) 
		
		helper.getClassorInterfaceMap(map, c);

		// add class names to code!!

		for (CompilationUnit c : linkedList) {
			String operations = "";

			String className = "";
			String classShortName = "";

			

			ArrayList<String> ConvertToPublic = new ArrayList<String>();
			List<TypeDeclaration> ltd = c.getTypes();
			Node node = ltd.get(0); // assuming no nested classes

			// Get className
			// for (Node n1 : ltd) {
			ClassOrInterfaceDeclaration coii = (ClassOrInterfaceDeclaration) node;

			if (coii.isInterface()) {
				className = "[" + "<<interface>>;";
			} else {
				className = "[";
			}
			className =className+ coii.getName();
			classShortName = coii.getName();

			code =code+ className;
			System.out.println(code);

			int nextParam = 0;

			String plus = null;
			for (BodyDeclaration b : ((TypeDeclaration) node).getMembers()) {
				
				if (b instanceof ConstructorDeclaration) {
					ConstructorDeclaration cd = ((ConstructorDeclaration) b);
					if (cd.getDeclarationAsString().startsWith("public") && !coii.isInterface()) {
						if (nextParam==0)
							operations += ";";
						operations += "+ " + cd.getName() + "(";
						for (Object gcn : cd.getChildrenNodes()) {
							if (gcn instanceof Parameter) {
								Parameter paramCast = (Parameter) gcn;
								String paramClass = paramCast.getType().toString();
								String paramName = paramCast.getChildrenNodes().get(0).toString();
								operations = operations + paramName + " : " + paramClass;
								if (map.containsKey(paramClass) && !map.get(classShortName)) {
									plus =plus+ "[" + classShortName + "] uses -.->";
									if (map.get(paramClass))
										plus =plus+ "[<<interface>>;" + paramClass + "]";
									else
										plus =plus+ "[" + paramClass + "]";
								}
								plus = plus+",";
							}
						}
						operations = operations + ")";
						nextParam = 1;
					}
				}
			}
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
				if (bd instanceof MethodDeclaration) {
					MethodDeclaration md = ((MethodDeclaration) bd);
					
					if (md.getDeclarationAsString().startsWith("public") && !coii.isInterface()) {
						
						if (md.getName().startsWith("set") || md.getName().startsWith("get")) {
							String varName = md.getName().substring(3);
							ConvertToPublic.add(varName.toLowerCase());
						} else {
							if (nextParam==1)
								operations = operations + ";";
							operations = operations + "+ " + md.getName() + "(";
							for (Object gcn : md.getChildrenNodes()) {
								if (gcn instanceof Parameter) {
									Parameter paramCast = (Parameter) gcn;
									String paramClass = paramCast.getType().toString();
									String paramName = paramCast.getChildrenNodes().get(0).toString();
									operations = operations + paramName + " : " + paramClass;
									if (map.containsKey(paramClass) && !map.get(classShortName)) {
										plus =plus+ "[" + classShortName + "] uses -.->";
										if (map.get(paramClass))
											plus =plus+ "[<<interface>>;" + paramClass + "]";
										else
											plus =plus+ "[" + paramClass + "]";
									}
									plus =plus+ ",";
								} else {
									String methodBody[] = gcn.toString().split(" ");
									for (String foo : methodBody) {
										if (map.containsKey(foo) && !map.get(classShortName)) {
											plus =plus+ "[" + classShortName + "] uses -.->";
											if (map.get(foo))
												plus =plus+ "[<<interface>>;" + foo + "]";
											else
												plus =plus+ "[" + foo + "]";
											plus =plus+ ",";
										}
									}
								}
							}
							operations = operations + ") : " + md.getType();
							nextParam = 1;
						}
					}
				}
			}

		}
	

		//for (String key : map.keySet()) {
		//	System.out.println(key + " " + map.get(key));
		//}
		System.out.println("Unique Code: " + code);
		// System.out.println("Out path: " + output);

	}

}
