/**
 * @author axsun
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.ScoreDoc;

public class LuceneTester {
	
	/** Define the paths for the data file and the lucene index */
	public static final String BUSINESSDATA_FILE="./dataset/business.json";
	public static final String USERDATA_FILE="./dataset/user.json";
	public static final String REVIEWDATA_FILE="./dataset/review.json";
	public static final String TIPDATA_FILE="./dataset/tip.json";
	public static final String INDEX_PATH="./luceneIndex";
	
	
	public static void main (String[] arg) throws Exception{
	
		boolean preformIndex=true;
		
		// To perform indexing. If there is no change to the data file, index only need to be created once 
		String filePath = new File("").getAbsolutePath();
		System.out.println(filePath);
		if(preformIndex){
			QAIndexer indexer = new QAIndexer(LuceneTester.INDEX_PATH);
			String[] indexPaths = {BUSINESSDATA_FILE, USERDATA_FILE, REVIEWDATA_FILE, TIPDATA_FILE};
			indexer.indexAllfiles(indexPaths);
		}
		
		//search index
		QASearcher searcher=new QASearcher(LuceneTester.INDEX_PATH);
		ScoreDoc[] hits = null;
		//search for keywords in field "question", and request for the top 20 results
		hits = searcher.search("stars", "4.5", 5 ,"R");
		searcher.printResult(hits);
		
		//current idea.
		//later i will think about put all queries in a json file and read this json file to get all queries and search one by one.
		List<SearchQuery> queries = new ArrayList<SearchQuery>();
		queries.add(new SearchQuery("neighborhood", "Starmount"));
		queries.add(new SearchQuery("stars", "5.0"));
		SearchQuery[] queryArray = queries.toArray(new SearchQuery[0]);
		hits=searcher.search(queryArray, 5 ,"B");
		searcher.printResult(hits);
				
		//phrase query in name field
		hits=searcher.search(queryArray, 5 , "P");
		searcher.printResult(hits);
				
		//search for keywords in "answer" field
		//hits=searcher.search(queryArray, 5 , "B");
		//searcher.printResult(hits);
		
		
	}
	
}
