package lunartools.ganimed.gui.optionscolourreduction.model;

public enum DitheringAlgorithm {
	NO_DITHERING("No dithering"),
	//SIMPLE_DITHERING1("Simple dithering (experimental)"),
	FLOYD_STEINBERG("Floyd-Steinberg"),
	JARVIS_JUDICE_NINKE("Jarvis-Judice-Ninke"),
	STUCKI("Stucki"),
	ATKINSON("Atkinson"),
	BURKES("Burkes"),
	SIERRA("Sierra"),
	TWO_ROW_SIERRA("Two-row-Sierra"),
	SIERRA_LITE("Sierra lite");

	private String name;

	DitheringAlgorithm(String name) {
		this.name=name;
	}

	public String toString() {
		return name;
	}
}
