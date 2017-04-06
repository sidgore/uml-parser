
import java.io.*;
import java.util.*;
import java.lang.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;






public class JavaToString {
	LinkedList<CompilationUnit> linkedList = new LinkedList<CompilationUnit>();
	   final String input;
	    final String output;
	    String code;
	    HashMap<String, Boolean> map;

	    JavaToString(String input, String output) {
	    	 map = new HashMap<String, Boolean>();
	    
	        this.input = input;
	        this.output = input + "/" + output + ".png";
	      
	         code = "";
	    }
	    public void start()   {
	    	File dir = new File(input);
	    	int count=0;
	    	File[] files = dir.listFiles();
	    	CompilationUnit cu = null ;
	    	FileInputStream in = null;
	    	//code="[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
	        System.out.println("Input Path: " + input);
	        if(files.length > 0){				
				System.out.println("Java file parsing in progress...");
				for(int i=0; i< files.length;i++)
				{
					
					if(files[i].getName().endsWith(".java"))
					{
						 try {
							in = new FileInputStream(files[i]);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  try {
							cu = JavaParser.parse(in);
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
						  
						  linkedList.add(cu);
						// cuArray.add(cu);
						// System.out.println(cu);
						//System.out.println(files[i].toString());
						
							//count++;
							//System.out.println(count);
						
						}
				
						
					//System.out.println(count);
					}
				//System.out.println(count);
				}
	        List<TypeDeclaration> a=cu.getTypes();
	        ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) a.get(0);
	        map.put(coi.getName(), coi.isInterface());
	        /*for (CompilationUnit c : linkedList) {
	            List<TypeDeclaration> cl = c.getTypes();
	            for (Node n : cl) {
	                ClassOrInterfaceDeclaration coi = (ClassOrInterfaceDeclaration) n;
	            //    map.put(coi.getName(), coi.isInterface()); 
	              
	            }*/
	        for (String key : map.keySet()) {
	            System.out.println(key + " " + map.get(key));
	        }
	        
	        
	       // System.out.println("Unique Code: " + code);
	        //System.out.println("Out path: " + output);
	     
	    }
	    
	    
}
