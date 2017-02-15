import java.io.File;
import java.util.ArrayList;
import java.math.*;

public class HashTable2 {

	private Entry[] hashTable;
	private int tableSize;
	private String currentFile;
	private int slotKey;
	private int hashUniqKey; 
	private final double load_factor=0.5;
	private int directoryFileCount;//for tf-idf
	private double[][] arrayTokenWeight;//for tf-idf
	private String[] arrayFileRow;//for tf-idf
	private ArrayList<File> fileList;//for tf-idf
	private ArrayList<Integer> wordCountList;//for tf-idf
	private int counter;///kaç eleman olduðunu bilmek için
	private int collision_counter;///sum of collision 


	public HashTable2(int tableSize) {
		super();
		this.tableSize = 9901;////10000e yakýn asal sayý yap
		this.hashTable = new Entry[tableSize];
		this.collision_counter=0;
		for (int i = 0; i < tableSize; i++) {
			hashTable[i]=null;
		}
	}
	public void doubleHash(ArrayList<String> wordsList,ArrayList<File> fileList,ArrayList<Integer> wordCountList){
		int addition=0;
		this.fileList=fileList;//for tf-idf .
		this.wordCountList=wordCountList;//for tf-idf.
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
				slotKey=Math.abs((func1(hashUniqKey)+i*func2(hashUniqKey))%tableSize);

			}
		}

	}
	public int func1(int key){
		return key%tableSize;
	}
	public int func2(int key ){
		return (int)Math.floor(key/5);
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
				hashTable[index].addFile(currentFile);//120543 7839
				hashTable[index].incCount();
				return true;
			}
			else{
				return false;
			}
		}
	}
	public void reSize(){
		//Takes new prime number for TableSize
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
		slotKey=Math.abs(func1(hashUniqKey)%tableSize);

		while(!reInsert(entry)){
			i++;
			slotKey=Math.abs((func1(hashUniqKey)+i*func2(hashUniqKey))%tableSize);
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
		//This metod is the main metod for Calculating Score and find Ranking.
		//all functions call from this metod.
		int[] arrayTokenSlotKey=new int[tokens.length];
		int mostFileCount=0;
		int tokenTheMostFileWith=0;

		/*This for calculates SlotKey for each word of Quary.
		 * if it find SlotKey, then assings to TokenSlotKey array
		 * if it can't takes function2 and tries again
		 * repeats procces until to find word or the faces witha  null slot 
		 * */
		for (int i = 0; i < tokens.length; i++) {
			hashingMain(tokens[i],false);
			if(hashTable[slotKey]!=null&&hashTable[slotKey].getWord().equalsIgnoreCase(tokens[i])){
				arrayTokenSlotKey[i]=slotKey;
			}else if(hashTable[slotKey]!=null&&!hashTable[slotKey].getWord().equalsIgnoreCase(tokens[i])){
				int j=0;
				boolean flag=false;
				while(flag==false){
					j++;
					slotKey=Math.abs((func1(hashUniqKey)+j*func2(hashUniqKey)))%tableSize;//j was i
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
		/* this for takes every token's file array and compares theirs size to 
		 * take the most large file array make it a solid point for comparing mutual files.
		 * (if we would't take largest one we couldn't fine mutual files for multiple tokens)
		 * */
		for(int i=0;i<arrayTokenSlotKey.length;i++){
			fileCountOfToken=hashTable[arrayTokenSlotKey[i]].getFileNames().size();
			if(fileCountOfToken>mostFileCount){
				mostFileCount=fileCountOfToken;
				tokenTheMostFileWith=arrayTokenSlotKey[i];//to take file Array Size again at bottom lines.
			}
		}

		//this is the main Array that holds TF-IDF values
		//and scores for Mutual Files.
		arrayTokenWeight=new double[tokens.length+1][mostFileCount+1];
		arrayFileRow=hashTable[tokenTheMostFileWith].getFileNames().toArray(new String[hashTable[tokenTheMostFileWith].getFileNames().size()]);


		//Calls functions to calculate TF-IDF value for words.
		for (int i = 0; i < arrayTokenSlotKey.length; i++) {
			fileCountOfToken=hashTable[arrayTokenSlotKey[i]].getFileNames().size();
			weightCalcForQuary(i,tokensQuantity,fileCountOfToken);//Every Word of Quary need TF-IDF value for Cosine Similary
			weightCalc(i,arrayTokenSlotKey[i],fileCountOfToken);
			
		}
		//these metods calculates Scores with Cosine Similarty
		if(tokens.length==1){
			scoreCalc(false);

		}else{
			scoreCalc(true);
		}
		//printWeights();
		sortFiles();//Sort Scores and gives order to mutual Files(RANKING).
		
	}

	public void weightCalcForQuary(int a,ArrayList<Integer> tokensQuantity,int fileCountOfToken){
		//Calculates weigth for only Quary,
		//please read weightCalc comments for more info
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
		/* Takes All files and word Freq of Those Files from Entry 
		 *2. Start Compare the word's files with the longest file pattern to find mutual files
		 *3. if finds then calculates TF-IDF value.
		 * */
		boolean flag=false;
		boolean flag2=false;

		for (int i = 0; i < arrayFileRow.length; i++) {
			for (int j = 0; j < arrayFileNamesOfToken.length; j++) {//j=i 
				if(arrayFileRow[i].equalsIgnoreCase(arrayFileNamesOfToken[j])){
					flag=true;
				}

				if(flag==true){

					arrayTokenWeight[tokenRow][i+1]=tfIdfCalc(fileCountOfToken,arrayFileRow[i],arrayWordCountOfFiles[j],0,false);//j was i
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
	public double tfIdfCalc(int fileCountOfToken,String fileName,int termFreq,int totalTerm,boolean flag){
		//Calculates Tf-IDF according this Formula: (Term Frequance/TotalWord at That File)*log(totalFileCount/fileCountOfToken).
		if(flag==true){
			BigDecimal divisor=new BigDecimal(fileCountOfToken);
			double b=BigDecimal.valueOf(directoryFileCount).divide(divisor, 8, RoundingMode.CEILING).doubleValue();
			b=Math.log(b);
			divisor=new BigDecimal(totalTerm);
			double a=BigDecimal.valueOf(termFreq).divide(divisor, 8, RoundingMode.CEILING).doubleValue();

			return a*b; 

		}
		else{
			BigDecimal divisor=new BigDecimal(fileCountOfToken);
			double b=BigDecimal.valueOf(directoryFileCount).divide(divisor, 8, RoundingMode.CEILING).doubleValue();
			b=Math.log(b);
			divisor=new BigDecimal(totalTermFinder(fileName));
			double a=BigDecimal.valueOf(termFreq).divide(divisor, 6, RoundingMode.CEILING).doubleValue();
			return a*b; 
		}

	}
	public void scoreCalc(boolean flag){
		//Cosine Similarty
		//Formula is cosineSimilarity=dotProduct/magd1*magd2
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

	public int totalTermFinder(String fileName){
		//Calculates and return word count of a specifed file.
		int wordCount=0;
		for (int i = 0; i < fileList.size(); i++) {
			if(fileName.equalsIgnoreCase(fileList.get(i).getName())){
				wordCount=wordCountList.get(i);
			}

		}
		return wordCount;
	}
	

	public void sortFiles(){
		double key=0;
		int i=0;
		double[][] arrayTempForPosition;
		int countForSize=0;
		for (int j = 1; j < arrayTokenWeight[0].length; j++){
			//counts scores which aren't 0, for the calculatig size for temp array .
			if(arrayTokenWeight[arrayTokenWeight.length-1][j]!=0.0)
				countForSize++;
		}
		arrayTempForPosition=new double[2][countForSize];
		//This temp array keeps scores arrenged in order with files. 
		
		for (int j = 1; j < arrayTokenWeight[0].length; j++){
			if(arrayTokenWeight[arrayTokenWeight.length-1][j]!=0.0){
				
				arrayTempForPosition[0][i]=j-1;//takes file slot
				arrayTempForPosition[1][i]=arrayTokenWeight[arrayTokenWeight.length-1][j];//takes score
				i++;
			}
		}
		i=0;	
		for (int j = 1; j < arrayTokenWeight[0].length; j++) {
			//Insertion Sort

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
		/*Compares sorted scores with temp of unsorted scores. When there is a match 
			algorithm takes file slot of score from temp.
				then take file from main FILE ARRAY(arrayFileRow).*/
			
		for (int i = 0; i < arrayTokenWeight[0].length-1; i++) {
			for (int j = 0; j < arrayTempForPosition[0].length; j++) {
				if(arrayTokenWeight[arrayTokenWeight.length-1][i]==arrayTempForPosition[1][j]&&flag==false){
					//Compares sorted scores with temp of unsorted scores. When there is a match 
					flag=true;
					//algorithm takes file slot of score from temp.
					//then take file from main FILE ARRAY(arrayFileRow).
					System.out.print("file: "+arrayFileRow[(int)arrayTempForPosition[0][j]]);
					System.out.println(" "+BigDecimal.valueOf(arrayTokenWeight[arrayTokenWeight.length-1][i]).toPlainString());
				}
			}
			flag=false;
		}



	}
	public void sumCollision(){
		System.out.println();
		System.out.println("Sum collision is " + collision_counter);

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





}
