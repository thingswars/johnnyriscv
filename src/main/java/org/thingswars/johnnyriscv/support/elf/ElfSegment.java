package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.thingswars.johnnyriscv.emulator.usermode.Segment;

import com.google.common.primitives.UnsignedLongs;

/**
 * Created by rob on 13/11/14.
 */
public class ElfSegment implements Segment {

    private final long alignedStart;
    private final long alignedEnd;

    private final long addressStart;
    private final long addressEnd;
    private final long addressMemoryEnd;

    private final ByteBuffer byteBuffer;

    private final boolean executable;
    private final boolean writeable;
    private final boolean readable;

    ElfSegment(ElfProgramHeader programHeader, ByteSource byteSource) throws IOException {
        readable = programHeader.readPermission();
        writeable = programHeader.writePermission();
        executable = programHeader.executePermission();
        addressStart = programHeader.getVirtualAddress(); 

        long offset = programHeader.getOffset();
        long fileSize = programHeader.getFileImageSize();
        long memorySize = programHeader.getMemoryImageSize();
        
        addressEnd = addressStart + fileSize;
        addressMemoryEnd = addressStart + memorySize;
     
        byteBuffer = byteSource.map(false, offset, fileSize);

        alignedStart = addressStart - UnsignedLongs.remainder(addressStart, programHeader.getAlignment());
        alignedEnd = addressMemoryEnd + (programHeader.getAlignment() - UnsignedLongs.remainder(addressMemoryEnd, programHeader.getAlignment()));
    }

    public long getAlignedStart() {
        return alignedStart;
    }

    public long getAlignedEnd() {
        return alignedEnd;
    }

    public long getAddressStart() {
        return addressStart;
    }
    
    public long getAddressEnd() {
    	return addressEnd;
    }
    
    public long getAddressMemoryEnd() {
    	return addressEnd;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public boolean isExecutable() {
        return executable;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public boolean isReadable() {
        return readable;
    }

}
