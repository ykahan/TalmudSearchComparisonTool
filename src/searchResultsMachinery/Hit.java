package searchResultsMachinery;

public class Hit {
	String text;
	String masechta;
	String daf;
	String amud;
	
	public Hit(String text, String masechta, String daf, String amud) {
		this.text = text;
		this.masechta = masechta;
		this.daf = daf;
		this.amud = amud;
	}
	
	public String getText() {
		return this.text;
	}
	
	public String getMasechta() {
		return this.masechta;
	}
	
	public String getDaf() {
		return this.daf;
	}
	
	public String getAmud() {
		return this.amud;
	}
	
	@Override
	public String toString() {
		int length = this.text.length();
		StringBuilder sb = new StringBuilder();
		sb.append("Text: " + getText() + "\n");
		sb.append("Location: " + getMasechta() + " " + getDaf() + " " + getAmud() + "\n");
		for(int i = 0; i < (length * 1.5); i++) {
			sb.append("-");
		}
		return sb.toString();
	}

}
