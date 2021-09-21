import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LZW {
	public ArrayList<Integer> compress(File inputFile) throws IOException {
		ArrayList<Integer> encodedAscii = new ArrayList<Integer>();

		// initalize encode dictionary
		HashMap<String, Integer> encodeDictionary = new HashMap<String, Integer>();
		for (int i = 0; i < 256; i++) {
			encodeDictionary.put("" + (char)i, i);
		}
		
		BufferedReader reader = new BufferedReader (new FileReader(inputFile));
		
		int i = 256;
		String current = "" + (char)reader.read();
		String next = "";
		
		// compression algorithm
		while (reader.ready()) {
			next = "" + (char)reader.read();
			String cAndN = current + next;
			
			if (encodeDictionary.containsKey(cAndN)){
				current = current + next;
			} else {
				encodeDictionary.put(cAndN, i);
				i++;
				encodedAscii.add(encodeDictionary.get(current));
				current = next;
			}
		}
		encodedAscii.add(encodeDictionary.get(current));
		reader.close();
		System.out.println("File Compressed");
		return encodedAscii;
	}
	
	public void decompress(ArrayList<Integer> encoded) throws IOException {
		HashMap<Integer,String> decodeDictionary = new HashMap <Integer,String>();
		// initialize decode dictionary
		for (int i = 0; i < 256; i++) {
			decodeDictionary.put(i, "" + (char)i);
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("decompressedFile.txt"));
		
		// decompression algorithm
		for (int i = 0; i < encoded.size(); i++) {
			String current = decodeDictionary.get(encoded.get(i));
			writer.write(current);
			if (i < encoded.size() - 1) {
				String next = decodeDictionary.get(encoded.get(i+1));
				if (next == null) { //special add
					decodeDictionary.put(decodeDictionary.size(), current + current.charAt(0));
				} else {
					decodeDictionary.put(decodeDictionary.size(), current + next.charAt(0));
				}
			}			
		}
		writer.close();
		System.out.println("File Decompressed");
	}
		
	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		LZW test = new LZW();
		ArrayList<Integer> result = test.compress(new File("lzw-file3.txt"));
		test.decompress(result);
		long endTime = System.nanoTime();
		System.out.println("Runtime (nanoseconds): " + (endTime - startTime));
	}
}
