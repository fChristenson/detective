package se.fidde.detective.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wordlist {
	public final List<String> WORDS;
	
	public Wordlist(){
		WORDS = getWordlist("/se/fidde/detective/wordlists/badwords.txt");
	}
	
	public Wordlist(String url){
		WORDS = getWordlist(url);
	}
	
	private List<String> getWordlist(String url){
		BufferedReader br = null;
		List<String> result = new ArrayList<>();
		try{
			br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(url)));
			String line = br.readLine();
			
			while (line != null) {
				result.add(line);
				line = br.readLine();
			}
			br.close();
			
		} catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		} 
			
		Collections.sort(result);
		return result;

	}
}
