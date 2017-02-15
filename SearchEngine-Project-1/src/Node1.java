
public class Node1 {

	private String file_name;
	private int frequance=1;
	private Node1 next;
	private Node1 previous;
	
	public Node1(String file_name) {
		super();
		
		this.file_name = file_name;
	}
	public void incFrequance() {
		frequance++;
	}
	
	
	
	public Node1 getPrevious() {
		return previous;
	}


	public void setPrevious(Node1 previous) {
		this.previous = previous;
	}


	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getFrequance() {
		return frequance;
	}

	

	public Node1 getNext() {
		return next;
	}

	public void setNext(Node1 next) {
		this.next = next;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
