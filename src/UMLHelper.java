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

	protected HashMap<String, Boolean> getClassorInterfaceMap(LinkedList<CompilationUnit> linkedList) {
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

	protected String convertToString(LinkedList<CompilationUnit> linkedList, HashMap<String, String> Connect,
			HashMap<String, Boolean> map) {
		String result = "";
		for (CompilationUnit c : linkedList) {

			String className = "";
			String classShortName = "";
			String methods = "";
			String attributes = "";
			String additions = ",";

			ArrayList<String> makeFieldPublic = new ArrayList<String>();
			List<TypeDeclaration> ltd = c.getTypes();
			Node node = ltd.get(0);

			ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) node;
			if (coi.isInterface()) {
				className = "[" + "<<interface>>;";
			} else {
				className = "[";
			}
			className = className + coi.getName();
			classShortName = coi.getName();

			boolean nAttrib = false;
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {

				if (bd instanceof ConstructorDeclaration) {
					ConstructorDeclaration cd = ((ConstructorDeclaration) bd);
					if (cd.getDeclarationAsString().startsWith("public") && !coi.isInterface()) {
						if (nAttrib) {
							methods = methods + ";";
						}

						methods = methods + "+ " + cd.getName() + "(";
						for (Object gcn : cd.getChildrenNodes()) {
							if (gcn instanceof Parameter) {
								Parameter paramCast = (Parameter) gcn;
								String paramClass = paramCast.getType().toString();
								String paramName = paramCast.getChildrenNodes().get(0).toString();
								methods = methods + paramName + " : " + paramClass;
								if (map.containsKey(paramClass) && !map.get(classShortName)) {
									additions = additions + "[" + classShortName + "] uses -.->";
									if (map.get(paramClass))
										additions = additions + "[<<interface>>;" + paramClass + "]";
									else
										additions = additions + "[" + paramClass + "]";
								}
								additions = additions + ",";
							}
						}
						methods = methods + ")";
						nAttrib = true;
					}
				}
			}
			for (BodyDeclaration b : ((TypeDeclaration) node).getMembers()) {
				if (b instanceof MethodDeclaration) {
					MethodDeclaration md = ((MethodDeclaration) b);

					if (md.getDeclarationAsString().startsWith("public") && !coi.isInterface()) {

						if (md.getName().startsWith("set") || md.getName().startsWith("get")) {
							String varName = md.getName().substring(3);
							makeFieldPublic.add(varName.toLowerCase());
						} else {
							if (nAttrib) {
								methods = methods + ";";
							}
							methods = methods + "+ " + md.getName() + "(";
							for (Object cnode : md.getChildrenNodes()) {
								if (cnode instanceof Parameter) {
									Parameter paramCast = (Parameter) cnode;
									String ClassName = paramCast.getType().toString();
									String attributeName = paramCast.getChildrenNodes().get(0).toString();
									methods = methods + attributeName + " : " + ClassName;
									if (map.containsKey(ClassName) && !map.get(classShortName)) {
										additions = additions + "[" + classShortName + "] uses -.->";
										if (map.get(ClassName))
											additions = additions + "[<<interface>>;" + ClassName + "]";
										else
											additions = additions + "[" + ClassName + "]";
									}
									additions = additions + ",";
								} else {
									String methodBody[] = cnode.toString().split(" ");
									for (String foo : methodBody) {
										if (map.containsKey(foo) && !map.get(classShortName)) {
											additions = additions + "[" + classShortName + "] uses -.->";
											if (map.get(foo))
												additions = additions + "[<<interface>>;" + foo + "]";
											else
												additions = additions + "[" + foo + "]";
											additions = additions + ",";
										}
									}
								}
							}
							methods = methods + ") : " + md.getType();
							nAttrib = true;
						}
					}
				}
			}
			boolean nField = false;
			for (BodyDeclaration bd : ((TypeDeclaration) node).getMembers()) {
				if (bd instanceof FieldDeclaration) {
					FieldDeclaration fd = ((FieldDeclaration) bd);
					String scope;

					String identifier = bd.toStringWithoutComments().substring(0,
							bd.toStringWithoutComments().indexOf(" "));

					if (identifier.equals("private")) {
						scope = "-";
					} else if (identifier.equals("public")) {
						scope = "+";
					} else {
						scope = "";
					}

					String fieldClass = fd.getType().toString();

					fieldClass = fieldClass.replace("[", "(");
					fieldClass = fieldClass.replace("]", ")");
					fieldClass = fieldClass.replace("<", "(");
					fieldClass = fieldClass.replace(">", ")");

					String fieldName = fd.getChildrenNodes().get(1).toString();
					if (fieldName.contains("="))
						fieldName = fd.getChildrenNodes().get(1).toString().substring(0,
								fd.getChildrenNodes().get(1).toString().indexOf("=") - 1);

					if (scope.equals("-") && makeFieldPublic.contains(fieldName.toLowerCase())) {
						scope = "+";
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
								connection = connection + "*";
							Connect.put(classShortName + "-" + getDepen, connection);
						}
					}
					if (scope == "+" || scope == "-") {
						if (nField)
							attributes = attributes + "; ";
						attributes = attributes + scope + " " + fieldName + " : " + fieldClass;
						nField = true;
					}
				}

			}

			if (coi.getExtends() != null) {
				additions = additions + "[" + classShortName + "] " + "-^ " + coi.getExtends() + ",";

			}
			if (coi.getImplements() != null) {
				List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) coi.getImplements();
				for (ClassOrInterfaceType intface : interfaceList) {
					additions = additions + "[" + classShortName + "] " + "-.-^ " + "[" + "<<interface>>;" + intface
							+ "]" + ",";

				}
			}

			result = result + className;
			if (!attributes.isEmpty()) {

				result = result + "|";

				attributes = attributes.replace("[", "(");
				attributes = attributes.replace("]", ")");
				attributes = attributes.replace("<", "(");
				attributes = attributes.replace(">", ")");
				result = result + attributes;
			}
			if (!methods.isEmpty()) {

				result = result + "|";

				methods = methods.replace("[", "(");
				methods = methods.replace("]", ")");
				methods = methods.replace("<", "(");
				methods = methods.replace(">", ")");

				result = result + methods;

			}
			result = result + "]" + additions;

		}
		return result;

	}

	public String parsingextended(HashMap<String, String> Connect, HashMap<String, Boolean> map) {
		String result = "";
		Set<String> keys = Connect.keySet();
		for (String i : keys) {
			String[] classes = i.split("-");
			if (map.get(classes[0]))
				result = result + "[<<interface>>;" + classes[0] + "]";
			else
				result = result + "[" + classes[0] + "]";
			result = result + Connect.get(i);
			if (map.get(classes[1]))
				result = result + "[<<interface>>;" + classes[1] + "]";
			else
				result = result + "[" + classes[1] + "]";
			result = result + ",";
		}
		return result;
	}

	public LinkedList<CompilationUnit> javaParser(String input) {
		LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();

		File dir = new File(input);

		File[] files = dir.listFiles();
		CompilationUnit complilationUnit = null;
		FileInputStream in = null;

		System.out.println("Input Path: " + input);
		if (files.length > 0) {

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
