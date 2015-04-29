package com.caucho.hessian.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 一个包装流，仅用来累计数据的接受量
 * @author guxuede
 */
public class GxdInputStream extends InputStream{
	
	private int AccumulativeLength;//Hessian2Input this time Accumulative Length
	private static int AccumulativeTotalLength;//Hessian2Input already read total length;
	
	private InputStream in;
	
	public GxdInputStream(InputStream in) {
		this.in=in;
	}
	
	@Override
	public int read() throws IOException {
		AccumulativeLength++;
		return in.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		AccumulativeLength=AccumulativeLength+b.length;
		return in.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		AccumulativeLength=AccumulativeLength+len;
		return in.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		in.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		in.reset();
	}

	@Override
	public boolean markSupported() {
		return in.markSupported();
	}
	
	public int getAccumulativeLength() {
		return AccumulativeLength;
	}

	public static int getAccumulatTotalLength() {
		return AccumulativeTotalLength;
	}

	public static void accumulativeTotalLength(int length) {
		AccumulativeTotalLength = AccumulativeTotalLength+length;
	}
}
