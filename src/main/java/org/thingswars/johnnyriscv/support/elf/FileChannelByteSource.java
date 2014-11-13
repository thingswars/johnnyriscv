package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelByteSource implements ByteSource {

	private final FileChannel fileChannel;
	
	public FileChannelByteSource(FileChannel fileChannel) {
		super();
		this.fileChannel = fileChannel;
	}

	@Override
	public ByteBuffer map(boolean writeable, long offset, long size) throws IOException {
		if (writeable) {
			return fileChannel.map(FileChannel.MapMode.PRIVATE, offset, size);
		}
		else {
			return fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, size);
		}
	}

	@Override
	public long position() throws IOException {
		return fileChannel.position();
	}
	
	@Override
	public int read(byte[] bytes) throws IOException {
		return fileChannel.read(ByteBuffer.wrap(bytes));
	}
	
	@Override
	public void seek(long newPosition) throws IOException {
		fileChannel.position(newPosition);
	}

	@Override
	public long size() throws IOException {
		return fileChannel.size();
	}

}
