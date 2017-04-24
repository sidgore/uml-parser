import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class UMLHelper {

	public HashMap<String, Boolean> getClassorInterfaceMap(HashMap<String, Boolean> map, CompilationUnit c) {

		List<TypeDeclaration> Nodes = c.getTypes();
		for (Node n : Nodes) {
			ClassOrInterfaceDeclaration ClassInterName = (ClassOrInterfaceDeclaration) n;
			map.put(ClassInterName.getName(), ClassInterName.isInterface());

		}

		return map;
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
