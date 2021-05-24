package lunartools.ganimed.gui.optionspng.model;

public enum TruecolourBitsCount {
	BIT24("24 bit",8),
	BIT21("21 bit",7),
	BIT18("18 bit",6),
	BIT15("15 bit",5),
	BIT12("12 bit",4),
	BIT9("9 bit",3),
	BIT6("6 bit",2),
	BIT3("3 bit",1);

	private String title;
	private int value;

	TruecolourBitsCount(String title, int value) {
		this.title=title;
		this.value=value;
	}

	public String getTitle() {
		return title;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return title;
	}
}
