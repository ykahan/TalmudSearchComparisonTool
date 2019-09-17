package searchResultsMachinery;

public class Hit {
	private String text;
	private String targetText;
	private String masechta;
	private String daf;
	private String amud;
	private boolean found;

	public Hit(String masechta, String daf, String amud, String text, String targetText) {
		this.text = text;
		this.targetText = targetText;
		this.daf = daf;
		this.amud = amud;
		this.found = false;
		incrementPosition();
		this.masechta = masechta;
		if(this.masechta.contentEquals("נידה")) this.masechta = "נדה";
	}

	public Hit() {
		this.found = false;
	}

	private void incrementPosition() {
		int locTargetText = getLocTargetText();
		boolean pageChangeExists = doesPageChangeExist();
		if (pageChangeExists) {
			int locPageChange = getLocPageChange();
			if(locPageChange < locTargetText) changePosition(locPageChange);
		}
	}

	private void changePosition(int locPageChange) {
		String[] foundWords = this.text.split(" ");
		this.daf = foundWords[locPageChange + 1];
		this.amud = foundWords[locPageChange + 3];
		this.amud = this.amud.substring(0, this.amud.length() - 1);
	}

	private boolean doesPageChangeExist() {
		String[] foundWords = this.text.split(" ");
		int lengthFoundWords = foundWords.length;

		for (int word = 0; word < lengthFoundWords; word++) {
			if (foundWords[word].contains("דף"))
				return true;
		}
		return false;
	}

	private int getLocPageChange() {
		return getLocText(this.text, "דף");
	}

	private int getLocTargetText() {
		return getLocText(this.text, this.targetText);
	}

	private int getLocText(String bigText, String littleText) {
		String[] littleTextWords = littleText.split(" ");
		String[] bigTextWords = bigText.split(" ");

		int locLittleText = 0;
		int lengthLittleText = littleTextWords.length;
		int lengthBigText = bigTextWords.length;
		int lastCheckableWord = lengthBigText - lengthLittleText;

		for (int word = 0; word <= lastCheckableWord; word++) {
			if (bigTextWords[word].contains(littleTextWords[0])) {
				for (int targetWord = 0; targetWord < lengthLittleText; targetWord++) {
					if (!bigTextWords[word + targetWord].contains(littleTextWords[targetWord])) {
						break;
					}
				}
				locLittleText = word;
			}
		}
		return locLittleText;
	}

	public String getText() {
		return this.text;
	}
	
	public void setText(String text) {
		this.targetText = text;
	}
	
	public String getTargetText() {
		return this.targetText;
	}
	
	public void setTargetText(String text) {
		this.targetText = text;
	}

	public String getMasechta() {
		return this.masechta;
	}
	
	public void setMasechta(String masechta) {
		this.masechta = masechta;
	}

	public String getDaf() {
		return this.daf;
	}
	
	public void setDaf(String daf) {
		this.daf = daf;
	}

	public String getAmud() {
		return this.amud;
	}
	
	public void setAmud(String amud) {
		this.amud = amud;
	}

	public boolean getFound() {
		return this.found;
	}

	public void setFound(boolean found) {
		if (found == true)
			this.found = found;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Text: " + getText() + "\n");
		sb.append("Location: " + getMasechta() + " " + getDaf() + " עמוד " + getAmud() + "\n");
		sb.append("Found in other source: " + getFound() + "\n");
		for (int i = 0; i < 40; i++) {
			sb.append("-");
		}
		return sb.toString();
	}

}
