import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Scanner;

public class Main
{
	
	/*!!!WARNING!!! PLEASE WRİTE NECESSARY PATH AT THE LINE 19, 139, 250 */
	public static void main(String[] args) throws IOException
	{
		String pathOfFolder = "D:/sport"; //   !!!WARNING!!! Please write "sport" FOLDER path.
        Scanner scn = new Scanner(System.in);
		int b = new File(pathOfFolder).listFiles().length;
		int index = -1;
		HashedDictionary<ArrayList<Integer>> dictionary = new HashedDictionary(2477);
		ArrayList<Integer> dictionaryArrayList = new ArrayList(b);
		ArrayList<Integer> test = new ArrayList(b);
		
		for(int i = 0 ; i< b; i++)
		{
			dictionaryArrayList.add(0);
			test.add(0);
		}
		ArrayList<String> txtFilesNames = new ArrayList<String>();
		String DELIMITERS = "[-+=" +

		        " " +        //space

		        "\r\n " +    //carriage return line fit

				"1234567890" + //numbers

				"’'\"" +       // apostrophe

				"(){}<>\\[\\]" + // brackets

				":" +        // colon

				"," +        // comma

				"‒–—―" +     // dashes

				"…" +        // ellipsis

				"!" +        // exclamation mark

				"." +        // full stop/period

				"«»" +       // guillemets

				"-‐" +       // hyphen

				"?" +        // question mark

				"‘’“”" +     // quotation marks

				";" +        // semicolon

				"/" +        // slash/stroke

				"⁄" +        // solidus

				"␠" +        // space?   

				"·" +        // interpunct

				"&" +        // ampersand

				"@" +        // at sign

				"*" +        // asterisk

				"\\" +       // backslash

				"•" +        // bullet

				"^" +        // caret

				"¤¢$€£¥₩₪" + // currency

				"†‡" +       // dagger

				"°" +        // degree

				"¡" +        // inverted exclamation point

				"¿" +        // inverted question mark

				"¬" +        // negation

				"#" +        // number sign (hashtag)

				"№" +        // numero sign ()

				"%‰‱" +      // percent and related signs

				"¶" +        // pilcrow

				"′" +        // prime

				"§" +        // section sign

				"~" +        // tilde/swung dash

				"¨" +        // umlaut/diaeresis

				"_" +        // underscore/understrike

				"|¦" +       // vertical/pipe/broken bar

				"⁂" +        // asterism

				"☞" +        // index/fist

				"∴" +        // therefore sign

				"‽" +        // interrobang

				"※" +          // reference mark

		        "]";

		String[] splitted = null;
		String text = "";
		ArrayList<String> stopWords= new ArrayList<>();
		String linesw;
		long sum = 0;
		ArrayList<String> textArrayList = new ArrayList<String>();
		
		//Stop words file reading.
		FileReader frsw = new FileReader("C:/Users/vsayd/Downloads/stop_words_en.txt"); //   !!!WARNING!!! Please write "stop words" FILE path.
		BufferedReader brsw = new BufferedReader(frsw);
		
		
		while((linesw = brsw.readLine()) != null)
		{
			if(!linesw.equals(""))
				stopWords.add(linesw);
			
		}
		brsw.close();
		
		//File reading and adding operations.
		try
		{
			
			File sourceFolder = new File(pathOfFolder);
			String fileExt = "";

			for (File sourceFile: sourceFolder.listFiles())
			{
				String fileName = sourceFile.getName();
				fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
				
				if(fileExt.equalsIgnoreCase("txt"))
				{
					
					String path = pathOfFolder+"/"+fileName;
					FileReader fr = new FileReader(path);
					BufferedReader br = new BufferedReader(fr);
					String line;
					while((line = br.readLine()) != null)
					{
			    	
						text=text+line;
					}
					
					System.out.println("The file "  +fileName+ " has been read. Necessary process have been taken.");
					txtFilesNames.add(fileName);
					index+=1;
					text=text.toLowerCase();
					splitted = text.split(DELIMITERS);
					text="";
					textArrayList.clear();
					for(int i=0; i<splitted.length;i++)
					{
						textArrayList.add(splitted[i]);
					}
					
					
					removeStopWords(textArrayList, stopWords);
					
					//Adding operation and calculating the adding time.
					long startTime = System.currentTimeMillis();
					for(String word: textArrayList)
					{

						if(word.isEmpty())
						{
							continue;
						}
						else
						{
							if(!dictionary.contains(word))
							{
								ArrayList<Integer> temp = new ArrayList();
								for(int i = 0 ; i<b; i++)
								{
									temp.add(0);
								}
								temp.set(index, 1);
								dictionary.add(word, temp);


							}
							
							else
							{
								dictionaryArrayList = dictionary.getValue(word);
								int m = dictionary.getValue(word).get(index);
								dictionaryArrayList.set(index, m+1);
								dictionary.add(word, dictionaryArrayList);
							}
						}
						
						dictionaryArrayList = new ArrayList(b);
						for(int i = 0 ; i<b; i++)
						{
							dictionaryArrayList.add(0);
						}
						
						
					}
					
					long stopTime = System.currentTimeMillis();
					sum = sum + (stopTime-startTime);
				
				}
				
				else
				{
					System.out.println("Filename extension not supported");
				}
				
			}
			
			
			//The words to be searched are stored.
			ArrayList<String> searchingWords= new ArrayList<>();
			String linesrch;
			
			FileReader frsrch = new FileReader("D:/search.txt"); //!!!WARNING!!! Please write "searching words" FILE path.
			BufferedReader brsrch = new BufferedReader(frsrch);
			
			
			while((linesrch = brsrch.readLine()) != null)
			{
				if(!linesrch.equals(""))
					searchingWords.add(linesrch);
				
			}
			brsrch.close();
			
			//You can unmark the comments to see the search time and searching words.
			/*
			ArrayList<Long> searchingTime = new ArrayList(); //The search time information of each word is stored.
			
			for(String i : searchingWords)
			{
				long startSearchTime = System.nanoTime();
				ArrayList<Integer> f = dictionary.search(i);
				if(f!= null)
				{
					System.out.println(i);
					
					for(int j : f)
					{
						System.out.print(j+" ");
					}
					
					System.out.println();
						
				}
				long stopSearchTime = System.nanoTime();
				searchingTime.add(stopSearchTime - startSearchTime);
				
			}
			*/
			
			
			//If you want, you can unmark the comments to see the values. And remove comment signs of above field.
			/*
			System.out.println("Collision:" + dictionary.collision);
			System.out.println("Adding Time: "+sum);
			System.out.println("Average Searching Time: " + average(searchingTime));
			System.out.println("Min Searching Time: " + Collections.min(searchingTime));
			System.out.println("Max Searching Time: " + Collections.max(searchingTime));
			*/
			
			
			//Search Engine
			ArrayList<String> inputString = new ArrayList();

			int numberOfWords = 0;
			String z;
			System.out.println("Please Enter 3 Words (Must Contain Only Lowercase Letters) : ");
			while(numberOfWords<3)
			{
				System.out.println("Please Enter " +(numberOfWords+1) + ". Words: ");
				z = scn.next();
				
				if(dictionary.getValue(z)==null)
				{
					System.out.println("The word you entered is invalid.");
				}
				
				else
				{
					inputString.add(z);
					numberOfWords+=1;
				}
							
			}
				
			
			ArrayList<String> firstWordTexts = new ArrayList();
			int numberOfIndex = 0;
			
			//It was determined in which files the words were used.
			for(String i : inputString)
			{
				ArrayList<Integer> testOfFor = dictionary.getValue(i);
				if(testOfFor!=null)
				{
					for(int k: testOfFor)
					{
						if(k>0)
						{
							firstWordTexts.add(txtFilesNames.get(numberOfIndex));
						}
						
						numberOfIndex+=1;
						
					}
					
					numberOfIndex = 0;
				}
				
				
			}
			
			//You can remove the comment marks to print in which files the words include.
			/*
			System.out.println();
			if(firstWordTexts!=null)
			{
				for(String i : firstWordTexts)
				{
					System.out.print(i+" ");
				}
			}
			*/
			
			//I used the hash dictionary structure to store the number of times each file has passed.
			HashedDictionary<Integer> searchEngine = new HashedDictionary();
			
			for(String i : firstWordTexts)
			{
				if(!searchEngine.contains(i))
				{
					searchEngine.add(i, 1);
				}
				
				else
				{
					int d = searchEngine.getValue(i);
					d+=1;
					searchEngine.add(i, d);
				}
			}
			
			System.out.println();
						
			ArrayList<String> newList = new ArrayList();
			  
	        for (String i : firstWordTexts) {
	  
	        	if (!newList.contains(i)) {
	  
	                newList.add(i);
	            }
	        }
	        
	        //The most frequent filenames were detected.
	        int q = searchEngine.getValue(newList.get(0));
	        ArrayList<String> newNewList = new ArrayList();
			newNewList.add(newList.get(0));
			newList.remove(0);
			System.out.println();
			if(newList!=null)
			{
				
				for(int i = 0; i<newList.size(); i++)
				{
					if(searchEngine.getValue(newList.get(i))>q)
					{
						newNewList.clear();
						newList.add(newList.get(i));
						q=searchEngine.getValue(newList.get(i));
					}
					
					else if(searchEngine.getValue(newList.get(i))==q)
					{
						newNewList.add(newList.get(i));
					}
					
					}
				
			}
			
			//Density was calculated by dividing the total number of words by the size of the file to find the most relevant file.
			double y = 0;
			String s = "";
			
			for(int i = 0; i<newNewList.size();i++)
			{
				double u = 0;
				int indexOfText = txtFilesNames.indexOf(newNewList.get(i));
				for(int j = 0; j<inputString.size();j++)
				{
					u = u + (double)dictionary.getValue(inputString.get(j)).get(indexOfText);
				}
				
				File f = new File(pathOfFolder+"/"+newNewList.get(i));
				
				u = u/ (double) ((int)f.length());
				
				if(u>y)
				{
					y = u;
					s = newNewList.get(i);
				}
				
				//System.out.println(newNewList.get(i)+": "+u); //You can remove the comment signs to see each file density.
				
				u = 0;
			}
			
			System.out.println("The Most Relevant File For The Words You Are Looking For: "+s);
						
			
			
		}

		catch(Exception e)
		{
		e.printStackTrace();
		}
		
		
		
	}
	
	public static void removeStopWords(ArrayList<String> textArrayList, ArrayList<String> stopWords)
	{
		for(int i = textArrayList.size()-1; i >= 0; i--)
		{
			if(stopWords.contains(textArrayList.get(i)))
			{
				textArrayList.remove(i);
			}
		}
	}
	
	public static long average (ArrayList<Long> t )
	{
		long sum = 0;
		for(int i = 0; i < t.size(); i++)
		    sum += t.get(i);
		
		sum = sum/t.size();
		
		return sum;
		
	}
	
	
}
