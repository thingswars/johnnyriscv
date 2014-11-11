package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.io.InputStream;

import org.thingswars.johnnyriscv.support.Endianness;

public class ElfIdentification {

	protected static final int ELF_IDENT_LENGTH = 16;
	
	private static final int EI_CLASS = 0x04;
	private static final int EI_DATA = 0x05;
	private static final int EI_VERSION = 0x06;
	private static final int EI_OSABI = 0x07;
	private static final int EI_ABIVERSION = 0x08;
	
	private final ElfFormat format;
	private final Endianness endianness;
	private final byte version;
	private final  byte operatingSystemAbi;
	private final byte abiVersion;
	
	public ElfIdentification(InputStream inputStream) throws IOException {
		byte[] bytes = new byte[ELF_IDENT_LENGTH];
		int numberReadBytes = inputStream.read(bytes, 0, ELF_IDENT_LENGTH);
		if (numberReadBytes == -1) {
			throw new ElfFormatException("Empty file");
		}
		if (numberReadBytes >= 4) {
			if (bytes[0] != 0x7F || bytes[1] != 'E' || bytes[2] != 'L' || bytes[3] != 'F') {
				throw new ElfFormatException("Invalid magic number");
			}
		}
		if (numberReadBytes != ELF_IDENT_LENGTH) {
			throw new ElfFormatException("ELF identification header too short (" + numberReadBytes + " bytes)");
		}
		
		switch (bytes[EI_CLASS]) {
		case 0:
			throw new ElfFormatException("Invalid class 0 in identification header");
		case 1:
			format = ElfFormat.ELF32;
			break;
		case 2:
			format = ElfFormat.ELF64;
			break;
		default:
			throw new ElfFormatException("Unknown class 0x" + Integer.toHexString(bytes[EI_CLASS] & 0xFF));
		}
		
		switch (bytes[EI_DATA]) {
		case 0:
			throw new ElfFormatException("Invalid data encoding 0");
		case 1:
			endianness = Endianness.LITTLE;
			break;
		case 2:
			endianness = Endianness.BIG;
			break;
		default:
			throw new ElfFormatException("Unsupported data encoding " + bytes[EI_DATA]);
		}
		
		version = bytes[EI_VERSION];
		operatingSystemAbi = bytes[EI_OSABI];
		abiVersion = bytes[EI_ABIVERSION];
		
	}

	public ElfFormat getFormat() {
		return format;
	}

	public Endianness getEndianness() {
		return endianness;
	}

	public byte getVersion() {
		return version;
	}

	public byte getOperatingSystemAbi() {
		return operatingSystemAbi;
	}

	public byte getAbiVersion() {
		return abiVersion;
	}

	@Override
	public String toString() {
		return format +
				" v" + version + " "
				+ endianness + "-ENDIAN";
	}
	
	
}
