/**
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

public class QASearcher {

	private IndexSearcher lSearcher;
	private IndexReader lReader;

	public QASearcher(String dir) {
		try {
			// create an index reader and index searcher
			lReader = DirectoryReader.open(FSDirectory.open(Paths.get(dir)));
			lSearcher = new IndexSearcher(lReader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// report the number of documents indexed
	public int getCollectionSize() {
		return this.lReader.numDocs();
	}

	public Query createQuery(SearchQuery[] textQueries) {
		QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
		Query multifieldsQuery;
		BooleanQuery.Builder multiFieldBuilder = new BooleanQuery.Builder();
		// keywords can be string[]
		// fields can also be string[]
		// i think for each field, the query keywords can be more than
		// one word, and then for each field is a boolean query
		// Then combine all fields queries using boolean query
		for (SearchQuery textQuery : textQueries) {
			Query query = null;
			switch (textQuery.getType()) {
			case 0:
				query = builder.createBooleanQuery(textQuery.getField(), textQuery.getContents());
				break;
			case 1:
				query = LatLonPoint.newDistanceQuery(textQuery.getField(), textQuery.getLatitude(),
						textQuery.getLongitude(), textQuery.getRadius());
				break;
			case 2:
				query = DoublePoint.newRangeQuery(textQuery.getField(), textQuery.getStart(), textQuery.getEnd());
				break;
			}

			if (textQuery.getOperator() == "AND")
				multiFieldBuilder.add(query, BooleanClause.Occur.MUST);
			else if (textQuery.getOperator() == "OR")
				multiFieldBuilder.add(query, BooleanClause.Occur.SHOULD);
			else if (textQuery.getOperator() == "NOT")
				multiFieldBuilder.add(query, BooleanClause.Occur.MUST_NOT);
		}
		multifieldsQuery = multiFieldBuilder.build();

		return multifieldsQuery;
	}

	public ScoreDoc[] search(Query query, int numHits, Similarity similarity) {
		try {
			ScoreDoc[] hits = null;
			// Create a TopScoreDocCollector
			TopScoreDocCollector collector = TopScoreDocCollector.create(numHits);

			// set different similarity for different retrieval model
			lSearcher.setSimilarity(similarity);
			// search index
			lSearcher.search(query, collector);

			// collect results
			hits = collector.topDocs().scoreDocs;
			return hits;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ScoreDoc getDetails(String id, String field) {
		try {
			ScoreDoc[] hits = null;
			// Create a TopScoreDocCollector
			TopScoreDocCollector collector = TopScoreDocCollector.create(1);
			QueryBuilder builder = new QueryBuilder(new StandardAnalyzer());
			Query query = builder.createBooleanQuery(field, id);
			// search index
			lSearcher.search(query, collector);

			// collect results
			hits = collector.topDocs().scoreDocs;
			return hits[0];
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void explain(Query query, ScoreDoc[] hits) throws Exception {
		for (ScoreDoc hit : hits) {
			System.out.println(lSearcher.explain(query, hit.doc));
		}
	}
	
	public void businessPrint(ScoreDoc hit) throws IOException {
		System.out.println("Business Name: " + lReader.document(hit.doc).get("name"));
		System.out.println("Business City: " + lReader.document(hit.doc).get("city"));
		System.out.println("Business Star: " + lReader.document(hit.doc).get("Stars"));
		System.out.println("Business Location: " + lReader.document(hit.doc).get("latitude") + ", " + lReader.document(hit.doc).get("longitude"));
	}
	
	public void userPrint(ScoreDoc hit) throws IOException {
		System.out.println("User Name: " + lReader.document(hit.doc).get("user_name"));
		System.out.println("User Review Count: " + lReader.document(hit.doc).get("Review_count"));
		System.out.println("User Average Stars: " + lReader.document(hit.doc).get("Average_stars"));
	}

	// present the search results
	public void printResult(ScoreDoc[] hits) {
		if (hits.length == 0) {
			System.out.println("No results");
		} else {
			int i = 1;
			for (ScoreDoc hit : hits) {
				System.out.println("\nResult " + i + "\tDocID: " + hit.doc + "\t Score: " + hit.score);
				try {
					if(lReader.document(hit.doc).get("business_id")!= null && lReader.document(hit.doc).get("user_id")!= null) {
						ScoreDoc business_hit = getDetails(lReader.document(hit.doc).get("business_id"), "business_id");
						businessPrint(business_hit);
						ScoreDoc user_hit = getDetails(lReader.document(hit.doc).get("user_id"), "user_id");
						userPrint(user_hit);
					} else {
						if(lReader.document(hit.doc).get("business_id")!= null ) {
							businessPrint(hit);
						} 
						if (lReader.document(hit.doc).get("user_id")!= null) {
							userPrint(hit);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				i++;

			}
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
