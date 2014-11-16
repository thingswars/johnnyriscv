package org.thingswars.johnnyriscv.emulator.usermode.csr;

import org.thingswars.johnnyriscv.emulator.usermode.Cpu;

/**
 * Created by rob on 16/11/14.
 */
public class RdInstretH extends ReadOnlyControlStatusRegister {

    public long read(Cpu cpu) {
        return cpu.getInstructionsRetired() >>> 32;
    }

}
