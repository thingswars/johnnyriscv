package org.thingswars.johnnyriscv.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.thingswars.johnnyriscv.support.elf.*;

import static org.junit.Assert.*;

public class ElfReadTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void base64() throws IOException {
		Elf elf = loadTestElf("tiny/base64");
		// Expected results based on output from "readelf -a"

//		Class:                             ELF32
//		Data:                              2's complement, little endian
//		Version:                           1 (current)
//		OS/ABI:                            UNIX - System V
//		ABI Version:                       0
//		Type:                              EXEC (Executable file)
//		Machine:                           Intel 80386
//		Version:                           0x1
//		Entry point address:               0x5b05b035
//		Start of program headers:          44 (bytes into file)
//		Start of section headers:          0 (bytes into file)
//		Flags:                             0x0
//		Size of this header:               52 (bytes)
//		Size of program headers:           32 (bytes)
//		Number of program headers:         1
//		Size of section headers:           0 (bytes)
//		Number of section headers:         0
//		Section header string table index: 0

		ElfIdentification ident = elf.getElfIdentification();
		assertEquals(ElfFormat.ELF32, ident.getFormat());
		assertEquals(0, ident.getAbiVersion());
		assertEquals(1, ident.getVersion());

		assertEquals(ElfObjectType.EXCUTABLE, elf.getObjectType());
		assertEquals(1, elf.getVersion());
		assertEquals(0x5b05b035L, elf.getEntryAddress());
		assertEquals(0, elf.getFlags());


//		Program Headers:
//		Type           Offset   VirtAddr   PhysAddr   FileSiz MemSiz  Flg Align
//		LOAD           0x000000 0x5b05b000 0xbf5b5b4b 0x00100 0x05eeb RWE 0x1000

		assertEquals(1, elf.getProgramHeaders().length);
		ElfProgramHeader elfProgramHeader = elf.getProgramHeaders()[0];
		assertEquals(ElfProgramHeaderType.LOAD, elfProgramHeader.getHeaderType());
		assertEquals(0x000000, elfProgramHeader.getOffset());
		assertEquals(0x5b05b000L, elfProgramHeader.getVirtualAddress());
		assertEquals(0xbf5b5b4bL, elfProgramHeader.getPhysicalAddress());
		assertEquals(0x00100, elfProgramHeader.getFileImageSize());
		assertEquals(0x05eeb, elfProgramHeader.getMemoryImageSize());
		assertTrue(elfProgramHeader.readPermission());
		assertTrue(elfProgramHeader.writePermission());
		assertTrue(elfProgramHeader.executePermission());
		assertEquals(0x1000, elfProgramHeader.getAlignment());
	}
	
	/**
	 * 
	 * The 'bf' sample elf file uses the elf 'class' byte to store a constant.
	 * Unfortunately, this clever trick would prevent us from detecting whether
	 * to use 32-bit or 64-bit architecture (we want to support both). So we want
	 * to reject that.
	 * 
	 * @throws IOException
	 */
	@Test
	public void rejectUnknownClass() throws IOException {
		thrown.expect(ElfFormatException.class);
		thrown.expectMessage("Unknown class 0x83");
		loadTestElf("tiny/bf");
	}
	
	private Elf loadTestElf(String name) throws IOException {
		URL resource = ElfReadTest.class.getResource("/elfsamples/" + name);
		try (InputStream inputStream = new FileInputStream(new File(resource.getFile()))) {
			return new Elf(inputStream);
		}
	}
}
