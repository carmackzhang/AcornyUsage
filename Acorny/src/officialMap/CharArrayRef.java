package officialMap;

public class CharArrayRef {
	
	char[] data;
	public final char[] getChars() {
		return data;
	}
	
	int offset;
	public int getOffset() {
		return offset;
	}
	
	int length;
	public int getLength() {
		return length;
	}
	
	public CharArrayRef(char[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}
}

