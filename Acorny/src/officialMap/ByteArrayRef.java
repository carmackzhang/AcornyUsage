package officialMap;

public class ByteArrayRef {
	
	byte[] data;
	public final byte[] getBytes() {
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
	
	public ByteArrayRef(byte[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}
}
