
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.github.javaparser.ast.CompilationUnit;

import com.github.javaparser.ast.expr.MethodCallExpr;

public class SequenceDiagram {

	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	final String input;
	final String output;
	String ClassName;
	String FunctionName;
	String code;
	HashMap<String, String> map;
	HashMap<String, String> Connect;
	HashMap<String, ArrayList<MethodCallExpr>> functionCalls;

	ArrayList<CompilationUnit> compilationUnit;
	HashMap<String, ArrayList<MethodCallExpr>> functionCall;

	SequenceDiagram(String input, String output, String ClassName, String FunctionName) {

		this.input = input;
		this.output = input + "\\" + output + ".png";
		this.ClassName = ClassName;
		this.FunctionName = FunctionName;
		map = new HashMap<String, String>();
		functionCalls = new HashMap<String, ArrayList<MethodCallExpr>>();
		code = "@startuml\n";
	}

	public void execute() {

		code += "actor user #black\n";
		code += "user" + " -> " + input + " : " + FunctionName + "\n";
		code += "activate ";

		code += "@enduml";

		System.out.println("Plant UML Code:\n" + code);

	}

}