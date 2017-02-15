import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class FileOp {


	private BufferedReader bufferedReader=null;
	private ArrayList<String> wordList;
	private ArrayList<File> fileList;
	private ArrayList<Integer> wordCountList;
	private int choose;

	private boolean flag=false;
	private String line=null;
	private String StopWords[]={"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"} ;
	private Scanner scn=new Scanner(System.in);
	private HashTable2  hash_Double;///double
	private HashTable hashT;////chaining
	private HashTableLinear  hash_linear;///linear
	private HashTableQuadratic  hash_quadratic;///quadratic
	private int countForQuary=0;
	private long fileOperationTime=0;
	private int cleanTotalWords=0;
	public void fileRead() throws FileNotFoundException{
		/* 
		 * 
		 * MOST OF THE COMMENTS HASHTABLE2 
		 * THEY ARE SLIGHTLY the SAME DESCRIPTOR FOR OTHER HASHTABLES
		 * 
		 * */
		long startTime=System.currentTimeMillis();
		wordList=new ArrayList<String>();
		fileList=new ArrayList<File>();
		wordCountList=new ArrayList<Integer>();
		
		int count=0;//counts the words that belongs to a file.
		try{



			File files=new File("C:\\Users\\oguzh\\Desktop\\2-Proje\\txts");
			File txts[]=files.listFiles();//Dosyalarý alýr
			//line=bufferedReader.readLine();

			for (int t = 0; t < txts.length; t++) {

				fileList.add(txts[t]);

				//Dosya sayýsýna göre for döndürür
				//bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(txts[t].getPath())));
				bufferedReader=new BufferedReader(new FileReader(txts[t].getPath()));
				//int counterr=0;
				while((line=bufferedReader.readLine())!=null){
					line=line.replaceAll("I|ý", "i");//ý'larý i yapar
					line=line.replaceAll("\\.|,|\\[|\\|']|-", " ");
					line=line.replaceAll("[^\\w\\s]","");//Latin harfleri ve sayýlar haricinde txt temizler
					String words[]=line.toLowerCase().split("\\s++");//Her satýrda ki boþluklarý siler

					for (int i = 0; i < words.length; i++) {
						if(line.equals("")){
							//Satýr boþ ise döngü atlatýr.
							break;
						}
						if(words[i].length()<=2){
							continue;
						}


						if(words[i].charAt(0)>96){
							for (int j = 0; j <  StopWords.length; j++) {
								if(words[i].equalsIgnoreCase(StopWords[j])){
									flag=true;
									break;
								}
							}
						}
						else{
							break;
						}


						if(flag==false)
						{
							//stop word göstermez.
							wordList.add(words[i]);
							count++;

						}
						else{
							flag=false;
						}
					}

				}
				wordCountList.add(count);
				count=0;
			}
			int totalWord=0;

			for (int i = 0; i < wordCountList.size(); i++) {
				totalWord=totalWord+wordCountList.get(i);
			}
			
			long endTime=System.currentTimeMillis();

			cleanTotalWords=totalWord;
			fileOperationTime=endTime-startTime;
			
			System.out.println(" ********  Menu  ***********");
			System.out.println("1- Chaining ");
			System.out.println("2- Double Hashing ");
			System.out.println("3- Linear Hashing ");
			System.out.println("4- Quadratic Hashing ");
			System.out.println("5- Run Times");
			System.out.println("6- Exit");
			System.out.print("Please enter number: ");
			choose=scn.nextInt();
			System.out.println();

			switch(choose){
			case 1:

				hashT=new HashTable(10107);
				hashT.hash(wordList,fileList,wordCountList);//CHANÝNG TABLE****
				while(countForQuary<100){
					Quary();
					hashT.sumCollision();
					countForQuary++;
				}

				break;
			case 2: 

				hash_Double=new HashTable2(9901);//OPEN ADRESÝNG TABLE
				hash_Double.doubleHash(wordList, fileList, wordCountList);	

				while(countForQuary<100){
					Quary();
					hash_Double.sumCollision();
					countForQuary++;
				}
				
				break;
			case 3:

				hash_linear = new HashTableLinear(9901);///Linear open adressing
				hash_linear.LinearHash(wordList, fileList, wordCountList);
				while(countForQuary<100){
					Quary();
					hash_linear.sumCollision();
					countForQuary++;
				}

				break;
			case 4:

				hash_quadratic=new HashTableQuadratic(9901);
				hash_quadratic.QuadraticHash(wordList, fileList, wordCountList);
				while(countForQuary<100){
					Quary();
					hash_quadratic.sumCollision();
					countForQuary++;
				}

				break;
			case 5: 
				hashT=new HashTable(10107);
				hash_Double=new HashTable2(9901);//OPEN ADRESÝNG TABLE
				hash_linear = new HashTableLinear(9901);///Linear open adressing
				hash_quadratic=new HashTableQuadratic(9901);
				timesForHashing();
				break;

			case 6:
				break;
			}

		}catch(Exception e){
			e.printStackTrace();
		}


	}
	public void Quary(){
		int count=1;
		scn=new Scanner(System.in);
		String input;

		System.out.print("Search: " );
		input=scn.nextLine();
		if(input.equalsIgnoreCase("exit")){
			return;
		}
		input=input.replaceAll("I|ý", "i");//ý'larý i yapar
		input=input.replaceAll("\\.|,|\\[|\\|']|-", " ");
		input=input.replaceAll("[^\\w\\s]","");

		String tokens[]=input.toLowerCase().split("\\s++");

		String tokensWithoutStops="";
		String tokensWithoutRep="";

		for (int i = 0; i < tokens.length; i++) {

			if(tokens[i].length()<=2){
				continue;
			}

			for (int j = 0; j < StopWords.length; j++) 
			{
				//stop wordsleri tarar

				if(tokens[i].charAt(0)>96){
					if(tokens[i].equalsIgnoreCase(StopWords[j])){
						flag=true;
						break;
					}

				}
				else{//If word is a number than exit from loop
					break;
				}
			}
			if(flag==false)
			{
				//stop word göstermez.
				tokensWithoutStops=tokensWithoutStops+" "+tokens[i];

			}
			else{
				flag=false;
			}

		}

		tokens=null;
		flag=false;
		tokensWithoutStops=tokensWithoutStops.trim();
		tokens=tokensWithoutStops.split("\\s++");
		ArrayList<Integer> tokensQuantity = new ArrayList<Integer>();
		for (int i = 0; i < tokens.length; i++) {
			for (int j = i+1; j < tokens.length; j++) {

				if(tokens[i].equalsIgnoreCase(tokens[j]))
				{	tokens[j]=null;
				flag=true;
				count++;
				}
			}
			if(flag==false&&tokens[i]!=null){
				tokensWithoutRep=tokensWithoutRep+" "+tokens[i];
				tokensQuantity.add(1);

			}else if(tokens[i]!=null){

				tokensQuantity.add(count);
				count=0;
				flag=false;

			}
		}
		tokens=null;
		tokensWithoutRep=tokensWithoutRep.trim();
		tokens=tokensWithoutRep.split("\\s++");
		if(choose ==1){
			hashT.search2(tokens, tokensQuantity);
		}
		if(choose==2){
			hash_Double.search(tokens, tokensQuantity);
		}
		else if(choose==3){
			hash_linear.search(tokens, tokensQuantity);
		}
		else if(choose==4){
			hash_quadratic.search(tokens, tokensQuantity);
		}



	}
	public void timesForHashing(){
		long timeChaining=0;
		long timeLinear=0;
		long timeDouble=0;
		long timeQuadreatic=0;
		long start;
		long end;
		
		
		start=System.currentTimeMillis();
		hashT.hash(wordList,fileList,wordCountList);//CHANÝNG TABLE****
		end=System.currentTimeMillis();
		timeChaining=end-start;
		
		
		start=System.currentTimeMillis();
		hash_linear.LinearHash(wordList, fileList, wordCountList);
		end=System.currentTimeMillis();
		timeLinear=end-start;
		
		
		start=System.currentTimeMillis();
		hash_Double.doubleHash(wordList, fileList, wordCountList);	
		end=System.currentTimeMillis();
		timeDouble=end-start;
		
		
		start=System.currentTimeMillis();
		hash_quadratic.QuadraticHash(wordList, fileList, wordCountList);
		end=System.currentTimeMillis();
		timeQuadreatic=end-start;
		
		System.out.println("File Operation Time: "+fileOperationTime);
		System.out.println();
		
		System.out.println("Total words without Stop words: "+cleanTotalWords);
		System.out.println();
		
		
		System.out.print("timeChaining: " +timeChaining +"   Chaining" );
		hashT.sumCollision();
		System.out.println();
		
		System.out.print("timeLinear: " +timeLinear+"  Linear");
		hash_linear.sumCollision();
		System.out.println();
		
		System.out.print("timeDouble: " +timeDouble+"   Double");
		hash_Double.sumCollision();
		System.out.println();
		
		System.out.print("timeQuadreatic: " +timeQuadreatic+"   Quadreatic");
		hash_quadratic.sumCollision();
		System.out.println();
	}












}
