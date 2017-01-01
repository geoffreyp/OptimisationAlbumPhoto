package tools;

public class RGBColor {
	long rouge, vert, bleu;

	public RGBColor() {}

	public RGBColor(long rouge, long vert, long bleu) {
		super();
		this.rouge = rouge;
		this.vert = vert;
		this.bleu = bleu;
	}

	public long getRouge() {
		return rouge;
	}

	public void setRouge(long rouge) {
		this.rouge = rouge;
	}

	public long getVert() {
		return vert;
	}

	public void setVert(long vert) {
		this.vert = vert;
	}

	public long getBleu() {
		return bleu;
	}

	public void setBleu(long bleu) {
		this.bleu = bleu;
	}

}
