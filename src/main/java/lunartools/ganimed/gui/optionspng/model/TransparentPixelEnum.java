package lunartools.ganimed.gui.optionspng.model;

public enum TransparentPixelEnum {
	PIXEL1("1 pixel",1),
	PIXEL2("2 pixel",2),
	PIXEL3("3 pixel",3),
	PIXEL4("4 pixel",4),
	PIXEL5("5 pixel",5),
	PIXEL6("6 pixel",6),
	PIXEL7("7 pixel",7),
	PIXEL8("8 pixel",8),
	PIXEL9("9 pixel",9),
	PIXEL10("10 pixel",10);

	private String title;
	private int value;
	
	TransparentPixelEnum(String title, int value) {
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
