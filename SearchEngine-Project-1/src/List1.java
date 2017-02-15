import java.util.ArrayList;
public class List1 {
	private Node1 head;
	private Node1 tail;
	private ArrayList<Integer> wordCountAtFiles;
	private ArrayList<String> fileNames;
	public List1() {
		head = null;
		tail = null;
	}
	

	public void add(String file_name){
		Node1 newNode=new Node1(file_name);
		
		if(head==null&&tail==null){
			head=newNode;
			tail=newNode;
		}
		else{
			head.setPrevious(newNode);
			newNode.setNext(head);
			head=newNode;
		}
	}
	public ArrayList<String> getFileNames(){
		Node1 temp=tail;
		fileNames=new ArrayList<String>();
		while(temp!=null){
			
			fileNames.add(temp.getFile_name());//doðru sýrada atýlýyor mu kontrol et
			temp=temp.getPrevious();
		}
		return fileNames;
	}
	public ArrayList<Integer> getWordCountAtFiles(){
		return wordCountAtFiles;
	}
	public Node1 getHead() {
		return head;
	}

	public void setHead(Node1 head) {
		this.head = head;
	}
	public int fileAmount(){
		Node1 temp=tail;
		int amountOfFile=0;
		wordCountAtFiles=new ArrayList<Integer>();
		
		while(temp!=null){
			
			amountOfFile++;
			wordCountAtFiles.add(temp.getFrequance());//doðru sýrada atýlýyor mu kontrol et
			temp=temp.getPrevious();
		}
		return amountOfFile;
	}
	
	
	
	
	
	

}
