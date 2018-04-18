package TestIndex;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

public class ClassicSimilarity extends TFIDFSimilarity {
	public ClassicSimilarity() {
	}

	// Weighs shorter fields more heavily
	@Override
	public float lengthNorm(int state) {
		return 1f;
	}

	@Override
	public float tf(float freq) {
		return (float) Math.sqrt(freq);
	}

	@Override
	public float idf(long docFreq, long docCount) {
		// TODO Auto-generated method stub
		return (float) Math.log(docCount / docFreq);
	}

	@Override
	public float scorePayload(int arg0, int arg1, int arg2, BytesRef arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float sloppyFreq(int distance) {
		return 1.0f;
	}

}
