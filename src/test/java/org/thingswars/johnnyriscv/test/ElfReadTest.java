package org.thingswars.johnnyriscv.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.thingswars.johnnyriscv.support.elf.Elf;
import org.thingswars.johnnyriscv.support.elf.ElfFormatException;

public class ElfReadTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void base64() throws IOException {
		// TODO: assertions would be a good thing.
		System.out.println(loadTestElf("tiny/base64"));
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
