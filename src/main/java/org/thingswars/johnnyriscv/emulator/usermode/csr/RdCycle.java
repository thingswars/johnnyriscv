package org.thingswars.johnnyriscv.emulator.usermode.csr;

import org.thingswars.johnnyriscv.emulator.usermode.ControlStatusRegister;
import org.thingswars.johnnyriscv.emulator.usermode.Cpu;

/**
 * Created by rob on 16/11/14.
 */
public class RdCycle extends ReadOnlyControlStatusRegister {

    public long read(Cpu cpu) {
        return cpu.getCycleTime();
    }

}
