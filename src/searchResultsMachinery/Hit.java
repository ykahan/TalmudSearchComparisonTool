package searchResultsMachinery;

public class Hit {
	String text;
	String masechta;
	String daf;
	String amud;
	
	public Hit(String masechta, String daf, String amud, String text) {
		this.masechta = masechta;
		this.daf = daf;
		this.amud = amud;
		this.text = text;
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
//		int length = this.masechta.length() + this.daf.length() + this.amud.length();
		StringBuilder sb = new StringBuilder();
		sb.append("Text: " + getText() + "\n");
		sb.append("Location: " + getMasechta() + " " + getDaf() + " " + getAmud() + "\n");
		for(int i = 0; i < 40; i++) {
			sb.append("-");
		}
		return sb.toString();
	}

}
