
import java.net.MalformedURLException;
import java.net.URL;

import java.net.*;
import java.io.*;

public class StringToUmlParser {

	public static void createDiagram(String code, String outPath)

	{
		outPath = "/Users/sidgore/Desktop/dia.png";

		URL url;
		try {

			// url=new
			// URL("https://yuml.me/diagram/plain/class/[A|-x:int;-y:int(*)]1-0..*[B],[A]-1[C],[A]-*[D]"+".png");

			url = new URL("https://yuml.me/diagram/plain/class/" + code + ".png");

			File file = new File("/Users/sidgore/Desktop/dia.png");
			// File file = new File(outPath);

			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in = conn.getInputStream();
			OutputStream out = new FileOutputStream(file);
			int b = 0;

			while (b != -1) {
				b = in.read();

				if (b != -1)
					out.write(b);
			}
			in.close();
			out.close();
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

	}

}
