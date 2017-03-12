import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;





public class JavaToString {
	   final String input;
	    final String output;
	    String code;

	    JavaToString(String input, String output) {
	        this.input = input;
	        this.output = input + "/" + output + ".png";
	      
	         code = "";
	    }
	    public void start()  {
	    	code="[Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order] [Order]++-0..*>[LineItem] [Order]-[note:Aggregate root{bg:wheat}]";
	     
	        System.out.println("Unique Code: " + code);
	        System.out.println("Outath: " + output);
	     
	    }
	    
	    
}
