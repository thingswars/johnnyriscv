package org.thingswars.johnnyriscv.emulator.usermode;

/**
 * Created by rob on 15/11/14.
 */
public class UnsupportedInstruction extends IllegalInstruction {

    public UnsupportedInstruction(String name, long pc) {
        super("Illegal Instruction (" + name + " not supported) at 0x" + Long.toHexString(pc), pc);
    }
}