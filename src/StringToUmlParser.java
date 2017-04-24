
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.net.*;
import java.io.*;
public class StringToUmlParser {
	
	//static String outpath;
	public static Boolean createDiagram(String code, String outPath)
	
	
	{
		outPath="/Users/sidgore/Desktop/dia.png";
			
			

				// Activate the new trust manager
			
				    //SSLContext sc = SSLContext.getInstance("SSL");
				   // sc.init(null, trustAllCerts, new java.security.SecureRandom());
				  //  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			

				// And as before now you can use URL and URLConnection
				//URL url = new URL("https://hostname:port/file.txt");
				//URLConnection connection = url.openConnection();
				//InputStream is = connection.getInputStream();
			
		URL url;
		try {
			///url = new URL("https://yuml.me/diagram/plain/class/[A|-x:int;-y:int(*)]1-0..*[B],[A]-1[C],[A]-*[D]"+".png");
			
			url=new URL("https://yuml.me/diagram/plain/class/"+code+".png");
			
			//File file = new File("/Users/sidgore/Desktop/dia.png");
			File file = new File(outPath);
			//OutputStream out = new FileOutputStream(file);
			
			URLConnection conn = url.openConnection();
            conn.connect();
            
            System.out.println("\ndownload: \n");
            System.out.println(">> URL: " + url);
           
            System.out.println(">> size: " + conn.getContentLength()
                            + " bytes");
            
            
            InputStream in = conn.getInputStream();
            OutputStream out = new FileOutputStream(file);
            int b = 0;

            while (b != -1) {
                    b = in.read();

                    if (b != -1)
                            out.write(b);
            }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				    
				    
				 

			
				 
			
			
	
				
		
		 
		


		return true;
		
	}
	
}

