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

	SequenceDiagram(String input, String output,String ClassName,String FunctionName) {
		
		
		
	
        this.input = input;
        this.output = input + "\\" + output + ".png";
        this.ClassName = ClassName;
        this.FunctionName = FunctionName;
        map = new HashMap<String, String>();
        functionCalls = new HashMap<String, ArrayList<MethodCallExpr>>();
        code = "@startuml\n";
	}
	
	
	
	public void execute() {
	}
}