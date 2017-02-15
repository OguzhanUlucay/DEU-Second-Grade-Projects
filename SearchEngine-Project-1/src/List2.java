
public class List2 {
	private Node2 head;
	private Node2 tail;
	private int collision_counter;///toplam collision sayýsý için
	
	public List2() {
		
	head = null;
	tail = null;
	this.collision_counter=0;
	}
	

	public boolean search(int searchToUniqKey,String fileName){
		Node2 temp2;
		boolean flag=false;
		
		int mean=(head.getUniqKeyID()+tail.getUniqKeyID())/2; //calculates avarage value of keys
		
		if(searchToUniqKey>=mean){//if the UniqKey is bigger than mean, search starts from head of list.
			temp2=head;
			while(temp2.getUniqKeyID()>=mean){
				
				if(searchToUniqKey == temp2.getUniqKeyID()){
					if(!fileName.equals(temp2.getList().getHead().getFile_name())){
						temp2.addNode(fileName);
					}
					else{
					temp2.getList().getHead().incFrequance();
					}
					flag= true;
				}
				if(temp2.getNext()!=null){
				temp2=temp2.getNext();
				}else{
					break;
				}
				
				
			}
		}
		else{
			//if the UniqKey is smaller than mean, search starts from tail of list.
			temp2=tail;
			while(temp2.getUniqKeyID()<mean){
				
				if(searchToUniqKey==temp2.getUniqKeyID()){
					if(!fileName.equals(temp2.getList().getHead().getFile_name())){
						temp2.addNode(fileName);
					}
					else{
					temp2.getList().getHead().incFrequance();
					}
					flag= true;
				}
				if(temp2.getPrevious()!=null){
					temp2=temp2.getPrevious();
					}
				else{
					break;
				}
					
			}
		}
		return flag;
	}
	
	public void add(String Word,String fileName, int uniqKeyId){
		
		
		if(head==null&&tail==null){
			Node2 newNode=new Node2(Word,uniqKeyId);//frequance **
			newNode.addNode(fileName);//*
			
			head=newNode;
			tail=newNode;
		}
		
		else if(search(uniqKeyId, fileName)==false){
			Node2 newNode=new Node2(Word,uniqKeyId);//frequance **
			newNode.addNode(fileName);//*
			
			if(newNode.getUniqKeyID()>head.getUniqKeyID()){
				newNode.setNext(head);
				head.setPrevious(newNode);
				head=newNode;
				collision_counter++;
				
			}
			else{
				
				Node2 temp=head;
				
				while(temp.getNext()!=null&&newNode.getUniqKeyID()<temp.getUniqKeyID()){
					
					temp=temp.getNext();
					collision_counter++;
					
				}
				if(head.getNext()!=null){
					newNode.setNext(temp);
					newNode.setPrevious(temp.getPrevious());
					temp.setPrevious(newNode);
					
					newNode.getPrevious().setNext(newNode);
				}else{
					head.setNext(newNode);
					newNode.setPrevious(head);
					tail=newNode;
				}
				
			}
		}
		
		
	}
	public Node2 searchToFind(int uniqKey){
    	Node2 temp ;
    	
    	int mean=(head.getUniqKeyID()+tail.getUniqKeyID())/2;
    	if(uniqKey>=mean){
			temp=head;
			while(temp.getUniqKeyID()>=mean){
				
				if(uniqKey == temp.getUniqKeyID()){
	         	return temp;
				}
				else{
					temp=temp.getNext();
					
				}	
			}
		}
		else{
			temp=tail;
			while(temp.getUniqKeyID()<mean){
				
				if(uniqKey==temp.getUniqKeyID()){
					return temp;
				}
				else{
					temp=temp.getPrevious();
					
				}
					
			}
		}
    	return null;
 }


	
	
	public Node2 getHead() {
		return head;
	}
	public void setHead(Node2 head) {
		this.head = head;
	}
	public Node2 getTail() {
		return tail;
	}
	public void setTail(Node2 tail) {
		this.tail = tail;
	}
	
	public int getCollision_counter() {
		return collision_counter;
	}	
	
	
}
