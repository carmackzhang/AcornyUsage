package officialMap;

//import org.apache.mina.common.ByteBuffer;

public class ThreadLocalBuffer {

	static final int INIT_BUFFER_LENGTH = 65536;
	static final int MAX_BUFFER_LENGTH = 1048576;
	
	static public final int THREAD_MAIN = 0;
	static public final int THREAD_LOAD = 0;
	static public final int THREAD_SEND = 0;
	static public final int THREAD_COLLECT = 0;
	static public final int THREAD_MAKEPAGE = 0;
	static public final int THREAD_REPLY = 0;
	static public final int THREAD_WRITEBACK = 0;
	static public final int THREAD_STATISTICS = 0;
	static public final int THREAD_ACCEPTOR = 0;
	static public final int THREAD_CONNECTOR = 0;
	static public final int THREAD_CONNECTOR_MONITOR = 0;
	static public final int THREAD_CONNECTOR_SEND = 0;
	static public final int THREAD_CONNECTOR_STUB = 0;
	

	// String Builder
	static ThreadLocal<StringBuilder> stringbuilder =
		new ThreadLocal<StringBuilder>() {
	        protected StringBuilder initialValue() {
	            return new StringBuilder(INIT_BUFFER_LENGTH);
        }
	};
	static ThreadLocal<StringBuilder> stringbuilder2 = 
		new ThreadLocal<StringBuilder>() {
        	protected StringBuilder initialValue() {
        		return new StringBuilder(INIT_BUFFER_LENGTH);
	    }
	};
	public static StringBuilder getStringBuilder(int thread) {
		StringBuilder builder = stringbuilder.get();
		builder.delete(0, builder.length());
		return builder;
	}
	public static StringBuilder getStringBuilder2(int thread) {
		StringBuilder builder = stringbuilder2.get();
		builder.delete(0, builder.length());
		return builder;
	}

	// Encoder
	static ThreadLocal<StringEncoder> gbkencoder =
		new ThreadLocal<StringEncoder>() {
	        protected StringEncoder initialValue() {
	            return new StringEncoder("GBK");
        }
	};
	public static StringEncoder getGbkEncoder() {
		return gbkencoder.get();
	}

	static ThreadLocal<StringEncoder> utf8encoder =
		new ThreadLocal<StringEncoder>() {
	        protected StringEncoder initialValue() {
	            return new StringEncoder("UTF-8");
        }
	};
	public static StringEncoder getUtf8Encoder() {
		return utf8encoder.get();
	}

	// Char Buffer
	static ThreadLocal<char[]> charbuffer =
		new ThreadLocal<char[]>() {
	        protected char[] initialValue() {
	            return new char[INIT_BUFFER_LENGTH];
        }
	};
	public static char[] getCharBuffer(int maxLength) {
		if (maxLength > MAX_BUFFER_LENGTH) {
			return new char[maxLength];
		}
		char[] buffer = charbuffer.get();
		if (buffer.length < maxLength) {
			int length = buffer.length;
			while (length < maxLength && length < MAX_BUFFER_LENGTH)
				length *= 2;
			buffer = new char[length];
			charbuffer.set(buffer);
		}
		return buffer;
	}

	// Byte Buffer
	static ThreadLocal<byte[]> bytebuffer =
		new ThreadLocal<byte[]>() {
	        protected byte[] initialValue() {
	            return new byte[INIT_BUFFER_LENGTH];
        }
	};
	public static byte[] getByteBuffer(int maxLength) {
		if (maxLength > MAX_BUFFER_LENGTH) {
			return new byte[maxLength];
		}
		byte[] buffer = bytebuffer.get();
		if (buffer.length < maxLength) {
			int length = buffer.length;
			while (length < maxLength && length < MAX_BUFFER_LENGTH)
				length *= 2;
			buffer = new byte[length];
			bytebuffer.set(buffer);
		}
		return buffer;
	}
	
/*	// Mina already pool some ByteBuffers
	// see ByteBuffer.getAllocator() & ByteBuffer.isUseDirectBuffers()
	static ThreadLocal<ByteBuffer> bytebufferbuffer = 
			new ThreadLocal<ByteBuffer>() {
		        protected ByteBuffer initialValue() {
		            return ByteBuffer.allocate(INIT_BUFFER_LENGTH);
	        }
		};
	public static ByteBuffer getByteBufferBuffer(int maxLength) {
		if(maxLength > MAX_BUFFER_LENGTH)
			return ByteBuffer.allocate(maxLength);
		ByteBuffer buffer = bytebufferbuffer.get();
		if (buffer.capacity() < maxLength) {
			int length = buffer.capacity();
			while (length < maxLength && length < MAX_BUFFER_LENGTH)
				length *= 2;
			buffer = ByteBuffer.allocate(length);
			bytebufferbuffer.set(buffer);
		}
		buffer.acquire();
		buffer.clear();
		return buffer;
	}*/
}
