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
	 * 
	 * @return the file splitted by what the user desired... And yes you can call this rather than splitContent(int line)... JK its private
	 */
	private String[] useContentLossType(int line, int amountOfSplits) {
		String[] content = new String[amountOfSplits + 1];
		for(int i = 0;i < content.length;i++) content[i] = "";
		int counter = 0;
		int splitCounter = 1;
		
		switch(contentLossType) {
		case FileContentSplitter.SPLIT_LINE_CONTENT_LOSS:
			for(int i = 0;i < fileContent.length;i++) {
				if(amountOfSplits == 0) { content[content.length - 1] = combinePartOfArray(fileContent, i, fileContent.length); break; }
				if(splitCounter == line) {
					if(content[counter].endsWith("\n")) content[counter] = content[counter].substring(0, content[counter].length() - 1);
					counter++; splitCounter = 1; amountOfSplits--; continue;
				}
				content[counter] += fileContent[i] + "\n";
				splitCounter++;
			}
			return content;
		case FileContentSplitter.NO_CONTENT_LOSS:
			for(int i = 0;i < fileContent.length;i++) {
				if(amountOfSplits == 0) { content[content.length - 1] = combinePartOfArray(fileContent, i, fileContent.length); break; }
				content[counter] += fileContent[i];
				if(splitCounter == line) { counter++; splitCounter = 1; amountOfSplits--; continue; }else content[counter] += "\n";
				splitCounter++; 
			}
			return content;
			default: throw new NullPointerException("Not an available contentLossType.");
		}
	}
	
	/**
	 * This method is used to combine part of an array... Like l0l, what did you expect?
	 * 
	 * @return A string that combined the part of the array being asked to
	 */
	private String combinePartOfArray(String[] arr, int start, int end) {
		if(end > arr.length) throw new ArrayIndexOutOfBoundsException("end > arr.length");
		String combined = "";
		for(int i = start;i < end;i++) {
			combined += arr[i];
			if(i != arr.length - 1) combined += "\n";
		}
		return combined;
	}
}