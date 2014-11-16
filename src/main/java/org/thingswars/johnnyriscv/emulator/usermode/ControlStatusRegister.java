package org.thingswars.johnnyriscv.emulator.usermode;

/**
 * Created by rob on 16/11/14.
 */
public interface ControlStatusRegister {

    long readAndWrite(Cpu cpu, long newValue);
    long readAndSet(Cpu cpu, long mask);
    long readAndClear(Cpu cpu, long mask);

}
