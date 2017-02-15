import java.util.ArrayList;

public class Entry {
	private String word;
	private int uniqKey;
	private ArrayList<String> fileNames;
	private ArrayList<Integer> countAtFiles;
	private String tempFile;
	private int counter=1;
	public Entry(String word, int uniqKey, String fileName){
		
		this.word=word;
		this.uniqKey=uniqKey;
		fileNames=new ArrayList<String>();
		countAtFiles=new ArrayList<Integer>();
		countAtFiles.add(counter);
		fileNames.add(fileName);
		tempFile=fileName;
	}
	public void incCount(){
		int lastIndex=fileNames.size()-1;
		if(fileNames.get(lastIndex).equalsIgnoreCase(tempFile)){
			counter++;
			countAtFiles.set(lastIndex, counter);
		}
		else{
			tempFile=fileNames.get(lastIndex);
			
		}
	}
	public void addFile(String fileName){
		int lastIndex=fileNames.size()-1;
		if(!fileNames.get(lastIndex).equals(fileName)){
			fileNames.add(fileName);
			countAtFiles.add(counter);
			counter=1;
		}
		
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public ArrayList<String> getFileNames() {
		return fileNames;
	}
	public int getUniqKey() {
		return uniqKey;
	}
	public void setUniqKey(int uniqKey) {
		this.uniqKey = uniqKey;
	}
	public void setFileNames(ArrayList<String> fileNames) {
		this.fileNames = fileNames;
	}
	public ArrayList<Integer> getCountAtFiles() {
		return countAtFiles;
	}
	public void setCountAtFiles(ArrayList<Integer> countAtFiles) {
		this.countAtFiles = countAtFiles;
	}
	public void printVariables(){
		System.out.println(word +" "+uniqKey);
	}
	public void printFilesandCount(){
		for (int i = 0; i < fileNames.size(); i++) {
			System.out.print("fileName: "+fileNames.get(i));
			System.out.println(" fileName: "+countAtFiles.get(i));
			
		}
		
	}
	
	
	

}
