import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;





public class JavaToString {
	   final String input;
	    final String output;
	    String code,os;

	    JavaToString(String input, String output) {
	    	os=System.getProperty("os.name");
	    
	        this.input = input;
	        this.output = input + "/" + output + ".png";
	      
	         code = "";
	    }
	    public void start() throws Exception  {
	    	File dir = new File(input);
	    	int count=0;
	    	File[] files = dir.listFiles();
	    
	    	code="[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
	        System.out.println("Input Path: " + input);
	        if(files.length > 0){				
				System.out.println("Java file parsing in progress...");
				for(int i=0; i< files.length;i++)
				{
					if(files[i].getName().endsWith(".java"))
					{
						FileInputStream in = new FileInputStream(files[i]);
						 CompilationUnit cu = JavaParser.parse(in);
						// cuArray.add(cu);
						 System.out.println(cu);
						//System.out.println(files[i].toString());
						//if("Windows".equalsIgnoreCase(os))
							count++;
							System.out.println(count);
						
						}
						
					//System.out.println(count);
					}
				System.out.println(count);
				}
	        
	        
	        System.out.println("Unique Code: " + code);
	        System.out.println("Out path: " + output);
	     
	    }
	    
	    
}
