package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.io.InputStream;

public class ElfProgramHeader {

    private final ElfProgramHeaderType headerType;
    private final long offset;
    private final long virtualAddress;
    private final long physicalAddress;
    private final int fileImageSize;
    private final int memoryImageSize;
    private final int flags;
    private final long alignment;

    public ElfProgramHeader(ElfByteSource elfByteSource) throws IOException {
        long currentPosition = elfByteSource.getCurrentPosition();
        headerType = ElfProgramHeaderType.fromFileValue(elfByteSource.readWord());
        offset = elfByteSource.readOffset();
        virtualAddress = elfByteSource.readAddress();
        physicalAddress = elfByteSource.readAddress();
        fileImageSize = elfByteSource.readWord();
        memoryImageSize = elfByteSource.readWord();
        flags = elfByteSource.readWord();
        alignment = elfByteSource.readWord();
    }

    public ElfProgramHeaderType getHeaderType() {
        return headerType;
    }

    public long getOffset() {
        return offset;
    }

    public long getVirtualAddress() {
        return virtualAddress;
    }

    public long getPhysicalAddress() {
        return physicalAddress;
    }

    public int getFileImageSize() {
        return fileImageSize;
    }

    public int getMemoryImageSize() {
        return memoryImageSize;
    }

    public int getFlags() {
        return flags;
    }

    public long getAlignment() {
        return alignment;
    }

    public boolean alignmentRequired() {
        return alignment != 0 && alignment != 1;
    }

    public boolean executePermission() {
        return (flags & 1) != 0;
    }

    public boolean writePermission() {
        return (flags & 2) != 0;
    }

    public boolean readPermission() {
        return (flags & 4) != 0;
    }

    @Override
    public String toString() {
        return  "[PROG HEADER: " + headerType +
                ", offset=" + offset +
                ", virtualAddress=" + Long.toHexString(virtualAddress) +
                ", physicalAddress=" + Long.toHexString(physicalAddress) +
                ", fileImageSize=" + Long.toHexString(fileImageSize) +
                ", memoryImageSize=" + Long.toHexString(memoryImageSize) +
                ", flags=" + Integer.toBinaryString(flags) +
                ", alignment=" + alignment +
                ']';
    }
}
