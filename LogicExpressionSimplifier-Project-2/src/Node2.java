import java.util.ArrayList;
public class Node2 {

	private String keyword;
	private int uniqKeyID;
	private List1 list;
	private Node2 next;
	private Node2 previous;
	public Node2(String keyword, int hashKey) {
		super();
		list= new List1();
		this.keyword = keyword;
		this.uniqKeyID = hashKey;
	}

	public void addNode(String fileName){
		
		list.add(fileName);
		
	}
	
	public int getFileAmount(){
		return list.fileAmount();
	}
	public ArrayList<String> getFileNames(){
		return list.getFileNames();
	}
	public ArrayList<Integer> getWordCountAtFiles(){
		return list.getWordCountAtFiles();
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getUniqKeyID() {
		return uniqKeyID;
	}
	public void setUniqKeyID(int hashKey) {
		this.uniqKeyID = hashKey;
	}
	public List1 getList() {
		return list;
	}
	public void setList(List1 list1) {
		this.list = list1;
	}
	public Node2 getNext() {
		return next;
	}
	public void setNext(Node2 next) {
		this.next = next;
	}
	public Node2 getPrevious() {
		return previous;
	}
	public void setPrevious(Node2 previous) {
		this.previous = previous;
	}


	
	
	
	
	
	
	
	
}
