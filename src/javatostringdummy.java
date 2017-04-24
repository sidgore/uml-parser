
import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class javatostringdummy {
	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	final String input;
	final String output;
	String code;
	HashMap<String, Boolean> map;
	HashMap<String, String> Connect;

	javatostringdummy(String input, String output) {
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
		//code = "[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
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
		
		 System.out.println(linkedList);
		for (CompilationUnit c : linkedList) {
			List<TypeDeclaration> Nodes = c.getTypes();
			for (Node n : Nodes) {
				ClassOrInterfaceDeclaration ClassInterName = (ClassOrInterfaceDeclaration) n;
				map.put(ClassInterName.getName(), ClassInterName.isInterface());

			}
		}
		
		for (CompilationUnit cu : linkedList)
		{
            //code =code+ parser(cu);
			
			    String result = "";
		        String className = "";
		        String classShortName = "";
		        String methods = "";
		        String fields = "";
		        String additions = ",";
		        
		        ArrayList<String> makeFieldPublic = new ArrayList<String>();
		        List<TypeDeclaration> ltd = cu.getTypes();
		        Node node = ltd.get(0); // assuming no nested classes
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
		                if (cd.getDeclarationAsString().startsWith("public")
		                        && !coi.isInterface()) {
		                    if (nextParam)
		                        methods += ";";
		                    methods += "+ " + cd.getName() + "(";
		                    for (Object gcn : cd.getChildrenNodes()) {
		                        if (gcn instanceof Parameter) {
		                            Parameter paramCast = (Parameter) gcn;
		                            String paramClass = paramCast.getType().toString();
		                            String paramName = paramCast.getChildrenNodes()
		                                    .get(0).toString();
		                            methods += paramName + " : " + paramClass;
		                            if (map.containsKey(paramClass)
		                                    && !map.get(classShortName)) {
		                                additions += "[" + classShortName
		                                        + "] uses -.->";
		                                if (map.get(paramClass))
		                                    additions += "[<<interface>>;" + paramClass
		                                            + "]";
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
		                if (md.getDeclarationAsString().startsWith("public")
		                        && !coi.isInterface()) {
		                    // Identify Setters and Getters
		                    if (md.getName().startsWith("set")
		                            || md.getName().startsWith("get")) {
		                        String varName = md.getName().substring(3);
		                        makeFieldPublic.add(varName.toLowerCase());
		                    } else {
		                        if (nextParam)
		                            methods += ";";
		                        methods += "+ " + md.getName() + "(";
		                        for (Object gcn : md.getChildrenNodes()) {
		                            if (gcn instanceof Parameter) {
		                                Parameter paramCast = (Parameter) gcn;
		                                String paramClass = paramCast.getType()
		                                        .toString();
		                                String paramName = paramCast.getChildrenNodes()
		                                        .get(0).toString();
		                                methods += paramName + " : " + paramClass;
		                                if (map.containsKey(paramClass)
		                                        && !map.get(classShortName)) {
		                                    additions += "[" + classShortName
		                                            + "] uses -.->";
		                                    if (map.get(paramClass))
		                                        additions += "[<<interface>>;"
		                                                + paramClass + "]";
		                                    else
		                                        additions += "[" + paramClass + "]";
		                                }
		                                additions += ",";
		                            } else {
		                                String methodBody[] = gcn.toString().split(" ");
		                                for (String foo : methodBody) {
		                                    if (map.containsKey(foo)
		                                            && !map.get(classShortName)) {
		                                        additions += "[" + classShortName
		                                                + "] uses -.->";
		                                        if (map.get(foo))
		                                            additions += "[<<interface>>;" + foo
		                                                    + "]";
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
		                
		                
		                
		                String fieldScope ;
		                
		                String identifier=bd.toStringWithoutComments().substring(0,
                                bd.toStringWithoutComments().indexOf(" "));
		                
		                if(identifier.equals("private"))
		                {
		                	fieldScope="-";
		                }
		                else if (identifier.equals("public"))
		                {
		                	fieldScope="+";
		                }
		                else {
		                	fieldScope="";
		                }
		                
		                
		                
		            
		                
		              
		                
		                String fieldClass = fd.getType().toString();
		                
		                
		                fieldClass = fieldClass.replace("[", "(");
		                fieldClass = fieldClass.replace("]", ")");
		                fieldClass = fieldClass.replace("<", "(");
		                fieldClass = fieldClass.replace(">", ")");
		                
		                
		                
		                String fieldName = fd.getChildrenNodes().get(1).toString();
		                if (fieldName.contains("="))
		                    fieldName = fd.getChildrenNodes().get(1).toString()
		                            .substring(0, fd.getChildrenNodes().get(1)
		                                    .toString().indexOf("=") - 1);
		                // Change scope of getter, setters
		                if (fieldScope.equals("-")
		                        && makeFieldPublic.contains(fieldName.toLowerCase())) {
		                    fieldScope = "+";
		                }
		                String getDepen = "";
		                boolean getDepenMultiple = false;
		                if (fieldClass.contains("(")) {
		                    getDepen = fieldClass.substring(fieldClass.indexOf("(") + 1,
		                            fieldClass.indexOf(")"));
		                    getDepenMultiple = true;
		                } else if (map.containsKey(fieldClass)) {
		                    getDepen = fieldClass;
		                }
		                if (getDepen.length() > 0 && map.containsKey(getDepen)) {
		                    String connection = "-";

		                    if (Connect
		                            .containsKey(getDepen + "-" + classShortName)) {
		                        connection = Connect
		                                .get(getDepen + "-" + classShortName);
		                        if (getDepenMultiple)
		                            connection = "*" + connection;
		                        Connect.put(getDepen + "-" + classShortName,
		                                connection);
		                    } else {
		                        if (getDepenMultiple)
		                            connection += "*";
		                        Connect.put(classShortName + "-" + getDepen,
		                                connection);
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
		        
		        
		        
		        
		        if (coi.getExtends() != null) {
		            additions += "[" + classShortName + "] " + "-^ " + coi.getExtends();
		            additions += ",";
		        }
		        
		        if (coi.getImplements() != null) {
		            List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) coi
		                    .getImplements();
		            for (ClassOrInterfaceType intface : interfaceList) {
		                additions += "[" + classShortName + "] " + "-.-^ " + "["
		                        + "<<interface>>;" + intface + "]";
		                additions += ",";
		            }
		        }
		        
		        
		     // Combine className, methods and fields
		        result += className;
		        if (!fields.isEmpty()) {
		        	
		        	
	                
	                
		        	fields = fields.replace("[", "(");
		        	fields = fields.replace("]", ")");
		        	fields = fields.replace("<", "(");
		        	fields = fields.replace(">", ")");
		        	
		            result += "|" + fields;
		        }
		        if (!methods.isEmpty()) {
		        	
		        	methods = methods.replace("[", "(");
		        	methods = methods.replace("]", ")");
		        	methods = methods.replace("<", "(");
		        	methods = methods.replace(">", ")");
		        	
		            result += "|" + methods;
		        }
		        result += "]";
		        result += additions;
		        code=code+result;
		        
		        System.out.println("Unique Code: " + result);
		


			
		

		}
		
		//code=code+result;

		// for (String key : map.keySet()) {
		// System.out.println(key + " " + map.get(key));
		// }
		System.out.println("Unique Code: " + code);
		// System.out.println("Out path: " + output);

	}

}
