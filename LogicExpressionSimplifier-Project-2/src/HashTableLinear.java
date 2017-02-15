import java.io.File;
import java.util.ArrayList;
import java.math.*;

public class HashTableLinear {

	private Entry[] hashTable;
	private int tableSize;
	private String currentFile;
	private int slotKey;
	private int hashUniqKey; 
	private final double load_factor=0.5;
	private int directoryFileCount;//tf-idf ile eklendi.
	private double[][] arrayTokenWeight;//tf-idf ile eklendi.
	private String[] arrayFileRow;//tf-idf ile eklendi.
	private ArrayList<File> fileList;//tf-idf ile eklendi.
	private ArrayList<Integer> wordCountList;//tf-idf ile eklendi.
	private int counter;///kaç eleman olduðunu bilmek için
	private int collision_counter;///toplam collision sayýsý için
	
	public HashTableLinear(int tableSize) {
		super();
		this.tableSize = 9901;////10000e yakýn asal sayý yap
		this.hashTable = new Entry[tableSize];
		this.collision_counter=0;
		for (int i = 0; i < tableSize; i++) {
			hashTable[i]=null;
		}
	}
	public void LinearHash(ArrayList<String> wordsList,ArrayList<File> fileList,ArrayList<Integer> wordCountList){
		int addition=0;
		this.fileList=fileList;//tf-idf ile eklendi.
		this.wordCountList=wordCountList;//tf-idf ile eklendi.
		directoryFileCount=fileList.size();
		
		
		for(int j=0;j<fileList.size();j++){
			
			currentFile=fileList.get(j).getName();
			if(j!=0)
				addition=addition+wordCountList.get(j-1);
			for (int i = 0; i < wordCountList.get(j); i++) {
			
			hashingMain(wordsList.get(i+addition),true);
			}
		}
	
	}
	public void hashingMain(String wordToHash,boolean flag){
		hashUniqKey=0;//holds uniq int value for strings.
		slotKey=0;
		for (int i = 0; i < wordToHash.length(); i++) {
			int charCode=wordToHash.charAt(i)-90;
			if(charCode<0){//charcode can only be lesser than zero if char is number.
				charCode=Math.abs(charCode);
			}
			hashUniqKey=(hashUniqKey*27+charCode);
		}
		int i=0;
		slotKey=Math.abs(func1(hashUniqKey)%tableSize);
		if(flag==true){
			while(!insert(wordToHash,slotKey,hashUniqKey)){
				i++;
				collision_counter++;
				slotKey=Math.abs((func1(hashUniqKey)+i)%tableSize);
			}
		}


	}
	public boolean insert(String word, int index,int uniqKey){
		if(hashTable[index]==null){
			Entry entry=new Entry(word,uniqKey,currentFile);
			hashTable[index]=entry;
			counter++;
			if (counter/tableSize >=load_factor) {
				System.out.println("Rehashing, Please Wait...");
				collision_counter=0;
				reSize();
			}
			return true;
		}
		else{
			if(hashTable[index].getWord().equals(word)){
				hashTable[index].addFile(currentFile);
				hashTable[index].incCount();
				return true;
			}
			else{
				return false;
			}
		}
	}
    public void reSize(){
		tableSize= tableSize *2;
		tableSize=getPrime(tableSize);
		System.out.println("New tableSize: "+tableSize);
		reFill();
	}
	public void reFill(){
		Entry[] temp = hashTable; 
		hashTable=new Entry[tableSize];
		counter =0;
		for(int i=0; i<temp.length;i++){
			if(temp[i] !=null){
				reHash(temp[i].getWord(),temp[i]);
				counter++;
			}
		}
		System.out.println("Rehash done");
		System.out.println();
	}
	public void reHash(String wordToHash, Entry entry){
		hashUniqKey=0;//holds uniq int value for strings.
		slotKey=0;
		for (int i = 0; i < wordToHash.length(); i++) {
			int charCode=wordToHash.charAt(i)-90;
			if(charCode<0){//charcode can only be lesser than zero if char is number.
				charCode=Math.abs(charCode);
			}
			hashUniqKey=(hashUniqKey*27+charCode);
		}
		int i=0;
		slotKey=Math.abs(func1(hashUniqKey + i)%tableSize);

		while(!reInsert(entry)){
			i++;
			slotKey=Math.abs((func1(hashUniqKey)+i)%tableSize);
		}

	}
	public boolean reInsert(Entry entry){

		//checks if the slot is empty
		if(hashTable[slotKey]==null){
			//add the Entry to new slot
			hashTable[slotKey]=entry;
			return true;
		}
		else{
			return false;
		}
	}
	public boolean isPrime(int number){
		if(number%2==0){
			return false;
		}
		for (int i = 3; i*i <= number; i+=2) {
			if(number%i==0){
				return false;
			}
		}
		return true;
	}
	public int getPrime(int minNumber){
		for (int i = minNumber; true ; i++) {
			if(isPrime(i)){
				return i;
			}
		}
	}
	public void search(String[] tokens, ArrayList<Integer> tokensQuantity){
		int[] arrayTokenSlotKey=new int[tokens.length];
		int mostFileCount=0;
		int tokenTheMostFileWith=0;


		for (int i = 0; i < tokens.length; i++) {
			hashingMain(tokens[i],false);
			if(hashTable[slotKey]!=null&&hashTable[slotKey].getWord().equalsIgnoreCase(tokens[i])){
				arrayTokenSlotKey[i]=slotKey;
			}else if(hashTable[slotKey]!=null&&!hashTable[slotKey].getWord().equalsIgnoreCase(tokens[i])){
				int j=0;
				boolean flag=false;
				while(flag==false){
					j++;
					slotKey=Math.abs((func1(hashUniqKey)+j))%tableSize;
					if(hashTable[slotKey]!=null&&hashTable[slotKey].getWord().equalsIgnoreCase(tokens[i])){
						arrayTokenSlotKey[i]=slotKey;
						flag=true;
					}
					else if(hashTable[slotKey]==null){
						System.out.println("Couldn't find");
						return;
					}
				}

			}else{
				System.out.println("Couldn't find");
				return;
			}

		}

		int fileCountOfToken;
		for(int i=0;i<arrayTokenSlotKey.length;i++){
			fileCountOfToken=hashTable[arrayTokenSlotKey[i]].getFileNames().size();
			if(fileCountOfToken>mostFileCount){
				mostFileCount=fileCountOfToken;
				tokenTheMostFileWith=arrayTokenSlotKey[i];
			}
		}

		arrayTokenWeight=new double[tokens.length+1][mostFileCount+1];
		arrayFileRow=hashTable[tokenTheMostFileWith].getFileNames().toArray(new String[hashTable[tokenTheMostFileWith].getFileNames().size()]);



		for (int i = 0; i < arrayTokenSlotKey.length; i++) {
			fileCountOfToken=hashTable[arrayTokenSlotKey[i]].getFileNames().size();
			weightCalcForQuary(i,tokensQuantity,fileCountOfToken);
			weightCalc(i,arrayTokenSlotKey[i],fileCountOfToken);
		}
		if(tokens.length==1){
			scoreCalc(false);

		}else{
			scoreCalc(true);
		}
		//printWeights();
		sortFiles();
	}
	public void weightCalcForQuary(int a,ArrayList<Integer> tokensQuantity,int fileCountOfToken){
		int totalTerm=0;
		for (int i = 0; i < tokensQuantity.size(); i++) {
			totalTerm=totalTerm+tokensQuantity.get(i);
		}

		arrayTokenWeight[a][0]=tfIdfCalc(fileCountOfToken,null,tokensQuantity.get(a),totalTerm,true);
		arrayTokenWeight[a][0]=BigDecimal.valueOf(arrayTokenWeight[a][0]).setScale(8, RoundingMode.HALF_UP)
				.doubleValue();


	}
	public void weightCalc(int tokenRow,int slotKey, int fileCountOfToken){
		String[] arrayFileNamesOfToken=hashTable[slotKey].getFileNames().toArray(new String[hashTable[slotKey].getFileNames().size()]);
		Integer[] arrayWordCountOfFiles=hashTable[slotKey].getCountAtFiles().toArray(new Integer[hashTable[slotKey].getCountAtFiles().size()]);

		boolean flag=false;
		boolean flag2=false;

		for (int i = 0; i < arrayFileRow.length; i++) {
			for (int j = 0; j < arrayFileNamesOfToken.length; j++) {//j=i
				if(arrayFileRow[i].equalsIgnoreCase(arrayFileNamesOfToken[j])){
					flag=true;
				}
			
			if(flag==true){

				arrayTokenWeight[tokenRow][i+1]=tfIdfCalc(fileCountOfToken,arrayFileRow[i],arrayWordCountOfFiles[j],0,false);
				arrayTokenWeight[tokenRow][i+1]=BigDecimal.valueOf(arrayTokenWeight[tokenRow][i+1]).setScale(8, RoundingMode.CEILING)
						.doubleValue();
				flag=false;
				flag2=true;
			}
			}
			if(flag2==false){
				arrayTokenWeight[tokenRow][i+1]=0;
			}else{
				flag2=false;
			}
		}

	}
	public void scoreCalc(boolean flag){
		if(flag==true){
			double dotProduct=0;
			double magd1=0;//magnitude
			double magd2=0;
			double cosineSimilarity;
			boolean flagForZeros=false;
			for(int i=0;i<arrayTokenWeight.length-1;i++){
				magd1=Math.sqrt(Math.pow(arrayTokenWeight[i][0],2)+magd1);
			}
			for (int j = 1; j < arrayTokenWeight[0].length; j++) {
				for (int i = 0; i < arrayTokenWeight.length-1; i++) {
					if(arrayTokenWeight[i][j]!=0.0){
					dotProduct=arrayTokenWeight[i][0]*arrayTokenWeight[i][j] +dotProduct;
					magd2=Math.sqrt(Math.pow(arrayTokenWeight[i][j],2)+magd2);
					}else{
						flagForZeros=true;
					}
				}
				if(flagForZeros==false){
				cosineSimilarity=dotProduct/magd1*magd2;
				arrayTokenWeight[arrayTokenWeight.length-1][j]=BigDecimal.valueOf(cosineSimilarity).setScale(8, RoundingMode.CEILING).doubleValue();
				}else{
					arrayTokenWeight[arrayTokenWeight.length-1][j]=0;
					flagForZeros=false;
				}
				magd2=0;
				dotProduct=0;
			}
		}else{
			for (int i = 1; i < arrayTokenWeight[0].length-1; i++) {
				arrayTokenWeight[1][i]=arrayTokenWeight[0][i];
			}
		}

	}
	public double tfIdfCalc(int fileCountOfToken,String fileName,int termFreq,int totalTerm,boolean flag){

		if(flag==true){
			BigDecimal divisor=new BigDecimal(fileCountOfToken);
			double b=BigDecimal.valueOf(directoryFileCount).divide(divisor, 8, RoundingMode.CEILING).doubleValue();
			b=Math.log(b);
			divisor=new BigDecimal(totalTerm);//a=(termFreq)/c
			double a=BigDecimal.valueOf(termFreq).divide(divisor, 8, RoundingMode.CEILING).doubleValue();

			return a*b; 

		}
		else{
			//double b=BigDecimal.valueOf(Math.log(1+directoryFileCount/fileCountOfToken)).setScale(8, RoundingMode.CEILING).doubleValue();
			BigDecimal divisor=new BigDecimal(fileCountOfToken);
			double b=BigDecimal.valueOf(directoryFileCount).divide(divisor, 8, RoundingMode.CEILING).doubleValue();
			b=Math.log(b);
			divisor=new BigDecimal(totalTermFinder(fileName));//a=(termFreq)/c
			double a=BigDecimal.valueOf(termFreq).divide(divisor, 6, RoundingMode.CEILING).doubleValue();
			return a*b; 
		}

	}
	public int totalTermFinder(String fileName){
		int wordCount=0;
		for (int i = 0; i < fileList.size(); i++) {
			if(fileName.equalsIgnoreCase(fileList.get(i).getName())){
				wordCount=wordCountList.get(i);
			}

		}
		return wordCount;
	}
	/*public void printWeights(){
		System.out.print("Quary"+" ");
		for (int i = 0; i < arrayFileRow.length; i++) {
			System.out.print(arrayFileRow[i]+" ");
		}
		System.out.println();
		for (int i = 0; i < arrayTokenWeight.length; i++) {
			for (int j = 0; j < arrayTokenWeight[0].length; j++) {
				System.out.print(arrayTokenWeight[i][j]+" ");
			}
			System.out.println();
		}
	}*/
	public void sortFiles(){
		double key=0;
		int i=0;
		double[][] arrayTempForPosition;
		int countForSize=0;
		for (int j = 1; j < arrayTokenWeight[0].length; j++){
			if(arrayTokenWeight[arrayTokenWeight.length-1][j]!=0.0)
				countForSize++;
		}
		arrayTempForPosition=new double[2][countForSize];
		for (int j = 1; j < arrayTokenWeight[0].length; j++){
			if(arrayTokenWeight[arrayTokenWeight.length-1][j]!=0.0){
			arrayTempForPosition[0][i]=j-1;
			arrayTempForPosition[1][i]=arrayTokenWeight[arrayTokenWeight.length-1][j];
			i++;
			}
		}
		i=0;	
		for (int j = 1; j < arrayTokenWeight[0].length; j++) {
			//arrayTokenWeight[arrayTokenWeight.length-1][j]=BigDecimal(arrayTokenWeight[arrayTokenWeight.length-1][j]).;
			key=arrayTokenWeight[arrayTokenWeight.length-1][j];
			
			i=j-1;
			while(i>-1&&arrayTokenWeight[arrayTokenWeight.length-1][i]<key){
				arrayTokenWeight[arrayTokenWeight.length-1][i+1]=arrayTokenWeight[arrayTokenWeight.length-1][i]; 
				i=i-1;
			}
			arrayTokenWeight[arrayTokenWeight.length-1][i+1]=key;
		}
		printSorted(arrayTempForPosition);
	}
	public void printSorted(double[][] arrayTempForPosition){
		boolean flag=false;
				
			for (int i = 0; i < arrayTokenWeight[0].length-1; i++) {
				for (int j = 0; j < arrayTempForPosition[0].length; j++) {
				if(arrayTokenWeight[arrayTokenWeight.length-1][i]==arrayTempForPosition[1][j]&&flag==false){
					flag=true;
					System.out.print("file: "+arrayFileRow[(int)arrayTempForPosition[0][j]]);
					System.out.println(" "+BigDecimal.valueOf(arrayTokenWeight[arrayTokenWeight.length-1][i]).toPlainString());
				}
			}
			flag=false;
		}
		
		
		
	}
	/*public void printTable(){///////////
		System.out.println("| index |"+"| keyWord |"+" | uniqKey |" );
		for (int i = 0; i < hashTable.length; i++) {
			System.out.print(i+" ");
			if(hashTable[i]!=null){
				hashTable[i].printVariables();
				hashTable[i].printFilesandCount();
			}
			else{

				System.out.print("index is Empty");
				System.out.println();
			}

		}
	} */  
	public int func1(int key){
		return key%tableSize;
	}
	
	public void sumCollision(){
		System.out.println();
		System.out.println("Sum collision is " + collision_counter);
		
	}
}

