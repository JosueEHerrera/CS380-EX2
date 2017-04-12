import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;


public class Ex2Client{
	private static byte convert(int a, int b){
		a = (a << 4)| b;
		return (byte) a;
	}
	public static void main(String[] args) throws Exception{
		//Socket socket = new Socket("codebank.xyx", 38102);

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
			int firstByte, secondByte;
			System.out.println("Received bytes: ");
			
			for(int i = 0; i < buffer.length; i++){
				// Reading 4 bits at a time 
				firstByte = is.read();
				secondByte = is.read();
				
				// Merge two 4 bits chunks into 8 bits
				//``firstByte = (firstByte << 4)| secondByte ;
				//firstByte = firstByte
				
				//adding bytes to buffer array 
				buffer[i] = convert(firstByte,secondByte);
				//print the bytes 
				if (i % 10 == 0){
					System.out.println();
				}
				System.out.print(buffer[i]);
			}


			//Generating CRC32
			CRC32 c = new CRC32();
			c.reset();
			c.update(buffer, 0, 100);
			long send = c.getValue();
			System.out.println("\nGenerated CRC32: "+ send);
			

			//CHECKING RESULT
			//System.out.println(br.read());
			int result = br.read();
			if (result == 1){
				System.out.println("Response Good.");
			}else{
				System.out.println("Response Bad.");
			}
		}

	}



}