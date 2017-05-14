
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class SequenceDiagram {
	String code;
	final String input;

	final String FunctionName;
	final String ClassName;

	HashMap<String, String> MethodMap;
	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();

	HashMap<String, ArrayList<MethodCallExpr>> MethodFlows;

	SequenceDiagram(final String input, final String ClassName, final String FunctionName) {
		this.input = input;

		this.ClassName = ClassName;
		this.FunctionName = FunctionName;
		MethodMap = new HashMap<String, String>();
		MethodFlows = new HashMap<String, ArrayList<MethodCallExpr>>();
		code = "@startuml\n";
	}

	public String execute(final String input) throws Exception {

		final SequenceHelper helper = new SequenceHelper();
		linkedList = helper.javaParser(input);

		for (final CompilationUnit c : linkedList) {
			String className = "";
			final List<TypeDeclaration> td = c.getTypes();
			for (final Node n : td) {
				final ClassOrInterfaceDeclaration ClassOrInter = (ClassOrInterfaceDeclaration) n;
				className = ClassOrInter.getName();
				for (final BodyDeclaration bd : ((TypeDeclaration) ClassOrInter).getMembers()) {
					if (bd instanceof MethodDeclaration) {
						final MethodDeclaration md = (MethodDeclaration) bd;
						final ArrayList<MethodCallExpr> m = new ArrayList<MethodCallExpr>();
						for (final Object cnode : md.getChildrenNodes()) {
							if (cnode instanceof BlockStmt) {
								for (final Object es : ((Node) cnode).getChildrenNodes()) {
									if (es instanceof ExpressionStmt
											&& ((ExpressionStmt) es).getExpression() instanceof MethodCallExpr) {
										m.add((MethodCallExpr) ((ExpressionStmt) es).getExpression());
									}
								}
							}
						}
						MethodFlows.put(md.getName(), m);
						MethodMap.put(md.getName(), className);
					}
				}
			}
		}

		code = code + "actor user #black\n" + "user" + " -> " + ClassName + " : " + FunctionName + "\n" + "activate "
				+ MethodMap.get(FunctionName) + "\n";

		convert(FunctionName);
		code = code + "@enduml";

		System.out.println("Code:\n" + code);
		return code;
	}

	private void convert(final String Func1) {

		for (final MethodCallExpr mce : MethodFlows.get(Func1)) {
			final String Class1 = MethodMap.get(Func1);
			final String Func2 = mce.getName();
			final String Class2 = MethodMap.get(Func2);
			if (MethodMap.containsKey(Func2)) {
				code = code + Class1 + " -> " + Class2 + " : " + mce.toStringWithoutComments() + "\n"
						+ "activate " + Class2 + "\n";

				convert(Func2);
				code = code + Class2 + " -->> " + Class1 + "\n" + "deactivate " + Class2 + "\n";

			}
		}

	}

}
