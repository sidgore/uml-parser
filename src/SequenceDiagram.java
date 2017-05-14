







import java.io.*;
import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import net.sourceforge.plantuml.SourceStringReader;

public class SequenceDiagram {
    String code;
    final String inPath;
   // final String outPath;
    final String inFuncName;
    final String inClassName;

    HashMap<String, String> mapMethodClass;
    LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
    
 
    HashMap<String, ArrayList<MethodCallExpr>> mapMethodCalls;

    SequenceDiagram(String inPath, String inClassName, String inFuncName) {
        this.inPath = inPath;
       // this.outPath = inPath + "/" + outFile + ".png";
        this.inClassName = inClassName;
        this.inFuncName = inFuncName;
        mapMethodClass = new HashMap<String, String>();
        mapMethodCalls = new HashMap<String, ArrayList<MethodCallExpr>>();
        code = "@startuml\n";
    }

    public String execute(String input) throws Exception {
    	
    	SequenceHelper helper = new SequenceHelper();
    	linkedList = helper.javaParser(input);
       // linkedList = getCuArray(inPath);
    	 for (CompilationUnit cu : linkedList) {
             String className = "";
             List<TypeDeclaration> td = cu.getTypes();
             for (Node n : td) {
                 ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
                 className = coi.getName();
                 for (BodyDeclaration bd : ((TypeDeclaration) coi)
                         .getMembers()) {
                     if (bd instanceof MethodDeclaration) {
                         MethodDeclaration md = (MethodDeclaration) bd;
                         ArrayList<MethodCallExpr> mcea = new ArrayList<MethodCallExpr>();
                         for (Object bs : md.getChildrenNodes()) {
                             if (bs instanceof BlockStmt) {
                                 for (Object es : ((Node) bs)
                                         .getChildrenNodes()) {
                                     if (es instanceof ExpressionStmt) {
                                         if (((ExpressionStmt) (es))
                                                 .getExpression() instanceof MethodCallExpr) {
                                             mcea.add(
                                                     (MethodCallExpr) (((ExpressionStmt) (es))
                                                             .getExpression()));
                                         }
                                     }
                                 }
                             }
                         }
                         mapMethodCalls.put(md.getName(), mcea);
                         mapMethodClass.put(md.getName(), className);
                     }
                 }
             }
         }
        
        code += "actor user #black\n";
        code += "user" + " -> " + inClassName + " : " + inFuncName + "\n";
        code += "activate " + mapMethodClass.get(inFuncName) + "\n";
       // helper.convert(inFuncName,code,mapMethodCalls,mapMethodClass);
        parse(inFuncName);
        code += "@enduml";
    
        System.out.println("Plant UML Code:\n" + code);
        return code;
    }

    private void parse(String callerFunc) {

        for (MethodCallExpr mce : mapMethodCalls.get(callerFunc)) {
            String callerClass = mapMethodClass.get(callerFunc);
            String calleeFunc = mce.getName();
            String calleeClass = mapMethodClass.get(calleeFunc);
            if (mapMethodClass.containsKey(calleeFunc)) {
                code += callerClass + " -> " + calleeClass + " : "
                        + mce.toStringWithoutComments() + "\n";
                code += "activate " + calleeClass + "\n";
                parse(calleeFunc);
                code += calleeClass + " -->> " + callerClass + "\n";
                code += "deactivate " + calleeClass + "\n";
            }
        }
       
    }





}













