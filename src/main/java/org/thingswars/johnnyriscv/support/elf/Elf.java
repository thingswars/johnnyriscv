package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.io.InputStream;

import org.thingswars.johnnyriscv.support.Endianness;

public class Elf {
	
	private int filePosition;
	
	private final ElfIdentification elfIdentification;
	private ElfObjectType objectType;
	private int machine;
	private int version;
	private long entryAddress;
	private long programHeaderOffset;
	private long sectionHeaderOffset;
	private int flags;
	private int elfHeaderSize;
	private int programHeaderEntrySize;
	private int programHeaderEntryCount;
	private int sectionHeaderEntrySize;
	private int sectionHeaderEntryCount;
	private int stringTableIndex;
	
	public Elf(InputStream inputStream) throws IOException {
		elfIdentification = new ElfIdentification(inputStream);
		filePosition = ElfIdentification.ELF_IDENT_LENGTH;

		objectType = ElfObjectType.fromFileValue(readHalfWord(inputStream));
		machine = readHalfWord(inputStream);
		version = readWord(inputStream);
		entryAddress = readAddress(inputStream);
		programHeaderOffset = readOffset(inputStream);
		sectionHeaderOffset = readOffset(inputStream);
		flags = readWord(inputStream);
		elfHeaderSize = readHalfWord(inputStream);
		programHeaderEntrySize = readHalfWord(inputStream);
		programHeaderEntryCount = readHalfWord(inputStream);
		sectionHeaderEntrySize = readHalfWord(inputStream);
		sectionHeaderEntryCount = readHalfWord(inputStream);
		stringTableIndex = readHalfWord(inputStream);
		
		if (programHeaderEntryCount > 0) {
			filePosition += inputStream.skip(programHeaderOffset - filePosition);
			System.out.println("Current position:" + filePosition);
			
		}
		
	}

	public ElfIdentification getElfIdentification() {
		return elfIdentification;
	}

	private long readOffset(InputStream inputStream) throws IOException {
		return readAddress(inputStream);
	}
	
	private long readAddress(InputStream inputStream) throws IOException {
		if (elfIdentification.getFormat() == ElfFormat.ELF64) {
			return readXWord(inputStream);
		}
		return readWord(inputStream);
	}
	
	private int readHalfWord(InputStream inputStream) throws IOException {
		final byte[] half = new byte[2];
		final int readBytes = inputStream.read(half);
		if (readBytes != half.length) {
			throw new ElfFormatException("Unexpected end of file in half-word");
		}
		filePosition += readBytes;
		if (elfIdentification.getEndianness() == Endianness.LITTLE) {
			return (half[0] & 0xFF) + ((half[1] & 0xFF) << 8);
		}
		else {
			return (half[1] & 0xFF) + ((half[0] & 0xFF) << 8);
		}
	}
	
	private int readWord(InputStream inputStream) throws IOException {
		byte[] word = new byte[4];
		final int readBytes = inputStream.read(word);
		if (readBytes != word.length) {
			throw new ElfFormatException("Unexpected end of file in word");
		}
		filePosition += readBytes;
		if (elfIdentification.getEndianness() == Endianness.LITTLE) {
			return (word[0] & 0xFF) + ((word[1] & 0xFF << 8)) + ((word[2] & 0xFF) << 16) + ((word[3] & 0xFF) << 24);
		}
		else {
			return (word[3] & 0xFF) + ((word[2] & 0xFF << 8)) + ((word[1] & 0xFF) << 16) + ((word[0] & 0xFF) << 24);
		}
	}
	
	private long readXWord(InputStream inputStream) throws IOException {
		byte[] xword = new byte[8];
		final int readBytes = inputStream.read(xword);
		if (readBytes != xword.length) {
			throw new ElfFormatException("Unexpected end of file in xword");
		}
		filePosition += readBytes;
		if (elfIdentification.getEndianness() == Endianness.LITTLE) {
			return (xword[0] & 0xFFL) + ((xword[1] & 0xFFL << 8)) + ((xword[2] & 0xFFL) << 16) + ((xword[3] & 0xFFL) << 24) +
		     (xword[4] & 0xFFL << 32) + ((xword[5] & 0xFFL << 40)) + ((xword[6] & 0xFFL) << 48) + ((xword[7] & 0xFFL) << 56);
		}
		else {
			return (xword[7] & 0xFFL) + ((xword[6] & 0xFFL << 8)) + ((xword[5] & 0xFFL) << 16) + ((xword[4] & 0xFFL) << 24) +
		     (xword[3] & 0xFFL << 32) + ((xword[2] & 0xFFL << 40)) + ((xword[1] & 0xFFL) << 48) + ((xword[0] & 0xFFL) << 56);
		}
	}

	@Override
	public String toString() {
		return "Elf [elfIdentification=" + elfIdentification + ", objectType="
				+ objectType + ", machine=" + machine + ", version=" + version
				+ ", entryAddress=" + entryAddress + ", programHeaderOffset="
				+ programHeaderOffset + ", sectionHeaderOffset="
				+ sectionHeaderOffset + ", flags=" + flags + ", elfHeaderSize="
				+ elfHeaderSize + ", programHeaderEntrySize="
				+ programHeaderEntrySize + ", programHeaderEntryCount="
				+ programHeaderEntryCount + ", sectionHeaderEntrySize="
				+ sectionHeaderEntrySize + ", sectionHeaderEntryCount="
				+ sectionHeaderEntryCount + ", stringTableIndex="
				+ stringTableIndex + "]";
	}
}
