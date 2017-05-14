import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;

import net.sourceforge.plantuml.SourceStringReader;

public class SequenceHelper {
	
	public SequenceHelper() {
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

	protected String generateSequenceDiagram(String source,String output) throws IOException {

        OutputStream png = new FileOutputStream(output);
        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
        return desc;

    }
	
	
	




}
