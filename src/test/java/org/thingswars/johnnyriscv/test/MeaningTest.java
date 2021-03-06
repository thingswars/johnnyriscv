package org.thingswars.johnnyriscv.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.thingswars.johnnyriscv.emulator.usermode.MemoryManagementUnit;
import org.thingswars.johnnyriscv.support.elf.Elf;
import org.thingswars.johnnyriscv.support.elf.ElfSegment;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by rob on 13/11/14.
 */
public class MeaningTest {

    @Test
    public void load() throws IOException {
        URL resource = ElfReadTinyTest.class.getResource("/elfsamples/meaning64");

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resource.getFile(), "rw")) {
            try (FileChannel fileChannel = randomAccessFile.getChannel()) {
                run(fileChannel);
            }
        }
    }

    public void run(FileChannel fileChannel) throws IOException {
        Elf elf = new Elf(fileChannel);
        List<ElfSegment> segments = elf.getSegments();
        assertEquals(1, segments.size());
        /* from readelf:
         * Program Headers:
  Type           Offset             VirtAddr           PhysAddr
                 FileSiz            MemSiz              Flags  Align
  LOAD           0x0000000000002000 0x0000000000010000 0x0000000000010000
                 0x0000000000001558 0x00000000000015b0  RWE    2000

         */
        ElfSegment segment = segments.get(0);
        assertTrue(segment.isExecutable());
        assertTrue(segment.isWriteable());
        assertTrue(segment.isReadable());
        assertEquals(0x10000, segment.getAddressStart());
        assertEquals(0x10000, segment.getAlignedStart());
        assertEquals(0x12000, segment.getAlignedEnd());
        
        MemoryManagementUnit mmu = new MemoryManagementUnit();
        mmu.addSegment(segment);
        
        long pc = elf.getHeader().getEntryAddress();
    }
    
    private static Integer b(String binary) {
    	return Integer.parseInt(binary, 2);
    }
}
