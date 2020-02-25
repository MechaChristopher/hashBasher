import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class hashBasher {
	
	public static void restartApplication() throws URISyntaxException, IOException {
		
		final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
		final File currentJar = new File(hashBasher.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	
		 /* is it a jar file? */
		 if(!currentJar.getName().endsWith(".jar"))
		   return;
		
		 /* Build command: java -jar application.jar */
		 final ArrayList<String> command = new ArrayList<String>();
		 command.add(javaBin);
		 command.add("-jar");
		 command.add(currentJar.getPath());
		
		 final ProcessBuilder builder = new ProcessBuilder(command);
		 builder.start();
		 System.exit(0);
		 
	}
	
	static String hash(String input) {
		
		byte[] output = null;
		String inp = input;
		String hashValue = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			output = digest.digest(inp.getBytes("UTF-8"));
			hashValue = DatatypeConverter.printHexBinary(output).toLowerCase();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return hashValue;
	}
	
	static String readLines(File f, String userDefined) throws IOException{

		int i = 0;
		String line = null;

		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		
			while ((line = br.readLine()) != null) {
				
					if(hash(line).equals(userDefined)) {
						i++;
						System.out.println("============================================");
						System.out.println(line + "\t" + hash(line) + "\nSuccessful match \"" + line + "\" found in " + i + " attempts");
						break;
					} else {
						i++;
						System.out.print(line);						
						System.out.println("\t\t" + hash(line));
					}

			}
			
			if((line = br.readLine()) == null)
				System.out.println("\n\nNo match for the input hash: " + userDefined + " was found.");
			
		br.close();
		fr.close();
		return line;
		
	}
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		
		URL path = ClassLoader.getSystemResource("10-million-password-list-top-1000000.txt");
		File passwordList = new File(path.toURI());
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter a SHA-1 hash");
		String userDefined = scanner.next();
		
		try {
			readLines(passwordList, userDefined);
		} catch (Exception e) {
			
		}
		
		System.out.println("If you would like to try another hash, please type 1 and press enter");
		int response = scanner.nextInt();
		if (response == 1) {
			restartApplication();
		} else {
			System.exit(0);
		}
		
		scanner.close();
		
	}
	
}
