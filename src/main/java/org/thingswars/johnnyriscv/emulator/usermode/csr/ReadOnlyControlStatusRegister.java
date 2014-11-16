package org.thingswars.johnnyriscv.emulator.usermode.csr;

import org.thingswars.johnnyriscv.emulator.usermode.ControlStatusRegister;
import org.thingswars.johnnyriscv.emulator.usermode.Cpu;
import org.thingswars.johnnyriscv.emulator.usermode.IllegalInstruction;

/**
 * Created by rob on 16/11/14.
 */
public abstract class ReadOnlyControlStatusRegister implements ControlStatusRegister {

    public abstract long read(Cpu cpu);

    @Override
    public long readAndWrite(Cpu cpu, long newValue) {
        throw new RuntimeException("Attempt to write read-only CSR");
    }

    @Override
    public long readAndSet(Cpu cpu, long mask) {
        if (mask != 0) {
            throw new RuntimeException("Attempt to set bits in read-only CSR");
        }
        return read(cpu);
    }

    @Override
    public long readAndClear(Cpu cpu, long mask) {
        if (mask != 0) {
            throw new RuntimeException("Attempt to clear bits in read-only CSR");
        }
        return read(cpu);
    }
}
