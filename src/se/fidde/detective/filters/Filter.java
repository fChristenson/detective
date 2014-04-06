package se.fidde.detective.filters;

import java.io.File;
import java.util.List;

import se.fidde.detective.suffixes.Suffix;

public class Filter {
	private volatile List<String> words;
	
	public Filter(List<String> words){
		this.words = words;
	}
	
	public void printFirstSuspectedFileAndPathOf(File folder){
		if(folder == null || !folder.canRead())
			return;
		
		String result = getFirstSuspectedFile(folder);
		if(!result.equals("")){
			System.out.println(folder.getAbsolutePath());
			System.out.println("Suspected file: " + result);
			System.out.println();
		}
	}
	
	public String getFirstSuspectedFile(File folder) {
		if(folder == null || !folder.canRead())
			return "";
		
		try{
			for(File file : folder.listFiles()){
				if(file.isFile() && isSuspect(file)){
					String path = file.toString();
					return getFileName(path);
				}
			}
			
		} catch(NullPointerException e){
			return "";
		}
		return "";
	}
			
	public boolean isSuspect(File file) {
		if(file == null || !file.canRead())
			return false;
		
		if(matchesSuffix(file, Suffix.values()) && hasKeyword(file))
			return true;
		
		return false;
	}
	
	private boolean matchesSuffix(File file, Suffix[] suffixes) {
		assert file != null && file.canRead();
		for (Suffix suffix : suffixes) {
			if (file.toString().matches(getRegex(suffix)))
				return true;

		}
		return false;
	}
	
	private String getRegex(Suffix suffix) {
		assert suffix != null;
		return "^.+(" + suffix.toString().toLowerCase() + "|" + suffix.toString().toUpperCase() + ")$";
	}
	
	private boolean hasKeyword(File file) {
		assert file != null && file.canRead();
		String path = file.getAbsolutePath();
		String string = getFileName(path);
		
		for (String dirtyWord : words) {
			if (string.contains(dirtyWord)) {
				return true;
			}

		}
		return false;
	}
	
	private String getFileName(String url){
		assert url != "";
		String os = System.getProperty("os.name");
		
		if(os.contains("windows") || os.contains("Windows"))
			return url.substring(url.lastIndexOf("\\"));
		
		return url.substring(url.lastIndexOf("/"));
	}

}
