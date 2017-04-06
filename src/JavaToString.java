import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;





public class JavaToString {
	LinkedList<CompilationUnit> linkedList = new LinkedList();
	   final String input;
	    final String output;
	    String code;

	    JavaToString(String input, String output) {
	    	
	    
	        this.input = input;
	        this.output = input + "/" + output + ".png";
	      
	         code = "";
	    }
	    public void start() throws Exception  {
	    	File dir = new File(input);
	    	int count=0;
	    	File[] files = dir.listFiles();
	    	CompilationUnit cu ;
	    	FileInputStream in;
	    	//code="[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
	        System.out.println("Input Path: " + input);
	        if(files.length > 0){				
				System.out.println("Java file parsing in progress...");
				for(int i=0; i< files.length;i++)
				{
					
					if(files[i].getName().endsWith(".java"))
					{
						 in = new FileInputStream(files[i]);
						  cu = JavaParser.parse(in);
							in.close();
						  
						  linkedList.add(cu);
						// cuArray.add(cu);
						// System.out.println(cu);
						//System.out.println(files[i].toString());
						
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
