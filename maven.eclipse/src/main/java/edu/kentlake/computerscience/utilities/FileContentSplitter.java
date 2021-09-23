package edu.kentlake.computerscience.utilities;

/**
 * @author Ruvim Slyusar
 *
 *	This class is used to split the content in a file by rules set by the user
 */
public class FileContentSplitter {
	public static final byte SPLIT_ONCE = 0;
	public static final byte SPLIT_FOR_EVERY = 1;
	
	public static final byte NO_CONTENT_LOSS = 2;
	public static final byte SPLIT_LINE_CONTENT_LOSS = 3;
	
	private byte splitType;
	private byte contentLossType;
	
	private String[] fileContent;
	
	public FileContentSplitter(String fileContent) {
		this(fileContent, FileContentSplitter.SPLIT_ONCE, FileContentSplitter.SPLIT_LINE_CONTENT_LOSS);
	}
	
	public FileContentSplitter(String fileContent, byte splitType) {
		this(fileContent, splitType, FileContentSplitter.SPLIT_LINE_CONTENT_LOSS);
	}
	
	public FileContentSplitter(String fileContent, byte splitType, byte contentLossType) {
		this.fileContent = fileContent.split("\n");
		this.splitType = splitType;
		this.contentLossType = contentLossType;
	}
	
	/**
	 * Sets how to split the content based on the users desire
	 */
	public void setSplitType(byte splitType) {
		this.splitType = splitType;
	}
	
	/**
	 * Sets the type of content loss the user desires
	 */
	public void setContentLossType(byte contentLossType) {
		this.contentLossType = contentLossType;
	}
	
	/**
	 * 
	 * @return the file splitted by what the user desired
	 */
	public String[] splitContent(int line) {
		if(line >= fileContent.length) throw new IllegalArgumentException("You can't split the content by a line that is larger than the content itself");
		if(line == 0) return new String[] {""};
		
		switch(splitType) {
		case FileContentSplitter.SPLIT_ONCE:
			return useContentLossType(line, 1);	
		case FileContentSplitter.SPLIT_FOR_EVERY:
			return useContentLossType(line, fileContent.length / line);
			default: throw new NullPointerException("Not an available splitType.");
		}
	}
	
	/**
	 * When you do String.split(); you lose the thing you asked to split by... Sometimes you don't want that, sometimes you do. 
	 */
	private String[] useContentLossType(int line, int amountOfSplits) {
		String[] content = new String[amountOfSplits + 1];
		for(int i = 0;i < content.length;i++) content[i] = "";
		int counter = 0;
		int splitCounter = 0;
		
		switch(contentLossType) {
		case FileContentSplitter.SPLIT_LINE_CONTENT_LOSS:
			for(int i = 0;i < fileContent.length;i++) {
				splitCounter++;
				if(splitCounter == line) {
					content[counter] = content[counter].substring(0, content[counter].length() - 1);
					counter++;
					splitCounter = 0;
					continue;
				}
				content[counter] += fileContent[i] + "\n";
			}
			return content;
		case FileContentSplitter.NO_CONTENT_LOSS:
			for(int i = 0;i < fileContent.length;i++) {
				splitCounter++;
				content[counter] += fileContent[i];
				if(splitCounter == line) { counter++; splitCounter = 0; continue; }else content[counter] += "\n";
			}
			return content;
			default: throw new NullPointerException("Not an available contentLossType.");
		}
	}
}
