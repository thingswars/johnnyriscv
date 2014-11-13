package org.thingswars.johnnyriscv.test;

import org.junit.Test;
import org.thingswars.johnnyriscv.support.elf.Elf;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by rob on 13/11/14.
 */
public class MeaningTest {

    @Test
    public void load() throws IOException {
        URL resource = ElfReadTinyTest.class.getResource("/elfsamples/meaning64");

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resource.getFile(), "r")) {
            try (FileChannel fileChannel = randomAccessFile.getChannel()) {
                run(fileChannel);
            }
        }
    }

    public void run(FileChannel fileChannel) throws IOException {
        Elf elf = new Elf(fileChannel);

    }
}
