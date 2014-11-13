package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;

public class ElfProgramHeader {

    private final ElfProgramHeaderType headerType;
    private final long offset;
    private final long virtualAddress;
    private final long physicalAddress;
    private final long fileImageSize;
    private final long memoryImageSize;
    private final int flags;
    private final long alignment;

    public ElfProgramHeader(ElfByteSource elfByteSource) throws IOException {
    	if (elfByteSource.elf64()) {
    		headerType = ElfProgramHeaderType.fromFileValue(elfByteSource.readWord());    		 
    		flags = elfByteSource.readWord();
    		offset = elfByteSource.readOffset();
    		virtualAddress = elfByteSource.readAddress();
    		physicalAddress = elfByteSource.readAddress();
    		fileImageSize = elfByteSource.readXWord();
    		memoryImageSize = elfByteSource.readXWord();    		
    		alignment = elfByteSource.readXWord();  		
    	}
    	else {
    		headerType = ElfProgramHeaderType.fromFileValue(elfByteSource.readWord());
    		offset = elfByteSource.readWord();     
    		virtualAddress = elfByteSource.readAddress();
    		physicalAddress = elfByteSource.readAddress();
    		fileImageSize = elfByteSource.readWord();
    		memoryImageSize = elfByteSource.readWord();
    		flags = elfByteSource.readWord();
    		alignment = elfByteSource.readWord();
    	}
        long fileSize = elfByteSource.size();
        
        if (offset > fileSize) {
        	throw new ElfFormatException("segment offset " + offset + " past file end " + fileSize);
        }
        if ((offset+fileImageSize) > fileSize) {
        	throw new ElfFormatException("segment end " + offset+fileImageSize + " past file end " + fileSize);
        }
    }

    public ElfProgramHeaderType getHeaderType() {
        return headerType;
    }
    
    public boolean loadableSegment() {
    	return headerType == ElfProgramHeaderType.LOAD;
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

    public long getFileImageSize() {
        return fileImageSize;
    }

    public long getMemoryImageSize() {
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
