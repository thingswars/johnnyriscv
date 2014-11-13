package org.thingswars.johnnyriscv.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.thingswars.johnnyriscv.emulator.usermode.MemoryManagementUnit;
import org.thingswars.johnnyriscv.support.elf.Elf;
import org.thingswars.johnnyriscv.support.elf.ElfSegment;

import com.google.common.collect.ImmutableMap;

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
      
        
        ImmutableMap<Integer, String> opcodes = ImmutableMap.<Integer, String>builder()
        		.put(b("00000"), "LOAD")
        		.put(b("01000"), "STORE")
        		.put(b("10000"), "MADD")
        		.put(b("11000"), "BRANCH")
        		
        		.put(b("00001"), "LOAD-FP")
        		.put(b("01001"), "STORE-FP")
        		.put(b("10001"), "MSUB")
        		.put(b("11001"), "JALR")
        		
        		.put(b("00010"), "custom-0")
        		.put(b("01010"), "custom-1")
        		.put(b("10010"), "NMSUB")
        		.put(b("11010"), "reserved")
        		
        		.put(b("00011"), "MISC-MEM")
        		.put(b("01011"), "AMO")
        		.put(b("10011"), "NMADD")
        		.put(b("11011"), "JAL")
        		
        		.put(b("00100"), "OP-IMM")
        		.put(b("01100"), "OP")
        		.put(b("10100"), "OP-FP")
        		.put(b("11100"), "SYSTEM")
        		
        		.put(b("00101"), "AUIPC")
        		.put(b("01101"), "LUI")
        		.put(b("10101"), "reserved")
        		.put(b("11101"), "reserved")
        		
        		.put(b("00110"), "OP-IMM-32")
        		.put(b("01110"), "OP032")
        		.put(b("10110"), "custom-2")
        		.put(b("11110"), "custom-3")
        		
        		.put(b("00111"), "48b")
        		.put(b("01111"), "64b")
        		.put(b("10111"), "48b")
        		.put(b("11111"), "80b+")

        		.build();
        
        long pc = elf.getHeader().getEntryAddress();
        while (true) {
        	int instruction = mmu.readInstruction(pc);
        	
        	int opcode = ((instruction >>> 24) & 0x7F) >> 2;
        	
        	System.out.println(opcodes.get(opcode) + ": " + Integer.toBinaryString(instruction));
        	pc += 4;
        }
    }
    
    private static Integer b(String binary) {
    	return Integer.parseInt(binary, 2);
    }
}
