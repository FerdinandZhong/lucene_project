package TestIndex;

public class SearchQuery {
	private String field;
	private String contents;
	private int type;
	private Double latitude;
	private Double longitude;
	private Double radius;
	private Double start;
	private Double end;
	
	public SearchQuery(String field, String contents, int type) {
		this.setField(field);
		this.setContents(contents);
		this.type = type;
	}
	
	public SearchQuery(String field, Double latitude, Double longitude, Double radius, int type) {
		this.setField(field);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
		this.setRadius(radius);
		this.type = type;
	}
	
	public SearchQuery(String field, Double start, Double end, int type) {
		this.setField(field);
		this.setStart(start);
		this.setEnd(end);
		this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public Double getStart() {
		return start;
	}

	public void setStart(Double start) {
		this.start = start;
	}

	public Double getEnd() {
		return end;
	}

	public void setEnd(Double end) {
		this.end = end;
	}
}

