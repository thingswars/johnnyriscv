package org.thingswars.johnnyriscv.emulator.usermode;

import java.nio.ByteBuffer;

public interface Segment {
    long getAddressStart();
    long getAddressEnd();
    ByteBuffer getByteBuffer();
    boolean isExecutable();
    boolean isWriteable();
    boolean isReadable();
}
