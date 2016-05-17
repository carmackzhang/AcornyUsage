package officialMap;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class StringEncoder {
	
	private Charset charset;

	private CharsetEncoder encoder;

	private final String charsetName;
	public String charsetName() {
		return charsetName;
	}

	public StringEncoder(String charsetName) {
	    this.charsetName = charsetName;
	    this.charset = Charset.forName(charsetName);
	    this.encoder = charset.newEncoder()
			.onMalformedInput(CodingErrorAction.REPLACE)
			.onUnmappableCharacter(CodingErrorAction.REPLACE);
	}

    private static int scale(int len, float expansionFactor) {
    	// We need to perform double, not float, arithmetic; otherwise
    	// we lose low order bits when len is larger than 2**24.
    	return (int)(len * (double)expansionFactor);
    }
	public ByteArrayRef encode(char[] data, int offset, int length) {
		int maxLength = scale(length, encoder.maxBytesPerChar());
		byte[] buffer = ThreadLocalBuffer.getByteBuffer(maxLength);
	    if (maxLength == 0)
	    	return new ByteArrayRef(buffer, 0, maxLength);
	    encoder.reset();
	    ByteBuffer bb = ByteBuffer.wrap(buffer);
	    CharBuffer cb = CharBuffer.wrap(data, offset, length);
	    try {
			CoderResult cr = encoder.encode(cb, bb, true);
			if (!cr.isUnderflow())
			    cr.throwException();
			cr = encoder.flush(bb);
			if (!cr.isUnderflow())
			    cr.throwException();
	    } catch (CharacterCodingException x) {
			// Substitution is always enabled,
			// so this shouldn't happen
			throw new Error(x);
	    }
	    return new ByteArrayRef(buffer, 0, bb.position());
	}
	public ByteArrayRef encode(String str) {
		return encode(str.toCharArray(), 0, str.length());
	}
	public ByteArrayRef encode(DirectString str) {
		CharArrayRef chars = str.getInnerChars();
		return encode(chars.getChars(), chars.getOffset(), chars.getLength());
	}
}