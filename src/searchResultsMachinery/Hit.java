package searchResultsMachinery;

public class Hit {
	private String text;
	private String masechta;
	private String daf;
	private String amud;
	private boolean found;
	
	public Hit(String masechta, String daf, String amud, String text) {
		this.masechta = masechta;
		this.daf = daf;
		this.amud = amud;
		this.text = text;
		this.found = false;
	}
	
	public String getText() {
		return this.text;
	}
	
//	public void setText(String text;) {
//		this.text = text;
//	}
	
	public String getMasechta() {
		return this.masechta;
	}
	
//	public void setMasechta(String masechta) {
//		this.masechta = masechta;
//	}
	
	public String getDaf() {
		return this.daf;
	}
	
//	public void setDaf(String daf) {
//		this.daf = daf;
//	}
	
	public String getAmud() {
		return this.amud;
	}
	
//	public void setAmud(String amud) {
//		this.amud = amud;
//	}
	
	public boolean getFound() {
		return this.found;
	}
	
	public void setFound(boolean found) {
		if(found == true) this.found = found;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Text: " + getText() + "\n");
		sb.append("Location: " + getMasechta() + " " + getDaf() + " " + getAmud() + "\n");
		sb.append("Found in other source: " + getFound() + "\n");
		for(int i = 0; i < 40; i++) {
			sb.append("-");
		}
		return sb.toString();
	}

}
