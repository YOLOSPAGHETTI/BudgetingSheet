package file;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	public static void createFile(String dir) {
		File file = new File(dir);
		try {
			file.createNewFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<File> getFilesInDir(String dir) {
		ArrayList<File> files = new ArrayList<File>();
		File folder = new File(dir);
		File[] listOfFolders = folder.listFiles();
		for(File file : listOfFolders) {
			if(file.getName().endsWith(".xml")) {
				files.add(file);
			}
		}
		return files;
	}
	
	public static ArrayList<String> read(String dir) {
		ArrayList<String> lines = new ArrayList<String>();
		try {
			FileReader reader = new FileReader(dir);
			BufferedReader bufferedReader = new BufferedReader(reader);
			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				lines.add(line);
			}
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static void write(List<String> lines, String dir) {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter =null;
		try {
			fileWriter=new FileWriter(new File(dir));
			bufferedWriter =new BufferedWriter(fileWriter);
			for(String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileWriter!=null){
					fileWriter.close();
				}
				if(bufferedWriter!=null){
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void write(String line, String dir) {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter =null;
		try {
			fileWriter=new FileWriter(new File(dir));
			bufferedWriter =new BufferedWriter(fileWriter);
			bufferedWriter.write(line);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileWriter!=null){
					fileWriter.close();
				}
				if(bufferedWriter!=null){
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void clear(String dir) {
		try {
			new FileWriter(dir, false).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
