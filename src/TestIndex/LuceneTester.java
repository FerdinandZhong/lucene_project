/**
 * @author axsun
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.search.ScoreDoc;

public class LuceneTester {

	/** Define the paths for the data file and the lucene index */
	public static final String BUSINESSDATA_FILE="C:\\Users\\Sruthi\\Documents\\MSA\\Trimester 3\\IR\\Project\\yelp_dataset\\yelp_dataset\\dataset\\business.json";
	public static final String USERDATA_FILE="C:\\Users\\Sruthi\\Documents\\MSA\\Trimester 3\\IR\\Project\\yelp_dataset\\yelp_dataset\\dataset\\user.json";
	public static final String REVIEWDATA_FILE="C:\\Users\\Sruthi\\Documents\\MSA\\Trimester 3\\IR\\Project\\yelp_dataset\\yelp_dataset\\dataset\\review.json";
	public static final String TIPDATA_FILE="C:\\Users\\Sruthi\\Documents\\MSA\\Trimester 3\\IR\\Project\\yelp_dataset\\yelp_dataset\\dataset\\tip.json";
	public static final String INDEX_PATH="C:\\Users\\Sruthi\\Documents\\MSA\\Trimester 3\\IR\\Project\\LuceneTest\\luceneYelpIndex";
	
	
	public static void main (String[] arg) throws Exception{
	
		boolean preformIndex=false;
		
		// To perform indexing. If there is no change to the data file, index only need to be created once 

		if(preformIndex){
			QAIndexer indexer = new QAIndexer(LuceneTester.INDEX_PATH);
			indexer.indexBusiness(LuceneTester.BUSINESSDATA_FILE);
			//indexer.indexUser(LuceneTester.USERDATA_FILE);
			//indexer.indexReview(LuceneTester.REVIEWDATA_FILE);
			//indexer.indexTip(LuceneTester.TIPDATA_FILE);
		}
		
		//search index
		QASearcher searcher=new QASearcher(LuceneTester.INDEX_PATH);
		
		//search for keywords in field "question", and request for the top 20 results
		ScoreDoc[] hits=searcher.search("name", "Beauty OR Salon", 5 ,"B");
		searcher.printResult(hits);
		
		//phrase query in name field
		//hits=searcher.search("name", "Beauty Salon", 5 , "P");
		//searcher.printResult(hits);
		
		//search for keywords in "answer" field
		//hits=searcher.search("city", "Las Vegas", 5 , "B");
		//searcher.printResult(hits);
		
		//current idea.
		//later i will think about put all queries in a json file and read this json file to get all queries and search one by one.
		List<SearchQuery> queries = new ArrayList<SearchQuery>();
		queries.add(new SearchQuery("city", "Charlotte"));
		queries.add(new SearchQuery("stars", "3.0"));
		SearchQuery[] queryArray = queries.toArray(new SearchQuery[0]);
		hits=searcher.search(queryArray, 5 ,"B");
		searcher.printResult(hits);
				
		//phrase query in name field
		hits=searcher.search(queryArray, 5 , "P");
		searcher.printResult(hits);
				
		// DPoints within 30 km from colombo city
		System.out.println("Businesses within 3 kms of specified latitude-longitude ");
		hits = searcher.search(35.0, -80.0, 3000.0, 5);
		searcher.printResult(hits);
		
		
	}
	
}
