/**
 * @author axsun
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import javafx.util.Pair;

public class QASearcher {

	private IndexSearcher lSearcher;
	private IndexReader lReader;

	public QASearcher(String dir) {
		try {
			//create an index reader and index searcher
			lReader = DirectoryReader.open(FSDirectory.open(Paths.get(dir)));
			lSearcher = new IndexSearcher(lReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//report the number of documents indexed 
	public int getCollectionSize() {
		return this.lReader.numDocs();
	}

	//search for keywords in specified field, with the number of top results 
	public ScoreDoc[] search(String field, String keywords, int numHits , String queryType) {
		
		//the query has to be analyzed the same way as the documents being index 
		//using the same Analyzer 
		QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
		Query query;
		if(queryType == "B")
		{
			 query = builder.createBooleanQuery(field, keywords);
			 /*BooleanClause[] clause = new BooleanClause[]();
			 BooleanQuery booleanQuery = new BooleanQuery(numHits, clause);*/
			 //booleanQuery.add(query, BooleanClause.Occur.MUST);
			 //QueryBuilder builder1 = new QueryBuilder(new Analyzer());
		}
		else
		{
			 query = builder.createPhraseQuery(field, keywords);
		}
		ScoreDoc[] hits = null;
		try {
			//Create a TopScoreDocCollector 
			TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);
			
			//search index
			lSearcher.search(query, collector);
			
			//collect results
			hits = collector.topDocs().scoreDocs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hits;
	}
	
	public ScoreDoc[] search(SearchQuery[] textQueries, int numHits, String queryType) {

		// the query has to be analyzed the same way as the documents being
		// index
		// using the same Analyzer
		QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
		Query multifieldsQuery;
		BooleanQuery.Builder multiFieldBuilder = new BooleanQuery.Builder();
		try {
			if (queryType == "B") {
				//keywords can be string[]
				//fields can also be string[]
				//i think for each field, the query keywords can be more than one word, and then for each field is a boolean query
				//Then combine all fields queries using boolean query 
				for(SearchQuery textQuery: textQueries ) {
					Query query = builder.createBooleanQuery(textQuery.getField(), textQuery.getContents());
					multiFieldBuilder.add(query, BooleanClause.Occur.MUST);
				}
				multifieldsQuery = multiFieldBuilder.build();
			} else {
				//if we want to search for multiple fields using phrase query, it will be like this,
				//query for every field is a phrase query
				//but combined with other field's phrase query using boolean query
				for(SearchQuery textQuery: textQueries ) {
					Query query = builder.createPhraseQuery(textQuery.getField(), textQuery.getContents());
					multiFieldBuilder.add(query, BooleanClause.Occur.MUST);
				}
				multifieldsQuery = multiFieldBuilder.build();
			}
			ScoreDoc[] hits = null;
			// Create a TopScoreDocCollector
			TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);

			// search index
			lSearcher.search(multifieldsQuery, collector);

			// collect results
			hits = collector.topDocs().scoreDocs;
			return hits;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//present the search results
	public void printResult(ScoreDoc[] hits) {
		int i = 1;
		for (ScoreDoc hit : hits) {
			System.out.println("\nResult " + i + "\tDocID: " + hit.doc + "\t Score: " + hit.score);
			try {
				System.out.println("Business Name: " + lReader.document(hit.doc).get("name"));
				System.out.println("Business City: " + lReader.document(hit.doc).get("city"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			i++;

		}
	}

	public void close() {
		try {
			if (lReader != null) {
				lReader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
