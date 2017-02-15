import java.util.*;
import java.math.*;
import java.io.*;
public class HashTable {

	private List2[] slotList;
	private int HashTableSize;
	private String fileName;
	private int hashIndexValue;
	private int hashUniqKey;
	private int directoryFileCount;//tf-idf ile eklendi.
	private double[][] arrayTokenWeight;//tf-idf ile eklendi.
	private String[] arrayFileRow;//tf-idf ile eklendi.
	private ArrayList<File> fileList;//tf-idf ile eklendi.
	private ArrayList<Integer> wordCountList;//tf-idf ile eklendi.
	private Node2 tokenNodes[];//tf-idf ile eklendi.
	private int collision_counter;
	public HashTable(int size ){
		HashTableSize=size;
		slotList=new List2[size];
		this.collision_counter=0;

	}

	public void hash(ArrayList<String> wordsList,ArrayList<File> fileList,ArrayList<Integer> wordCountList){
		int addition=0;
		this.fileList=fileList;//tf-idf ile eklendi.
		this.wordCountList=wordCountList;//tf-idf ile eklendi.
		directoryFileCount=fileList.size();
		
		for(int j=0;j<fileList.size();j++){
			
			fileName=fileList.get(j).getName();
			if(j!=0)
				addition=addition+wordCountList.get(j-1);
			for (int i = 0; i < wordCountList.get(j); i++) {
			
			hashFunc(wordsList.get(i+addition),true);

			}
		}
	}

	public void hashFunc(String wordHash,boolean flag){
		//Does hash

		hashIndexValue=0;//holds hashed index value by modulo.
		hashUniqKey=0;//holds uniq int value for strings.

		for (int i = 0; i < wordHash.length(); i++) {

			int charCode=wordHash.charAt(i)-96;
			/*Takes letters one by one
			 * a is 97 in unicode so charCode values starts with 1 and goes on(1,2,3,4..)  
			 */
			if(charCode<0){//charcode can only be lesser than zero if char is number.
				charCode=Math.abs(charCode);
			}
			hashUniqKey=(hashUniqKey*27+charCode);
			hashIndexValue=(hashIndexValue*27+charCode)%HashTableSize;

		}
		if(flag==true){
		insert(wordHash,hashIndexValue,hashUniqKey);
		}
	}

	public void insert(String word,int indexValue,int uniqKeyID){
		if(slotList[indexValue]==null){
			slotList[indexValue]=new List2();
		}

		slotList[indexValue].add(word,fileName,uniqKeyID);


	}
	public void search(String input){
		hashFunc(input,false);
		
		slotList[hashIndexValue].searchToFind(hashUniqKey);
	}
	public void search2(String[] tokens, ArrayList<Integer> tokensQuantity){
		//This For Score and Ranking
		
		int[] arrayTokenSlotKey=new int[tokens.length];
		int mostFileCount=0;
		int tokenTheMostFileWith=0;
		tokenNodes=new Node2[tokens.length];

		for (int i = 0; i < tokens.length; i++) {
			hashFunc(tokens[i],false);
			
			if(slotList[hashIndexValue].searchToFind(hashUniqKey)!=null){
				arrayTokenSlotKey[i]=hashIndexValue;
				tokenNodes[i]=slotList[hashIndexValue].searchToFind(hashUniqKey);
			}
			else{
				System.out.println("Couldn't find");
				return;
			}
			
		}

		int fileCountOfToken;//Tokenin dosya sayýsý
		
		for(int i=0;i<arrayTokenSlotKey.length;i++){
			fileCountOfToken=tokenNodes[i].getFileAmount();
			if(fileCountOfToken>mostFileCount){
				mostFileCount=fileCountOfToken;
				tokenTheMostFileWith=i;
			}
		}

		arrayTokenWeight=new double[tokens.length+1][mostFileCount+1];
		arrayFileRow=tokenNodes[tokenTheMostFileWith].getFileNames().toArray(new String[tokenNodes[tokenTheMostFileWith].getFileNames().size()]);



		for (int i = 0; i < arrayTokenSlotKey.length; i++) {
			fileCountOfToken=tokenNodes[i].getFileAmount();
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
		String[] arrayFileNamesOfToken=tokenNodes[tokenRow].getFileNames().toArray(new String[tokenNodes[tokenRow].getFileNames().size()]);
		Integer[] arrayWordCountOfFiles=tokenNodes[tokenRow].getWordCountAtFiles().toArray(new Integer[tokenNodes[tokenRow].getWordCountAtFiles().size()]);

		boolean flag=false;
		boolean flag2=false;

		for (int i = 0; i < arrayFileRow.length; i++) {
			for (int j = 0; j < arrayFileNamesOfToken.length; j++) {
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
	public void sumCollision(){
		for (int i = 0; i < slotList.length; i++) {
			if(slotList[i]!=null){
				collision_counter = collision_counter +slotList[i].getCollision_counter();
			}
		}
		System.out.println();
		System.out.println("Sum collision is "+collision_counter);
	}
	
		
		
		
	
	
	

}
