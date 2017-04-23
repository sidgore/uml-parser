import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;

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
	
	
	
	public void execute() {
		File dir = new File(input);
		// int count = 0;
		File[] files = dir.listFiles();
		CompilationUnit complilationUnit = null;
		FileInputStream in = null;
		code = "[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
		System.out.println("Input Path: " + input);
		if (files.length > 0) {
			System.out.println("Java file parsing in progress...");
			for (int i = 0; i < files.length; i++) {

				if (files[i].getName().endsWith(".java")) {
					try {
						in = new FileInputStream(files[i]);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						complilationUnit = JavaParser.parse(in);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					linkedList.add(complilationUnit);

					// System.out.println(cu);
					// System.out.println(files[i].toString());

					// count++;
					// System.out.println(count);

				}

				// System.out.println(count);
			}
			// System.out.println(count);
		}
		// List<TypeDeclaration> a=cu.getTypes();
		// ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration)
		// a.get(0);
		// map.put(coi.getName(), coi.isInterface());
		for (CompilationUnit c : linkedList) {
			List<TypeDeclaration> Nodes = c.getTypes();
			for (Node n : Nodes) {
				ClassOrInterfaceDeclaration ClassInterName = (ClassOrInterfaceDeclaration) n;
				map.put(ClassInterName.getName(), ClassInterName.isInterface());

			}
		}

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
			className = className + coii.getName();
			classShortName = coii.getName();

			code = code + className;
			System.out.println(code);

			int nextParam = 0;

			String plus = null;
			for (BodyDeclaration b : ((TypeDeclaration) node).getMembers()) {

				if (b instanceof ConstructorDeclaration) {
					ConstructorDeclaration cd = ((ConstructorDeclaration) b);
					if (cd.getDeclarationAsString().startsWith("public") && !coii.isInterface()) {
						if (nextParam == 0)
							operations += ";";
						operations += "+ " + cd.getName() + "(";
						for (Object gcn : cd.getChildrenNodes()) {
							if (gcn instanceof Parameter) {
								Parameter paramCast = (Parameter) gcn;
								String paramClass = paramCast.getType().toString();
								String paramName = paramCast.getChildrenNodes().get(0).toString();
								operations = operations + paramName + " : " + paramClass;
								if (map.containsKey(paramClass) && !map.get(classShortName)) {
									plus = plus + "[" + classShortName + "] uses -.->";
									if (map.get(paramClass))
										plus = plus + "[<<interface>>;" + paramClass + "]";
									else
										plus = plus + "[" + paramClass + "]";
								}
								plus = plus + ",";
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
							if (nextParam == 1)
								operations = operations + ";";
							operations = operations + "+ " + md.getName() + "(";
							for (Object gcn : md.getChildrenNodes()) {
								if (gcn instanceof Parameter) {
									Parameter paramCast = (Parameter) gcn;
									String paramClass = paramCast.getType().toString();
									String paramName = paramCast.getChildrenNodes().get(0).toString();
									operations = operations + paramName + " : " + paramClass;
									if (map.containsKey(paramClass) && !map.get(classShortName)) {
										plus = plus + "[" + classShortName + "] uses -.->";
										if (map.get(paramClass))
											plus = plus + "[<<interface>>;" + paramClass + "]";
										else
											plus = plus + "[" + paramClass + "]";
									}
									plus = plus + ",";
								} else {
									String methodBody[] = gcn.toString().split(" ");
									for (String foo : methodBody) {
										if (map.containsKey(foo) && !map.get(classShortName)) {
											plus = plus + "[" + classShortName + "] uses -.->";
											if (map.get(foo))
												plus = plus + "[<<interface>>;" + foo + "]";
											else
												plus = plus + "[" + foo + "]";
											plus = plus + ",";
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

		// for (String key : map.keySet()) {
		// System.out.println(key + " " + map.get(key));
		// }
		System.out.println("Unique Code: " + code);
		// System.out.println("Out path: " + output);

	}

}
