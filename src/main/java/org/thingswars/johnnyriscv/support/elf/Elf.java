package org.thingswars.johnnyriscv.support.elf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Random;

import org.thingswars.johnnyriscv.support.Endianness;

public class Elf {

	private final ElfHeader elfHeader;

	public Elf(ByteBuffer byteBuffer) throws ElfFormatException {
		if (byteBuffer.capacity() == 0) {
			throw new ElfFormatException("Empty file");
		}
		byteBuffer.mark();
		elfHeader = new ElfHeader(byteBuffer);
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

	@Override
	public String toString() {
		return "Elf" + elfHeader;
	}
}
