package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
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
	
	public ElfIdentification(ByteSource byteSource) throws IOException {
		
		byte[] ident = new byte[ELF_IDENT_LENGTH];
		
		if (byteSource.read(ident) < ELF_IDENT_LENGTH) {
			throw new ElfFormatException("Not an elf file: ident header too short");
		}

		if (ident[0] != 0x7F || ident[1] != 'E' || ident[2] != 'L' || ident[3] != 'F') {
			throw new ElfFormatException("Not an elf file: invalid magic number");
		}

		switch (ident[EI_CLASS]) {
		case 0:
			throw new ElfFormatException("Invalid class 0 in identification header");
		case 1:
			format = ElfFormat.ELF32;
			break;
		case 2:
			format = ElfFormat.ELF64;
			break;
		default:
			throw new ElfFormatException("Unknown class 0x" + Integer.toHexString(ident[EI_CLASS] & 0xFF));
		}
		
		switch (ident[EI_DATA]) {
		case 0:
			throw new ElfFormatException("Invalid data encoding 0");
		case 1:
			endianness = Endianness.LITTLE;
			break;
		case 2:
			endianness = Endianness.BIG;
			break;
		default:
			throw new ElfFormatException("Unsupported data encoding " + ident[EI_DATA]);
		}
		
		version = ident[EI_VERSION];
		operatingSystemAbi = ident[EI_OSABI];
		abiVersion = ident[EI_ABIVERSION];
		
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
