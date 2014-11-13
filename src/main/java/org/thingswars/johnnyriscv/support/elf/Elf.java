package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Elf {

	private final ElfHeader elfHeader;
	
	private final List<ElfSegment> segments;

	public Elf(FileChannel fileChannel) throws IOException {
		this(new FileChannelByteSource(fileChannel));
	}
	
	public Elf(ByteSource byteSource) throws IOException {
		if (byteSource.size() == 0) {
			throw new ElfFormatException("Empty file");
		}
		elfHeader = new ElfHeader(byteSource);
		segments = new ArrayList<>();
		for (ElfProgramHeader programHeader : elfHeader.getProgramHeaders()) {
			if (programHeader.loadableSegment()) {
				segments.add(new ElfSegment(programHeader, byteSource));
			}
		}
	}

	public ElfHeader getHeader() {
		return elfHeader;
	}

	public ElfIdentification getElfIdentification() {
		return elfHeader.getElfIdentification();
	}

	public ElfProgramHeader[] getProgramHeaders() {
		return elfHeader.getProgramHeaders();
	}
	
	public List<ElfSegment> getSegments() {
		return segments;
	}

	@Override
	public String toString() {
		return "Elf" + elfHeader;
	}
}
