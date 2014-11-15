package org.thingswars.johnnyriscv.test;

import org.junit.Ignore;
import org.junit.Test;
import org.thingswars.johnnyriscv.emulator.usermode.Cpu;
import org.thingswars.johnnyriscv.emulator.usermode.MemoryManagementUnit;
import org.thingswars.johnnyriscv.support.elf.Elf;
import org.thingswars.johnnyriscv.support.elf.ElfSegment;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rob on 15/11/14.
 */
public class Rv32uiTest {

    @Test
    @Ignore
    public void load() throws IOException {
        URL resource = ElfReadTinyTest.class.getResource("/elfsamples/rv32ui/rv32ui-p-add");

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
/*
Program Headers:
  Type           Offset   VirtAddr   PhysAddr   FileSiz MemSiz  Flg Align
  LOAD           0x001000 0x00002000 0x00002000 0x00520 0x00520 R E 0x1000
 */
        ElfSegment segment = segments.get(0);
        assertTrue(segment.isExecutable());
        assertFalse(segment.isWriteable());
        assertTrue(segment.isReadable());
        assertEquals(0x2000, segment.getAddressStart());
        assertEquals(0x2000, segment.getAlignedStart());
        assertEquals(0x3000, segment.getAlignedEnd());

        MemoryManagementUnit mmu = new MemoryManagementUnit();
        mmu.addSegment(segment);

        long pc = elf.getHeader().getEntryAddress();
        Cpu cpu = new Cpu(mmu);
        cpu.setProgramCounter(pc);

        cpu.executeInstruction();
    }

}
