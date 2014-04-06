package se.fidde.detective.suffixes;

public enum Suffix {

	GIF("gif"), JPEG("jpeg"), JPG("jpg"), PNG("png"), FLV("flv"), MP4("mp4");

	private final String NAME;

	private Suffix(String name) {
		this.NAME = name;
	}

	@Override
	public String toString() {
		return NAME;
	}
}
