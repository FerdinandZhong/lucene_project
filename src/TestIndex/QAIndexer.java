/**
 *
 * This code is provided solely as sample code for using Lucene.
 * 
 */

package TestIndex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LatLonPoint;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.JSONArray;
import org.json.JSONObject;

public class QAIndexer {

	private IndexWriter writer = null;
	
	//for recording time used for indexing 
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	public QAIndexer(String dir) throws IOException {
		//specify the directory to store the Lucene index
		Directory indexDir = FSDirectory.open(Paths.get(dir));
		
		//specify the analyzer used in indexing
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
		cfg.setOpenMode(OpenMode.CREATE);
		
		//create the IndexWriter
		writer = new IndexWriter(indexDir, cfg);
	}

	//specify what is a document, and how its fields are indexed
	public void indexAllfiles(String[] fileNames)throws Exception {
		for(int i=0; i< fileNames.length; i++) {
			switch (fileNames[i]) {
				case "./dataset/business.json": this.indexBusiness(fileNames[i]);
				break;
				case "./dataset/user.json": this.indexUser(fileNames[i]);
				break;
				case "./dataset/review.json": this.indexReview(fileNames[i]);
				break;
				case "./dataset/tip.json": this.indexTip(fileNames[i]);
				break;
			}	
		}
		//close the index writer.
		writer.close();

	}

	protected Document getBusinessDocument(String business_id, String name, String neighborhood, String address, String city, String state, String postalcode, Double latitude, Double longitude,
			Double stars, Integer review_count, Integer is_open, JSONObject attributes, JSONArray categories) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("business_id", business_id, Field.Store.YES));
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("neighborhood", neighborhood, Field.Store.YES));
		doc.add(new TextField("address", address, Field.Store.YES));
		doc.add(new TextField("city", city, Field.Store.YES));
		doc.add(new TextField("state", state, Field.Store.YES));
		doc.add(new TextField("postalcode", postalcode, Field.Store.YES));
		doc.add(new LatLonPoint("location", latitude,longitude));
		doc.add(new TextField("latitude", latitude.toString(), Field.Store.YES));
		doc.add(new TextField("longitude", longitude.toString(), Field.Store.YES));
		// this one can be kept for range query
		doc.add(new DoublePoint("stars", stars));
		doc.add(new TextField("Stars", stars.toString(), Field.Store.YES));
		doc.add(new DoublePoint("review_count", review_count));
		doc.add(new TextField("is_open", is_open == 1 ? "open": "closed", Field.Store.YES));
		doc.add(new TextField("city", city, Field.Store.YES));
		for(int i=0;i<categories.length();i++) {
		    doc.add(new TextField("categories", categories.get(i).toString() , Field.Store.YES)); // doc is a Document 
		}

		return doc;
	}
	
	public void indexBusiness(String fileName) throws Exception {
		
		System.out.println("Start indexing "+fileName+" "+sdf.format(new Date()));
		
		//read a JSON file
		Scanner in = new Scanner(new File(fileName));
		int lineNumber = 1;
		String jLine = "";
		while (in.hasNextLine()) {
			try {
				jLine = in.nextLine().trim();
				//parse the JSON file and extract the values for "question" and "answer"
				JSONObject jObj = new JSONObject(jLine);
				String business_id = jObj.getString("business_id");
				String name = jObj.getString("name");
				String city = jObj.getString("city");
				String neighborhood = jObj.getString("neighborhood");
				String address = jObj.getString("address");
				String state = jObj.getString("state");
				String postalcode = jObj.getString("postal_code");
				Double latitude = 0.0;
				if (!jObj.isNull("latitude"))
				{
					latitude = jObj.getDouble("latitude");
				}
				Double longitude = 0.0;
				if (!jObj.isNull("longitude"))
				{
					longitude = jObj.getDouble("longitude");
				}
				Double stars = jObj.getDouble("stars");
				Integer review_count = jObj.getInt("review_count");
				Integer is_open = jObj.getInt("is_open");
				JSONObject attributes = jObj.getJSONObject("attributes");
				JSONArray categories = jObj.getJSONArray("categories");

				//create a document for each JSON record 
				Document doc = getBusinessDocument(business_id, name, neighborhood, address, city, state, postalcode, latitude, longitude,
						stars, review_count, is_open, attributes, categories);
				
				//index the document
				writer.addDocument(doc);
				
				lineNumber++;
			} catch (Exception e) {
				System.out.println("Error at: " + lineNumber + "\t" + jLine);
				e.printStackTrace();
			}
		}
		//close the file reader
		in.close();
		System.out.println("Index completed at " + sdf.format(new Date()));
		System.out.println("Total number of documents indexed: " + writer.maxDoc());			
	}
	
public void indexUser(String fileName) throws Exception {
		
		System.out.println("Start indexing "+fileName+" "+sdf.format(new Date()));
		
		//read a JSON file
		Scanner in = new Scanner(new File(fileName));
		int lineNumber = 1;
		String jLine = "";
		while (in.hasNextLine()) {
			try {
				jLine = in.nextLine().trim();
				//parse the JSON file and extract the values for "question" and "answer"
				JSONObject jObj = new JSONObject(jLine);
				String user_id = jObj.getString("user_id");
				String name = jObj.getString("name");
				Integer review_count = jObj.getInt("review_count");
				String yelping_since = jObj.getString("yelping_since");
				JSONArray friends = jObj.getJSONArray("friends");
				Integer useful = jObj.getInt("useful");
				Integer funny = jObj.getInt("funny");
				Integer cool = jObj.getInt("cool");
				Integer fans = jObj.getInt("fans");
				JSONArray elite = jObj.getJSONArray("elite");
				Double average_stars = jObj.getDouble("average_stars");

				//create a document for each JSON record 
				Document doc = getUserDocument(user_id, name, review_count, yelping_since, friends, useful, funny, cool, fans,
						elite, average_stars);
				
				//index the document
				writer.addDocument(doc);
				
				lineNumber++;
			} catch (Exception e) {
				System.out.println("Error at: " + lineNumber + "\t" + jLine);
				e.printStackTrace();
			}
		}
		//close the file reader
		in.close();
		System.out.println("Index completed at " + sdf.format(new Date()));
		System.out.println("Total number of documents indexed: " + writer.maxDoc());
			
	}


	protected Document getUserDocument(String user_id, String name, Integer review_count, String yelping_since,JSONArray friends, Integer useful, Integer funny, Integer cool, Integer fans,
			JSONArray elite, Double average_stars) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("user_id", user_id, Field.Store.YES));
		doc.add(new TextField("user_name", name, Field.Store.YES));
		doc.add(new DoublePoint("review_count", review_count));
		doc.add(new TextField("Review_count", review_count.toString(), Field.Store.YES));
		doc.add(new TextField("yelping_since", yelping_since, Field.Store.YES));
		doc.add(new TextField("friends", friends.toString(), Field.Store.YES));
		doc.add(new TextField("useful", useful.toString(), Field.Store.YES));
		doc.add(new TextField("funny", funny.toString(), Field.Store.YES));
		doc.add(new TextField("cool", cool.toString(), Field.Store.YES));
		doc.add(new DoublePoint("fans", fans));
		doc.add(new TextField("elite", elite.toString(), Field.Store.YES));
		doc.add(new DoublePoint("average_stars", average_stars));
		doc.add(new TextField("Average_stars", average_stars.toString(), Field.Store.YES));
		return doc;
	}
	
public void indexReview(String fileName) throws Exception {
		
		System.out.println("Start indexing "+fileName+" "+sdf.format(new Date()));
		
		//read a JSON file
		Scanner in = new Scanner(new File(fileName));
		int lineNumber = 1;
		String jLine = "";
		while (in.hasNextLine()) {
			try {
				jLine = in.nextLine().trim();
				JSONObject jObj = new JSONObject(jLine);
				String review_id = jObj.getString("review_id");
				String user_id = jObj.getString("user_id");
				String business_id = jObj.getString("business_id");
				Integer stars = jObj.getInt("stars");
				String date = jObj.getString("date");
				String text = jObj.getString("text");
				Integer useful = jObj.getInt("useful");
				Integer funny = jObj.getInt("funny");
				Integer cool = jObj.getInt("cool");
							
				//create a document for each JSON record 
				Document doc = getReviewDocument(review_id, user_id, business_id, stars, date, text, useful, funny, cool);
				
				//index the document
				writer.addDocument(doc);
				
				lineNumber++;
			} catch (Exception e) {
				System.out.println("Error at: " + lineNumber + "\t" + jLine);
				e.printStackTrace();
			}
		}
		//close the file reader
		in.close();
		System.out.println("Index completed at " + sdf.format(new Date()));
		System.out.println("Total number of documents indexed: " + writer.maxDoc());
			
	}


	protected Document getReviewDocument(String review_id, String user_id, String business_id, Integer stars,String date, String text, Integer useful, Integer funny, Integer cool) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("review_id", review_id, Field.Store.YES));
		doc.add(new TextField("user_id", user_id, Field.Store.YES));
		doc.add(new TextField("business_id", business_id, Field.Store.YES));
		doc.add(new DoublePoint("stars", stars));
		doc.add(new TextField("date", date, Field.Store.YES));
		doc.add(new TextField("text", text, Field.Store.YES));
		doc.add(new TextField("useful", useful.toString(), Field.Store.YES));
		doc.add(new TextField("funny", funny.toString(), Field.Store.YES));
		doc.add(new TextField("cool", cool.toString(), Field.Store.YES));
		
		return doc;
	}
	
public void indexTip(String fileName) throws Exception {
		
		System.out.println("Start indexing "+fileName+" "+sdf.format(new Date()));
		
		//read a JSON file
		Scanner in = new Scanner(new File(fileName));
		int lineNumber = 1;
		String jLine = "";
		while (in.hasNextLine()) {
			try {
				jLine = in.nextLine().trim();
				JSONObject jObj = new JSONObject(jLine);
				String text = jObj.getString("text");
				String date = jObj.getString("date");
				Integer likes = jObj.getInt("likes");
				String business_id = jObj.getString("business_id");
				String user_id = jObj.getString("user_id");
							
				Document doc = getTipDocument(text, date, likes, business_id, user_id);
				
				//index the document
				writer.addDocument(doc);
				
				lineNumber++;
			} catch (Exception e) {
				System.out.println("Error at: " + lineNumber + "\t" + jLine);
				e.printStackTrace();
			}
		}
		//close the file reader
		in.close();
		System.out.println("Index completed at " + sdf.format(new Date()));
		System.out.println("Total number of documents indexed: " + writer.maxDoc());
			
	}


	protected Document getTipDocument(String text, String date, Integer likes, String business_id, String user_id) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("text", text, Field.Store.YES));
		doc.add(new TextField("date", date, Field.Store.YES));
		doc.add(new TextField("likes", likes.toString(), Field.Store.YES));
		doc.add(new DoublePoint("likes", likes.doubleValue()));
		doc.add(new TextField("business_id", business_id, Field.Store.YES));
		doc.add(new TextField("user_id", user_id, Field.Store.YES));
		
		return doc;
	}
	
}
