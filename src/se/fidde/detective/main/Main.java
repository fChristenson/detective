package se.fidde.detective.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import se.fidde.detective.crawlers.Crawler;
import se.fidde.detective.filters.Filter;
import se.fidde.detective.util.Wordlist;

public class Main {

	private static List<File> foldersToScan;
	private static File rootFolder;
	
	
	public static void main(String[] args) {
		validate(args);
		System.out.println("Finding secrets...");
		try {
			rootFolder = new File(args[0]);
			foldersToScan = getFoldersIn(rootFolder);
			invokeTasks();
			
			Wordlist words = new Wordlist();
			Filter filter = new Filter(words.WORDS);
			filter.printFirstSuspectedFileAndPathOf(rootFolder);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());

		} finally {
			System.out.println("Done finding secrets");
			System.exit(0);
			
		}
	}

	private static void invokeTasks(){
		ForkJoinPool pool = new ForkJoinPool();
		
		for(File child : foldersToScan){
			pool.invoke(new Crawler(child));
				
		}
	}

	private static List<File> getFoldersIn(File root){
		assert root != null;
		if(!root.exists())
			return new ArrayList<>();
			
		List<File> result = new ArrayList<>();
		for(File file : root.listFiles()){
			if(file.isDirectory())
				result.add(file);
		}
		
		return result;
	}

	private static void validate(String[] args) {
		assert args != null;
		if (args.length < 1) {
			System.out.println("Program needs a root folder");
			System.exit(0);

		} else if (args.length > 1) {
			System.out.println("Too many arguments");
			System.exit(0);
		}
	}
	
}
