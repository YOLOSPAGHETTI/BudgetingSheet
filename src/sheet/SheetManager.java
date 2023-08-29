package sheet;

import java.io.File;
import java.util.ArrayList;

import file.FileManager;
import file.XMLManager;
import ref.Constants;
import ui.TabManager;

public class SheetManager {
	private TabManager tabManager;
	
	private String separator;
	private String saveDir;
	
	private ArrayList<Sheet> sheets;
	private Sheet currentSheet;
	
	public SheetManager(TabManager tabManager) {
		this.tabManager = tabManager;
		sheets = new ArrayList<Sheet>();
		
		separator = System.getProperty("file.separator");;
    	saveDir = System.getProperty("user.dir") + separator + "src" + separator + "saves";
    	ArrayList<File> saveFiles = FileManager.getFilesInDir(saveDir);
    	
    	for(File file : saveFiles) {
    		ArrayList<String> xmlLines = FileManager.read(file.getAbsolutePath());
    		String name = XMLManager.getSpecificValueFromXMLFile(xmlLines, Constants.SHEET);
    		
    		Sheet sheet = new Sheet(name, file, xmlLines);
    		sheets.add(sheet);
    		
    		if(currentSheet == null) {
    			currentSheet = sheet;
    		}
    	}
    	
    	if(sheets.isEmpty()) {
    		addSheet("Default");
    	}
	}
	
	public Sheet getSheetByName(String name) {
		for(Sheet sheet : sheets) {
			if(sheet.getName().equals(name)) {
				return sheet;
			}
		}
		return null;
	}
	
	public Sheet getCurrentSheet() {
		return currentSheet;
	}
	
	public ArrayList<String> getSheetNames() {
		ArrayList<String> sheetNames = new ArrayList<String>();
		for(Sheet sheet : sheets) {
			sheetNames.add(sheet.getName());
		}
		
		return sheetNames;
	}
	
	public void setCurrentSheet(String name) {
    	currentSheet = getSheetByName(name);
    	tabManager.loadTabs();
    }
	
	public boolean addSheet(String name) {
		String nameNoSpace = name.replace(" ", "");
		String dir = saveDir + separator + nameNoSpace + ".xml";
		
		if(!name.isEmpty()) {
			if(getSheetByName(name) == null) {
				FileManager.createFile(dir);
				String xml = XMLManager.buildXMLLine(Constants.SHEET, name);
				FileManager.write(xml, dir);
				ArrayList<String> saveXml = new ArrayList<String>();
				saveXml.add(xml);
				currentSheet = new Sheet(name, new File(dir), saveXml);
				sheets.add(currentSheet);
				
				return true;
			}
		}
		return false;
	}
	
	public void deleteSheet(String name) {
		Sheet sheet = getSheetByName(name);
		
		File file = sheet.getFile();
		file.delete();
		
		sheets.remove(sheet);
	}
	
	public void editSheetName(String oldSheetName, String newSheetName) {
		Sheet sheet = getSheetByName(oldSheetName);
		sheet.setName(newSheetName);
		
		File oldFile = sheet.getFile();
		File newFile = new File(sheet.getDir().replace(oldSheetName+".xml", newSheetName.replace(" ", "")+".xml"));
		oldFile.renameTo(newFile);
		sheet.setFile(newFile);
		
		ArrayList<String> xml = new ArrayList<String>();
		String xmlLine = XMLManager.buildXMLLine(Constants.SHEET, newSheetName);
		xml.add(xmlLine);
		replaceSection(Constants.SHEET, xml);
	}
	
	public void replaceSection(String tag, ArrayList<String> newSectionXml) {
		ArrayList<String> saveXml = currentSheet.getSaveXml();
		saveXml = XMLManager.removeSections(saveXml, tag, true);
		
		saveXml.addAll(newSectionXml);
		
		currentSheet.overwriteXml(saveXml);
		
		tabManager.refreshCaldendarTab();
	}
}
