package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ByteSource {

	/**
	 * 
	 * @param writeable whether to support copy-on-write, rather than read-only
	 * @param start 
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	ByteBuffer map(boolean writeable, long offset, long size) throws IOException;

	long position() throws IOException;
	
	void seek(long newPosition) throws IOException;
	
	int read(byte[] bytes) throws IOException;
	
	long size() throws IOException;
}
