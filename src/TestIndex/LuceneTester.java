/**
 * @author axsun
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
		
		//current idea.
		//later i will think about put all queries in a json file and read this json file to get all queries and search one by one.
		List<SearchQuery> queries = new ArrayList<SearchQuery>();
		queries.add(new SearchQuery("address", "University City",0, "AND")); 
		queries.add(new SearchQuery("stars", 3.2, 4.6, 2, "OR"));
		queries.add(new SearchQuery("location", 35.0, -80.0, 300000.0, 1, "AND"));
		SearchQuery[] queryArray = queries.toArray(new SearchQuery[0]);
		Query query = searcher.createQuery(queryArray);
		//retrieval model 1
		System.out.println("Retrieval Model: BM25Similarity");
		hits=searcher.search(query, 5, new BM25Similarity());
		searcher.printResult(hits);
		searcher.explain(query, hits);
		
		System.out.println("Retrieval Model: BooleanSimilarity");
		hits=searcher.search(query, 5, new BooleanSimilarity());
		searcher.printResult(hits);
		searcher.explain(query, hits);
		
		System.out.println("Retrieval Model: TFIDFSimilarity");
		hits=searcher.search(query, 5, new ClassicSimilarity());
		searcher.printResult(hits);
		searcher.explain(query, hits);
	}
	
}
