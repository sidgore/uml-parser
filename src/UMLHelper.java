import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class UMLHelper {

	UMLHelper() {
	}

	public HashMap<String, Boolean> getClassorInterfaceMap(LinkedList<CompilationUnit> linkedList) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		for (CompilationUnit c : linkedList) {

			List<TypeDeclaration> Nodes = c.getTypes();
			for (Node n : Nodes) {
				ClassOrInterfaceDeclaration ClassInterName = (ClassOrInterfaceDeclaration) n;
				map.put(ClassInterName.getName(), ClassInterName.isInterface());

			}
		}
		return map;
	}

	String convertToString(LinkedList<CompilationUnit> linkedList, HashMap<String, String> Connect,
			HashMap<String, Boolean> map) {
		String result = "";
		for (CompilationUnit c : linkedList) {

			String className = "";
			String classShortName = "";
			String methods = "";
			String fields = "";
			String additions = ",";

			ArrayList<String> makeFieldPublic = new ArrayList<String>();
			List<TypeDeclaration> ltd = c.getTypes();
			Node node = ltd.get(0); // assuming no nested classes

			// Get className
			ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
			if (coi.isInterface()) {
				className = "[" + "<<interface>>;";
			} else {
				className = "[";
			}
			className += coi.getName();
			classShortName = coi.getName();

			// Parsing Methods
			boolean nextParam = false;
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
				// Get Methods
				if (bd instanceof ConstructorDeclaration) {
					ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
					if (cd.getDeclarationAsString().startsWith("public") && !coi.isInterface()) {
						if (nextParam)
							methods += ";";
						methods += "+ " + cd.getName() + "(";
						for (Object gcn : cd.getChildrenNodes()) {
							if (gcn instanceof Parameter) {
								Parameter paramCast = (Parameter) gcn;
								String paramClass = paramCast.getType().toString();
								String paramName = paramCast.getChildrenNodes().get(0).toString();
								methods += paramName + " : " + paramClass;
								if (map.containsKey(paramClass) && !map.get(classShortName)) {
									additions += "[" + classShortName + "] uses -.->";
									if (map.get(paramClass))
										additions += "[<<interface>>;" + paramClass + "]";
									else
										additions += "[" + paramClass + "]";
								}
								additions += ",";
							}
						}
						methods += ")";
						nextParam = true;
					}
				}
			}
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
				if (bd instanceof MethodDeclaration) {
					MethodDeclaration md = ((MethodDeclaration) bd);
					// Get only public methods
					if (md.getDeclarationAsString().startsWith("public") && !coi.isInterface()) {
						// Identify Setters and Getters
						if (md.getName().startsWith("set") || md.getName().startsWith("get")) {
							String varName = md.getName().substring(3);
							makeFieldPublic.add(varName.toLowerCase());
						} else {
							if (nextParam)
								methods += ";";
							methods += "+ " + md.getName() + "(";
							for (Object gcn : md.getChildrenNodes()) {
								if (gcn instanceof Parameter) {
									Parameter paramCast = (Parameter) gcn;
									String paramClass = paramCast.getType().toString();
									String paramName = paramCast.getChildrenNodes().get(0).toString();
									methods += paramName + " : " + paramClass;
									if (map.containsKey(paramClass) && !map.get(classShortName)) {
										additions += "[" + classShortName + "] uses -.->";
										if (map.get(paramClass))
											additions += "[<<interface>>;" + paramClass + "]";
										else
											additions += "[" + paramClass + "]";
									}
									additions += ",";
								} else {
									String methodBody[] = gcn.toString().split(" ");
									for (String foo : methodBody) {
										if (map.containsKey(foo) && !map.get(classShortName)) {
											additions += "[" + classShortName + "] uses -.->";
											if (map.get(foo))
												additions += "[<<interface>>;" + foo + "]";
											else
												additions += "[" + foo + "]";
											additions += ",";
										}
									}
								}
							}
							methods += ") : " + md.getType();
							nextParam = true;
						}
					}
				}
			}
			// Parsing Fields
			boolean nextField = false;
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
				if (bd instanceof FieldDeclaration) {
					FieldDeclaration fd = ((FieldDeclaration) bd);
					String fieldScope = aToSymScope(
							bd.toStringWithoutComments().substring(0, bd.toStringWithoutComments().indexOf(" ")));
					String fieldClass = changeBrackets(fd.getType().toString());
					String fieldName = fd.getChildrenNodes().get(1).toString();
					if (fieldName.contains("="))
						fieldName = fd.getChildrenNodes().get(1).toString().substring(0,
								fd.getChildrenNodes().get(1).toString().indexOf("=") - 1);
					// Change scope of getter, setters
					if (fieldScope.equals("-") && makeFieldPublic.contains(fieldName.toLowerCase())) {
						fieldScope = "+";
					}
					String getDepen = "";
					boolean getDepenMultiple = false;
					if (fieldClass.contains("(")) {
						getDepen = fieldClass.substring(fieldClass.indexOf("(") + 1, fieldClass.indexOf(")"));
						getDepenMultiple = true;
					} else if (map.containsKey(fieldClass)) {
						getDepen = fieldClass;
					}
					if (getDepen.length() > 0 && map.containsKey(getDepen)) {
						String connection = "-";

						if (Connect.containsKey(getDepen + "-" + classShortName)) {
							connection = Connect.get(getDepen + "-" + classShortName);
							if (getDepenMultiple)
								connection = "*" + connection;
							Connect.put(getDepen + "-" + classShortName, connection);
						} else {
							if (getDepenMultiple)
								connection += "*";
							Connect.put(classShortName + "-" + getDepen, connection);
						}
					}
					if (fieldScope == "+" || fieldScope == "-") {
						if (nextField)
							fields += "; ";
						fields += fieldScope + " " + fieldName + " : " + fieldClass;
						nextField = true;
					}
				}

			}
			// Check extends, implements
			if (coi.getExtends() != null) {
				additions += "[" + classShortName + "] " + "-^ " + coi.getExtends();
				additions += ",";
			}
			if (coi.getImplements() != null) {
				List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) coi.getImplements();
				for (ClassOrInterfaceType intface : interfaceList) {
					additions += "[" + classShortName + "] " + "-.-^ " + "[" + "<<interface>>;" + intface + "]";
					additions += ",";
				}
			}
			// Combine className, methods and fields
			result += className;
			if (!fields.isEmpty()) {
				result += "|" + changeBrackets(fields);
			}
			if (!methods.isEmpty()) {
				result += "|" + changeBrackets(methods);
			}
			result += "]";
			result += additions;
		}
		return result;

	}

	private String changeBrackets(String foo) {
		foo = foo.replace("[", "(");
		foo = foo.replace("]", ")");
		foo = foo.replace("<", "(");
		foo = foo.replace(">", ")");
		return foo;
	}

	private String aToSymScope(String stringScope) {
		switch (stringScope) {
		case "private":
			return "-";
		case "public":
			return "+";
		default:
			return "";
		}
	}

	public String parsingextended(HashMap<String, String> Connect, HashMap<String, Boolean> map) {
		String result = "";
		Set<String> keys = Connect.keySet(); // get all keys
		for (String i : keys) {
			String[] classes = i.split("-");
			if (map.get(classes[0]))
				result += "[<<interface>>;" + classes[0] + "]";
			else
				result += "[" + classes[0] + "]";
			result += Connect.get(i); // Add connection
			if (map.get(classes[1]))
				result += "[<<interface>>;" + classes[1] + "]";
			else
				result += "[" + classes[1] + "]";
			result += ",";
		}
		return result;
	}

	public LinkedList<CompilationUnit> javaParser(String input) {
		LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();

		File dir = new File(input);

		File[] files = dir.listFiles();
		CompilationUnit complilationUnit = null;
		FileInputStream in = null;
		// code =
		// "[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order]
		// [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
		System.out.println("Input Path: " + input);
		if (files.length > 0) {
			System.out.println("Java file parsing in progress...");
			for (int i = 0; i < files.length; i++) {

				if (files[i].getName().endsWith(".java")) {
					try {
						in = new FileInputStream(files[i]);

						complilationUnit = JavaParser.parse(in);
						linkedList.add(complilationUnit);

						in.close();

					} catch (Exception s) {

						s.printStackTrace();
					}
				}

			}
		}

		return linkedList;
	}

}
