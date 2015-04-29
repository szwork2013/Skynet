package com.caucho.hessian.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 一个包装流，仅用来累计数据的发送量
 * @author guxuede
 */
public class GxdOutputStream extends OutputStream{
	
	private int AccumulativeLength;//Hessian2Input this time Accumulative writed Length
	
	private static int AccumulativeTotalLength;//Hessian2Input already writed total length;
	
	private OutputStream out;
	
	public GxdOutputStream(OutputStream out) {
		this.out=out;
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
		AccumulativeLength++;
	}

	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
		AccumulativeLength=AccumulativeLength+b.length;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		AccumulativeLength=AccumulativeLength+len;
	}

	@Override
	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void close() throws IOException {
		out.close();
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
