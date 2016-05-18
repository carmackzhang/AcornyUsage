package officialMap;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.BitSet;

public class UrlEncoder {

	public static enum CharSet {
		GBK, UTF8, UTF16_LE
	};
	
	public static ByteArrayRef getBytesRef(DirectString str, CharSet charset){
		switch (charset) {
		case GBK:
			StringEncoder gbkEncoder = ThreadLocalBuffer.getGbkEncoder();
			return gbkEncoder.encode(str);
		case UTF8:
			StringEncoder utf8Encoder = ThreadLocalBuffer.getUtf8Encoder();
			return utf8Encoder.encode(str);
		case UTF16_LE:
			CharArrayRef chars = str.getInnerChars();
			char[] charvalue = chars.getChars();
			int offset = chars.getOffset();
			int length = chars.getLength();
			byte[] buffer = ThreadLocalBuffer.getByteBuffer(length * 2);
			for(int i = 0; i < length; i++){
				char ch = charvalue[offset + i];
				buffer[i*2] = (byte)(ch & 0xff);
				buffer[i*2+1] = (byte)((ch >> 8) & 0xff);
			}
			return new ByteArrayRef(buffer, 0, length * 2);
		default:
			return null;	
		}
	}
	public static byte[] getBytes(DirectString str, CharSet charset){
		ByteArrayRef bytes = getBytesRef(str, charset);
		return Arrays.copyOfRange(bytes.getBytes(), bytes.getOffset(), bytes.getLength());
	}

	public static ByteArrayRef getBytesRef(String str, CharSet charset){
		switch (charset) {
		case GBK:
			StringEncoder gbkEncoder = ThreadLocalBuffer.getGbkEncoder();
			return gbkEncoder.encode(str);
		case UTF8:
			StringEncoder utf8Encoder = ThreadLocalBuffer.getUtf8Encoder();
			return utf8Encoder.encode(str);
		case UTF16_LE:
			int length = str.length();
			char[] charvalue = ThreadLocalBuffer.getCharBuffer(length);
			str.getChars(0, length, charvalue, 0);
			byte[] buffer = ThreadLocalBuffer.getByteBuffer(length * 2);
			for(int i = 0; i < length; i++){
				char ch = charvalue[i];
				buffer[i*2] = (byte)(ch & 0xff);
				buffer[i*2+1] = (byte)((ch >> 8) & 0xff);
			}
			return new ByteArrayRef(buffer, 0, length * 2);
		default:
			return null;	
		}
	}
	static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');
	static {
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
		    dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
		    dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
		    dontNeedEncoding.set(i);
		}
		dontNeedEncoding.set(' '); /* encoding a space to a + is done
					    * in the encode() method */
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');
	}
	
	public static String urlEncode(String s, CharSet charset) throws UnsupportedEncodingException {
		if (s == null)
			return null;
		boolean needToChange = false;
	    StringBuilder out = new StringBuilder(s.length());
		CharArrayWriter charArrayWriter = new CharArrayWriter();

		if (charset == null)
		    throw new NullPointerException("charsetName");


		for (int i = 0; i < s.length();) {
		    int c = (int) s.charAt(i);
		    //System.out.println("Examining character: " + c);
		    if (dontNeedEncoding.get(c)) {
			if (c == ' ') {
			    c = '+';
			    needToChange = true;
			}
			out.append((char)c);
			i++;
		    } else {
			do {
			    charArrayWriter.write(c);
			    if (c >= 0xD800 && c <= 0xDBFF) {
					if ( (i+1) < s.length()) {
					    int d = (int) s.charAt(i+1);
					    if (d >= 0xDC00 && d <= 0xDFFF) {
					        charArrayWriter.write(d);
					        i++;
					    }
					}
			    } 
			    i++;
			} while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

			charArrayWriter.flush();
			char[] charBuffer = charArrayWriter.toCharArray();
			DirectString str = new DirectString(charBuffer, 0, charBuffer.length);
			ByteArrayRef bar = getBytesRef(str, charset);
			byte[] ba = bar.getBytes();
			int len = bar.getLength();
			for (int j = 0; j < len; j++) {
			    out.append('%');
			    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
			    // converting to use uppercase letter as part of
			    // the hex value if ch is a letter.
			    if (Character.isLetter(ch)) {
				ch -= caseDiff;
			    }
			    out.append(ch);
			    ch = Character.forDigit(ba[j] & 0xF, 16);
			    if (Character.isLetter(ch)) {
				ch -= caseDiff;
			    }
			    out.append(ch);
			}
			charArrayWriter.reset();
			needToChange = true;
		    }
		}
		return (needToChange? out.toString() : s);
	}
	
	public static void main(String[] args) {
		String keyword = "";
		try {
			String res = urlEncode(keyword,CharSet.UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
