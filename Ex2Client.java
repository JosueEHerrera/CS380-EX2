/*
/	Name :	Josue Herrera
/			Kean Jafari
/
*/


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;
import java.nio.*;


public class Ex2Client{
	private static byte convert(int a, int b){
		a = (a << 4)| b;
		return (byte) a;
	}
	public static void main(String[] args) throws Exception{
		//Socket socket = new Socket("codebank.xyx", 38102);
		int counter = 0;
		try(Socket socket = new Socket("codebank.xyz", 38102)){
			//Server Connected Message 
			System.out.println("Connected to Server.");
			
			// Read from input
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			// Write to stream 
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);

			//Creating Byte Array
			byte[] buffer = new byte[100];
			System.out.println("Received bytes: ");
			ByteBuffer bb = ByteBuffer.allocate(4);


			for (int i = 0; i < 100; i++) {
				
				if (i % 10 == 0)
					System.out.println();

				// Read first byte
				int firstRecByte = is.read();
				int firstByte = firstRecByte*16;

				// Read second byte
				int secondRecByte = is.read();
				int finalByte = firstByte + secondRecByte;
				buffer[i] = (byte) finalByte;


				// HEX to String, then changes to Uppercase
				String strRepOne = Integer.toHexString(firstRecByte);
				String strRepTwo = Integer.toHexString(secondRecByte);
				strRepOne = strRepOne.toUpperCase();
				strRepTwo = strRepTwo.toUpperCase();

				// Printing the hex representation of the first and second 
				// bytes individually
				System.out.print(strRepOne + strRepTwo);

			}


			//Generating CRC32
			CRC32 c = new CRC32();
			c.reset();
			c.update(buffer, 0, 100);
			long send = c.getValue();
			String str = Long.toHexString(send);
			str = str.toUpperCase();
			System.out.println("\n\nGenerated CRC32: "+ str);


			//Sending CRC
			bb.putInt( (int) send);
			byte[] crcToServer = bb.array();
			out.write(crcToServer);


			//CHECKING RESULT
			int result = br.read();
			if (result == 1){
				System.out.println("Response Good.");
			}else{
				System.out.println("Response Bad.");
			}
		} catch (Exception e) { e.printStackTrace(System.out); }
	}
}