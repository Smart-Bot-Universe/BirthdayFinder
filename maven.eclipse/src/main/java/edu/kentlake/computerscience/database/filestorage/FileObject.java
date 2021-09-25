package edu.kentlake.computerscience.database.filestorage;

import java.io.File;

public class FileObject {

	private String dir;
	private File file;
	
	public static FileObject[] createFiles(String... dirs) {
		FileObject[] files = new FileObject[dirs.length];
		for(int i = 0;i < dirs.length;i++) files[i] = new FileObject(dirs[i]);
		return files;
	}
	
	public FileObject(String dir) {
		this(new File(dir));
	}
	
	public FileObject(File dir) {
		file = dir;
		this.dir = file.getPath();
	}
	
	public String getDir() {
		return dir;
	}
	
	public File getFile() {
		return file;
	}
	
	public String getFileName() {
		return file.getName();
	}
	
	public boolean isDirectory() {
		return file.isDirectory();
	}
	
	public boolean isFile() {
		return file.isFile();
	}
	
	public FileObject[] listFiles() {
		File[] files = file.listFiles(); if(files == null) return new FileObject[0];
		FileObject[] fileObjects = new FileObject[files.length];
		for(int i = 0;i < fileObjects.length;i++) fileObjects[i] = new FileObject(files[i]);
		return fileObjects;
	}
	
	@Override
	public String toString() {
		return dir;
	}
}
