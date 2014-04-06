package se.fidde.detective.crawlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import se.fidde.detective.filters.Filter;
import se.fidde.detective.util.Wordlist;

public class Crawler extends RecursiveAction {
	private static final long serialVersionUID = -5074676398989775838L;
	private final File ROOT_FOLDER;
	private final Filter FILTER;

	public Crawler(File root) throws IllegalArgumentException {
		if (root == null || !root.exists())
			throw new IllegalArgumentException(root + " is not a valid root!");

		Wordlist words = new Wordlist();
		FILTER = new Filter(words.WORDS);
		this.ROOT_FOLDER = root;
	}

	private List<File> getChildrenOf(File folder) {
		assert folder != null;
		List<File> folders = new ArrayList<>();

		for (File file : folder.listFiles()) {
			if (file.isDirectory())
				folders.add(file);
		}
		return folders;
	}

	private boolean folderContainsMoreFolders(File folder) {
		assert folder != null;
		if (!folder.isDirectory() || !folder.canRead())
			return false;

		try{
			for (File file : folder.listFiles()) {
				if (file.isDirectory())
					return true;
			}
			
		} catch(NullPointerException e){
			return false;
		}
		return false;
	}

	@Override
	protected void compute() {
			if(!ROOT_FOLDER.canRead())
				return;
			
			else if (!folderContainsMoreFolders(ROOT_FOLDER)) {
				FILTER.printFirstSuspectedFileAndPathOf(ROOT_FOLDER);
				return;
				
			} else {
				FILTER.printFirstSuspectedFileAndPathOf(ROOT_FOLDER);
				List<File> folders = getChildrenOf(ROOT_FOLDER);
				List<Crawler> crawlers = new ArrayList<>();
				
				for (File folder : folders) {
					Crawler crawler = new Crawler(folder);
					crawler.fork();
					crawlers.add(crawler);
				}
				
				for(Crawler crawler : crawlers)
					crawler.join();
				
				return;
			}
	}
}
