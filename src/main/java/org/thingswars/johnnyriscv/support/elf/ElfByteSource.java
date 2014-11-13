package org.thingswars.johnnyriscv.support.elf;

import org.thingswars.johnnyriscv.support.Endianness;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by rob on 10/11/14.
 */
class ElfByteSource implements ByteSource {

    private final ByteSource byteSource;

    private final ElfIdentification elfIdentification;

    ElfByteSource(ByteSource byteSource, ElfIdentification elfIdentification) {
        this.byteSource = byteSource;
        this.elfIdentification = elfIdentification;
    }

    void skipToOffset(int offset) throws IOException {
        if (offset > byteSource.size()) {
            throw new RuntimeException("Elf Offset past end of file: " + offset);
        }
        byteSource.seek(offset);
    }

    long readOffset() throws IOException {
        return readAddress();
    }

    long readAddress() throws IOException {
        if (elfIdentification.getFormat() == ElfFormat.ELF64) {
            return readXWord();
        }
        return ((long)readWord()) & 0xFFFFFFFFL;
    }

    int readHalfWord() throws IOException {
        final byte[] half = new byte[2];
        byteSource.read(half);
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (half[0] & 0xFF) + ((half[1] & 0xFF) << 8);
        }
        else {
            return (half[1] & 0xFF) + ((half[0] & 0xFF) << 8);
        }
    }

    int readWord() throws IOException {
        byte[] word = new byte[4];
        byteSource.read(word);
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (word[0] & 0xFF) + ((word[1] & 0xFF) << 8) + ((word[2] & 0xFF) << 16) + ((word[3] & 0xFF) << 24);
        }
        else {
            return (word[3] & 0xFF) + ((word[2] & 0xFF) << 8) + ((word[1] & 0xFF) << 16) + ((word[0] & 0xFF) << 24);
        }
    }

    long readXWord() throws IOException {
        byte[] xword = new byte[8];
        byteSource.read(xword);
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (xword[0] & 0xFFL) + ((xword[1] & 0xFFL) << 8) + ((xword[2] & 0xFFL) << 16) + ((xword[3] & 0xFFL) << 24) +
                    (xword[4] & 0xFFL << 32) + ((xword[5] & 0xFFL << 40)) + ((xword[6] & 0xFFL) << 48) + ((xword[7] & 0xFFL) << 56);
        }
        else {
            return (xword[7] & 0xFFL) + ((xword[6] & 0xFFL) << 8) + ((xword[5] & 0xFFL) << 16) + ((xword[4] & 0xFFL) << 24) +
                    ((xword[3] & 0xFFL) << 32) + ((xword[2] & 0xFFL) << 40) + ((xword[1] & 0xFFL) << 48) + ((xword[0] & 0xFFL) << 56);
        }
    }
    
    boolean elf64() {
    	return elfIdentification.getFormat() == ElfFormat.ELF64;
    }

	@Override
	public ByteBuffer map(boolean writeable, long offset, long size)
			throws IOException {
		return byteSource.map(writeable, offset, size);
	}

	@Override
	public long position() throws IOException {
		return byteSource.position();
	}

	@Override
	public void seek(long newPosition) throws IOException {
		byteSource.seek(newPosition);
	}

	@Override
	public int read(byte[] bytes) throws IOException {
		return byteSource.read(bytes);
	}

	@Override
	public long size() throws IOException {
		return byteSource.size();
	}
}
