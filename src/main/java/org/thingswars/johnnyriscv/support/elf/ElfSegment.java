package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by rob on 13/11/14.
 */
public class ElfSegment {

    private final long alignedStart;
    private final long alignedEnd;

    private final long addressStart;

    private final ByteBuffer byteBuffer;

    private final boolean executable;
    private final boolean writeable;
    private final boolean readable;

    ElfSegment(ElfProgramHeader programHeader, FileChannel fileChannel) throws IOException {
        readable = programHeader.readPermission();
        writeable = programHeader.writePermission();
        executable = programHeader.executePermission();
        FileChannel.MapMode mode;
        addressStart = programHeader.getVirtualAddress();

        int offset = (int)programHeader.getOffset();
        int size = programHeader.getFileImageSize();

        if (writeable) {
            // copy-on-write so that we don't modify the elf binary file
            mode = FileChannel.MapMode.PRIVATE;
        }
        else {
            mode = FileChannel.MapMode.READ_ONLY;
        }
        byteBuffer = fileChannel.map(mode, offset, size);

        long alignmentMask = 0; // TODO alignment

        alignedStart = addressStart;
        alignedEnd = addressStart + programHeader.getMemoryImageSize();
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
