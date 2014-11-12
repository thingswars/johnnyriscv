package org.thingswars.johnnyriscv.support.elf;

import org.thingswars.johnnyriscv.support.Endianness;

import java.nio.ByteBuffer;

/**
 * Created by rob on 10/11/14.
 */
class ElfByteSource {

    private final ByteBuffer byteBuffer;

    private final ElfIdentification elfIdentification;

    ElfByteSource(ByteBuffer byteBuffer, ElfIdentification elfIdentification) {
        this.byteBuffer = byteBuffer;
        this.elfIdentification = elfIdentification;
    }

    void skipToOffset(int offset) {
        if (offset > byteBuffer.capacity()) {
            throw new RuntimeException("Elf Offset past end of file: " + offset);
        }
        byteBuffer.position(offset);
    }

    int readOffset() {
        // ByteBuffer means we cannot support offsets that exceed 32-bit integer
        long offset = readAddress();
        if (offset > Integer.MAX_VALUE) {
            throw new RuntimeException("Offsets past " + Integer.MAX_VALUE + " not supported");
        }
        return (int)offset;
    }

    long readAddress() {
        if (elfIdentification.getFormat() == ElfFormat.ELF64) {
            return readXWord();
        }
        return ((long)readWord()) & 0xFFFFFFFFL;
    }

    int readHalfWord() {
        final byte[] half = new byte[2];
        byteBuffer.get(half);
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (half[0] & 0xFF) + ((half[1] & 0xFF) << 8);
        }
        else {
            return (half[1] & 0xFF) + ((half[0] & 0xFF) << 8);
        }
    }

    int readWord() {
        byte[] word = new byte[4];
        byteBuffer.get(word);
        if (elfIdentification.getEndianness() == Endianness.LITTLE) {
            return (word[0] & 0xFF) + ((word[1] & 0xFF) << 8) + ((word[2] & 0xFF) << 16) + ((word[3] & 0xFF) << 24);
        }
        else {
            return (word[3] & 0xFF) + ((word[2] & 0xFF) << 8) + ((word[1] & 0xFF) << 16) + ((word[0] & 0xFF) << 24);
        }
    }

    long readXWord() {
        byte[] xword = new byte[8];
        byteBuffer.get(xword);
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
        return byteBuffer.position();
    }
}
