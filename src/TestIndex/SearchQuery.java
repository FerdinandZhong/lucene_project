package TestIndex;

public class SearchQuery {
	private String field;
	private String contents;
	
	public SearchQuery(String field, String contents) {
		this.setField(field);
		this.setContents(contents);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	
}
