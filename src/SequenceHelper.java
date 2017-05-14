import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

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

	@SuppressWarnings("deprecation")
	protected String generateSequenceDiagram(String source, String output) throws IOException {

		OutputStream png = new FileOutputStream(output);
		SourceStringReader reader = new SourceStringReader(source);
		return reader.generateImage(png);

	}

}
