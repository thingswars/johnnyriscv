package org.thingswars.johnnyriscv.emulator.usermode;

/**
 * Created by rob on 15/11/14.
 */
public class IllegalInstruction extends RuntimeException {

    final long pc;

    public IllegalInstruction(long pc) {
        super("Illegal Instruction at 0x" + Long.toHexString(pc));
        this.pc = pc;
    }

    public IllegalInstruction(String message, long pc) {
        super(message);
        this.pc = pc;
    }

    public long getPc() {
        return pc;
    }
}
