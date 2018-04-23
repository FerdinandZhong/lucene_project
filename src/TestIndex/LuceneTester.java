/**
 *
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;


public class LuceneTester {
	
	/** Define the paths for the data file and the lucene index */
	public static final String BUSINESSDATA_FILE="./dataset/business.json";
	public static final String USERDATA_FILE="./dataset/user.json";
	public static final String REVIEWDATA_FILE="./dataset/review.json";
	public static final String TIPDATA_FILE="./dataset/tip.json";
	public static final String INDEX_PATH="./luceneIndex";
	
	public static final int BOOLEAN_QUERY_TYPE =0;
	public static final int DISTANCE_QUERY_TYPE =1;
	public static final int RANGE_QUERY_TYPE=2;
	
	
	public static void main (String[] arg) throws Exception{
		
		//only the first time need to be true
		boolean preformIndex=true;
		
		if(preformIndex){
			QAIndexer indexer = new QAIndexer(LuceneTester.INDEX_PATH);
			String[] indexPaths = {BUSINESSDATA_FILE, USERDATA_FILE, REVIEWDATA_FILE, TIPDATA_FILE};
			indexer.indexAllfiles(indexPaths);
		}
		
		//search index
		QASearcher searcher=new QASearcher(LuceneTester.INDEX_PATH);
		ScoreDoc[] hits = null;
		
		List<SearchQuery> queries = new ArrayList<SearchQuery>();
		/*----------create 20 queries, and retrieve top 10 results----------*/
		//1: Restaurant named “Village Juicery”
		queries.add(new SearchQuery("name", "Village Juicery", 0,"AND"));
		//2: Business with stars from 4.5 to 4.8
		//queries.add(new SearchQuery("stars", 4.5, 4.9, RANGE_QUERY_TYPE, "OR"));
		//3: See the Dentist
		//queries.add(new SearchQuery("categories", "Dentists", BOOLEAN_QUERY_TYPE,"AND"));
		//4: Find the Location of the place
		//queries.add(new SearchQuery("location", 35.0, -80.0, 300000.0, DISTANCE_QUERY_TYPE, "AND"));
		// 5: Business with review count from 22 to 100 
		//queries.add(new SearchQuery("review_count", 22.0, 100.0, RANGE_QUERY_TYPE, "OR"));
		//6: Places with free wifi 
		//queries.add(new SearchQuery("WiFi", "free", BOOLEAN_QUERY_TYPE,"AND"));
		//7: Restaurant serving seafood
		//queries.add(new SearchQuery("categories", "seafood", BOOLEAN_QUERY_TYPE,"AND"));
		//8: Counselling for property management
		//queries.add(new SearchQuery("categories", "Property Management", BOOLEAN_QUERY_TYPE,"AND"));
		//9: Business near university city
		//queries.add(new SearchQuery("neighborhood", "University City", BOOLEAN_QUERY_TYPE,"AND"));
		//10: searching for fast food
		//queries.add(new SearchQuery("text", "fast  food", RANGE_QUERY_TYPE,"AND"));
		//11:  searching for Haircut
		//queries.add(new SearchQuery("text", "Haircut", RANGE_QUERY_TYPE,"AND")); 
		//12: Restaurant serving buffet
		//queries.add(new SearchQuery("text", "buffet", RANGE_QUERY_TYPE,"AND")); 
		//13: Restaurant serving Chinese Food
		//queries.add(new SearchQuery("text", "Chinese Food", RANGE_QUERY_TYPE,"AND")); 
		//14: Restaurant serving western Food
		//queries.add(new SearchQuery("text", "western Food", RANGE_QUERY_TYPE,"AND")); 
		//15: Iphone purchasing 
		//queries.add(new SearchQuery("text", "iPhone", RANGE_QUERY_TYPE,"AND")); 
		//16: Women's Clothing
		//queries.add(new SearchQuery("text", "Women's Clothing", RANGE_QUERY_TYPE,"AND")); 	
		//17: Store for sport shoes
		//queries.add(new SearchQuery("text", "sport shoes", RANGE_QUERY_TYPE,"AND")); 	
		//18: Medical clinic for human being 
		//queries.add(new SearchQuery("text", "medical clinic", RANGE_QUERY_TYPE,"AND")); 
		//19: Massage services
		//queries.add(new SearchQuery("text", "massage", RANGE_QUERY_TYPE,"AND"));
		//20: Romantic place
		//queries.add(new SearchQuery("text", "romantic", RANGE_QUERY_TYPE,"AND"));
		
		SearchQuery[] queryArray = queries.toArray(new SearchQuery[0]);
		Query query = searcher.createQuery(queryArray);
		/*---------------------2 retrieval model used---------------------*/
		System.out.println("Retrieval Model: BM25Similarity");
		hits=searcher.search(query, 5, new BM25Similarity());
		searcher.printResult(hits);
		searcher.explain(query, hits);

		System.out.println("Retrieval Model: TFIDFSimilarity");
		hits=searcher.search(query, 5, new ClassicSimilarity());
		searcher.printResult(hits);
		searcher.explain(query, hits);
	}
	
}
