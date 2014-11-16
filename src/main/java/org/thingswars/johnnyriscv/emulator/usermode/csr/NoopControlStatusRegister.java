package org.thingswars.johnnyriscv.emulator.usermode.csr;

import org.thingswars.johnnyriscv.emulator.usermode.ControlStatusRegister;
import org.thingswars.johnnyriscv.emulator.usermode.Cpu;

/**
 * Created by rob on 16/11/14.
 */
public class NoopControlStatusRegister implements ControlStatusRegister {

    @Override
    public long readAndWrite(Cpu cpu, long newValue) {
        return 0;
    }

    @Override
    public long readAndSet(Cpu cpu, long mask) {
        return 0;
    }

    @Override
    public long readAndClear(Cpu cpu, long mask) {
        return 0;
    }
}
