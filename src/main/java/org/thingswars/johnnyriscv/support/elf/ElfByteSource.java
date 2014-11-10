package org.thingswars.johnnyriscv.support.elf;

import org.thingswars.johnnyriscv.support.Endianness;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by rob on 10/11/14.
 */
class ElfByteSource {

    private final InputStream inputStream;

    private final ElfIdentification elfIdentification;

    private int currentPosition;

    ElfByteSource(InputStream inputStream, ElfIdentification elfIdentification) {
        this.inputStream = inputStream;
        this.elfIdentification = elfIdentification;
        currentPosition = ElfIdentification.ELF_IDENT_LENGTH;
    }

    void skipToOffset(long offset) throws IOException {
        currentPosition += inputStream.skip(offset - currentPosition);
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
        final int readBytes = inputStream.read(half);
        if (readBytes != half.length) {
            throw new ElfFormatException("Unexpected end of file in half-word");
        }
        currentPosition += readBytes;
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (half[0] & 0xFF) + ((half[1] & 0xFF) << 8);
        }
        else {
            return (half[1] & 0xFF) + ((half[0] & 0xFF) << 8);
        }
    }

    int readWord() throws IOException {
        byte[] word = new byte[4];
        final int readBytes = inputStream.read(word);
        if (readBytes != word.length) {
            throw new ElfFormatException("Unexpected end of file in word");
        }
        currentPosition += readBytes;
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (word[0] & 0xFF) + ((word[1] & 0xFF) << 8) + ((word[2] & 0xFF) << 16) + ((word[3] & 0xFF) << 24);
        }
        else {
            return (word[3] & 0xFF) + ((word[2] & 0xFF) << 8) + ((word[1] & 0xFF) << 16) + ((word[0] & 0xFF) << 24);
        }
    }

    long readXWord() throws IOException {
        byte[] xword = new byte[8];
        final int readBytes = inputStream.read(xword);
        if (readBytes != xword.length) {
            throw new ElfFormatException("Unexpected end of file in xword");
        }
        currentPosition += readBytes;
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (xword[0] & 0xFFL) + ((xword[1] & 0xFFL) << 8) + ((xword[2] & 0xFFL) << 16) + ((xword[3] & 0xFFL) << 24) +
                    (xword[4] & 0xFFL << 32) + ((xword[5] & 0xFFL << 40)) + ((xword[6] & 0xFFL) << 48) + ((xword[7] & 0xFFL) << 56);
        }
        else {
            return (xword[7] & 0xFFL) + ((xword[6] & 0xFFL) << 8) + ((xword[5] & 0xFFL) << 16) + ((xword[4] & 0xFFL) << 24) +
                    ((xword[3] & 0xFFL) << 32) + ((xword[2] & 0xFFL) << 40) + ((xword[1] & 0xFFL) << 48) + ((xword[0] & 0xFFL) << 56);
        }
    }

    long getCurrentPosition() {
        return currentPosition;
    }
}
