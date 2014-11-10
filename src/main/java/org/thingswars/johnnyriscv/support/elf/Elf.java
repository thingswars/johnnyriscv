package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.thingswars.johnnyriscv.support.Endianness;

public class Elf {

	private final ElfIdentification elfIdentification;
	private final ElfObjectType objectType;
	private final int machine;
	private final int version;
	private final long entryAddress;
	private final long programHeaderOffset;
	private final long sectionHeaderOffset;
	private final int flags;
	private final int elfHeaderSize;
	private final int programHeaderEntrySize;
	private final int programHeaderEntryCount;
	private final int sectionHeaderEntrySize;
	private final int sectionHeaderEntryCount;
	private final int stringTableIndex;

	private final ElfProgramHeader[] programHeaders;
	
	public Elf(InputStream inputStream) throws IOException {
		elfIdentification = new ElfIdentification(inputStream);
		ElfByteSource elfByteSource = new ElfByteSource(inputStream, elfIdentification);

		objectType = ElfObjectType.fromFileValue(elfByteSource.readHalfWord());
		machine = elfByteSource.readHalfWord();
		version = elfByteSource.readWord();
		entryAddress = elfByteSource.readAddress();
		programHeaderOffset = elfByteSource.readOffset();
		sectionHeaderOffset = elfByteSource.readOffset();
		flags = elfByteSource.readWord();
		elfHeaderSize = elfByteSource.readHalfWord();
		programHeaderEntrySize = elfByteSource.readHalfWord();
		programHeaderEntryCount = elfByteSource.readHalfWord();
		sectionHeaderEntrySize = elfByteSource.readHalfWord();
		sectionHeaderEntryCount = elfByteSource.readHalfWord();
		stringTableIndex = elfByteSource.readHalfWord();

		elfByteSource.skipToOffset(programHeaderOffset);

		programHeaders = new ElfProgramHeader[programHeaderEntryCount];
		for (int i = 0; i < programHeaderEntryCount; i++) {
			programHeaders[i] = new ElfProgramHeader(elfByteSource);
		}
	}

	public ElfIdentification getElfIdentification() {
		return elfIdentification;
	}

	public ElfProgramHeader[] getProgramHeaders() {
		return programHeaders;
	}

	public ElfObjectType getObjectType() {
		return objectType;
	}

	public int getMachine() {
		return machine;
	}

	public int getVersion() {
		return version;
	}

	public long getEntryAddress() {
		return entryAddress;
	}

	public int getFlags() {
		return flags;
	}

	public int getStringTableIndex() {
		return stringTableIndex;
	}

	@Override
	public String toString() {
		return "Elf [elfIdentification=" + elfIdentification + ", objectType="
				+ objectType + ", machine=" + machine + ", version=" + version
				+ ", entryAddress=" + Long.toHexString(entryAddress) + ", sectionHeaderOffset="
				+ sectionHeaderOffset + ", flags=" + flags + ", elfHeaderSize="
				+ elfHeaderSize + ", "
				+ Arrays.toString(programHeaders)
				+ sectionHeaderEntrySize + ", sectionHeaderEntryCount="
				+ sectionHeaderEntryCount + ", stringTableIndex="
				+ stringTableIndex + "]";
	}
}
