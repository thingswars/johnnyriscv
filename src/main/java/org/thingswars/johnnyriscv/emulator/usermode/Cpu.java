package org.thingswars.johnnyriscv.emulator.usermode;

import org.thingswars.johnnyriscv.emulator.usermode.csr.*;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Created by rob on 13/11/14.
 */
public class Cpu {

    private final long gpr[] = new long[32];

    private final ControlStatusRegister csr[] = new ControlStatusRegister[4096];

    private long pc = 0x2000;
    private long instructionsRetired = 0;

    private final MemoryManagementUnit mmu;

    private final ThreadMXBean threadMXBean;

    private final boolean useThreadCpuTime;

    public static final int RDCYCLE =    0b110000000000;
    public static final int RDCYCLEH =   0b110010000000;
    public static final int RDTIME =     0b110000000001;
    public static final int RDTIMEH =    0b110010000001;
    public static final int RDINSTRET =  0b110000000010;
    public static final int RDINSTRETH = 0b110010000010;

    public Cpu(MemoryManagementUnit mmu) {
        this.mmu = mmu;
        // Try to isolate thread cpu time
        threadMXBean = ManagementFactory.getThreadMXBean();
        if (threadMXBean.isCurrentThreadCpuTimeSupported()) {
            useThreadCpuTime = true;
            if (!threadMXBean.isThreadCpuTimeEnabled()) {
                threadMXBean.setThreadCpuTimeEnabled(true);
            }
        }
        else {
            useThreadCpuTime = false;
        }

        csr[RDCYCLE] = new RdCycle();
        csr[RDCYCLEH] = new RdCycle();
        csr[RDTIME] = new RdTime();
        csr[RDTIMEH] = new RdTimeH();
        csr[RDINSTRET] = new RdInstret();
        csr[RDINSTRETH] = new RdInstretH();
        csr[0b101000] = new NoopControlStatusRegister(); // TODO
    }

    public void setProgramCounter(long pc) {
        this.pc = pc;
    }

    public void executeInstruction() {
        int instruction = mmu.readInstruction(pc);
        executeInstruction(instruction);
    }

    public void executeInstruction(int instruction) {

        int majorOpcode = (instruction & 0x7f) >> 2;

        switch (majorOpcode) {
            case 0b00000: // "LOAD"
                throw new UnsupportedInstruction("LOAD", pc);
            
            case 0b01000: // "STORE"
                throw new UnsupportedInstruction("STORE", pc);
            case 0b10000: // "MADD"
                throw new UnsupportedInstruction("MADD", pc);
            case 0b11000: // "BRANCH"
                branch(instruction);
                break;

            case 0b00001: // "LOAD-FP"
                throw new UnsupportedInstruction("LOAD-FP", pc);
            case 0b01001: // "STORE-FP"
                throw new UnsupportedInstruction("TORE-FP", pc);
            case 0b10001: // "MSUB"
                throw new UnsupportedInstruction("MSUB", pc);
            case 0b11001: // "JALR"
                throw new UnsupportedInstruction("JALR", pc);

            case 0b10010: // "NMSUB"
                throw new UnsupportedInstruction("NMSUB", pc);
            case 0b11010: // "reserved"
                throw new UnsupportedInstruction("reserved", pc);

            case 0b00011: // "MISC-MEM"
                throw new UnsupportedInstruction("MISC-MEM", pc);
            case 0b01011: // "AMO"
                throw new UnsupportedInstruction("AMO", pc);
            case 0b10011: // "NMADD"
                throw new UnsupportedInstruction("NMADD", pc);
            case 0b11011: // "JAL"
                throw new UnsupportedInstruction("JAL", pc);

            case 0b00100: // "OP-IMM"
                opImm(instruction);
                pc += 4;
                break;
            case 0b01100: // "OP"
                throw new UnsupportedInstruction("OP", pc);
            case 0b10100: // "OP-FP"
                throw new UnsupportedInstruction("OP-FP", pc);
            case 0b11100: // "SYSTEM"
                system(instruction);
                pc += 4;
                break;

            case 0b00101: // "AUIPC"
                throw new UnsupportedInstruction("AUIPC", pc);
            case 0b01101: // "LUI"
                throw new UnsupportedInstruction("LUI", pc);


            case 0b00110: // "OP-IMM-32"
                throw new UnsupportedInstruction("OP-IMM-32", pc);
            case 0b01110: // "OP032"
                throw new UnsupportedInstruction("OP032", pc);

            case 0b10101: // "reserved"
            case 0b11101: // "reserved"
                throw new UnsupportedInstruction("reserved", pc);

            case 0b00010: // "custom-0"
            case 0b01010: // "custom-1"
            case 0b10110: // "custom-2"
            case 0b11110: // "custom-3"
                throw new UnsupportedInstruction("custom", pc);
            case 0b00111: // "48b"
            case 0b01111: // "64b"
            case 0b10111: // "48b"
            case 0b11111: // "80b+"
                throw new UnsupportedInstruction("extensions", pc);
            default:
                throw new IllegalInstruction(pc);
        }

        ++instructionsRetired;
    }

    public void branch(int instruction) {
        int funct3 = (instruction >>> 12) & 0b111;
        long rs1 = gpr[(instruction >>> 15) & 0b11111];
        long rs2 = gpr[(instruction >>> 20) & 0b11111];

        boolean branch = false;

        switch (funct3) {
            case 0b000: // BEQ
                branch = rs1 == rs2;
                break;

            case 0b001: // BNE
                branch = rs1 != rs2;
                break;

            case 0b100: // BLT
                branch = rs1 < rs2;
                break;

            case 0b101: // BGE
                branch = rs1 > rs2;
                break;

            case 0b110: // BLTU
                branch = rs1 < rs2; // TODO unsigned
                break;

            case 0b111: // BGTU
                branch = rs1 > rs2; // TODO unsigned
                break;

            default:
                throw new IllegalInstruction("Unknown BRANCH funct3 " +
                        Integer.toBinaryString(funct3) +
                        " at " + Long.toHexString(pc), pc);
        }

        if (branch) {
            throw new RuntimeException("Branch, please!");
        }
        else {
            pc += 4;
        }
    }

    public void opImm(int instruction) {
        int rd = (instruction >>> 7) & 0b11111;
        int rs1 = (instruction >>> 15) & 0b11111;
        int funct3 = (instruction >>> 12) & 0x111;
        long immediate = (instruction >> 20);

        switch (funct3) {
            case 0b000: // ADDI
                gpr[rd] = gpr[rs1] + immediate;
                break;

            case 0b001: // SLLI
                throw new UnsupportedInstruction("SLLI", pc);

            case 0b010: // SLTI
                throw new UnsupportedInstruction("SLTI", pc);

            case 0b011: // SLTIU, need to check signExt here
                throw new UnsupportedInstruction("SLTUI", pc);

            case 0b100: // XORI
                gpr[rd] = gpr[rs1] ^ immediate;
                break;

            case 0b101: // SRLI and SRAI
                throw new UnsupportedInstruction("SRLI", pc);

            case 0b110: // ORI
                gpr[rd] = gpr[rs1] | immediate;
                break;

            case 0b111: // ANDI
                gpr[rd] = gpr[rs1] & immediate;
                break;
            default:
                throw new IllegalInstruction(pc);
        }
    }

    void system(int instruction) {
        int rd = (instruction >> 7) & 0b11111;
        int funct3 = (instruction >> 12) & 0b111;
        int rs1 = (instruction >> 15) & 0b11111;
        int funct7 = (instruction >> 25) & 0b1111111;

        switch (funct3) {
            case 0b000: // PRIV
                if (funct7 == 0 && rd == 0 && rs1 == 0) {
                    throw new UnsupportedInstruction("SCALL", pc);
                }
                else if (funct7 == 1 && rd == 0 && rs1 == 0) {
                    throw new UnsupportedInstruction("SBREAK", pc);
                }
                throw new IllegalInstruction(pc);
            case 0b001:
                throw new IllegalInstruction("CSRW", pc);
            case 0b010:
                csrrs(funct7, rd, rs1);
                break;
            case 0b011:
                csrrc(funct7, rd, rs1);
                break;

            case 0b100:
                throw new IllegalInstruction("CSRWI", pc);

            case 0b101:
                throw new IllegalInstruction("CSRSI", pc);

            case 0b110:
                throw new IllegalInstruction("CSRCI", pc);

            case 0b111:
                throw new IllegalInstruction("0b111", pc);

            default:
                throw new IllegalInstruction(pc);
        }
    }

    public long getInstructionsRetired() {
        return instructionsRetired;
    }

    public long getCycleTime() {
        if (useThreadCpuTime) {
            return threadMXBean.getCurrentThreadCpuTime();
        }
        return System.nanoTime();
    }

    private void csrrs(int funct7, int rd, int rs1) {

        ControlStatusRegister controlStatusRegister = csr[funct7];

        if (controlStatusRegister == null) {
            throw new IllegalInstruction(
                    "Undefined CSR " + Integer.toBinaryString(funct7) + " in CSRRS at " + Long.toHexString(pc), pc);
        }

        gpr[rd] = controlStatusRegister.readAndSet(this, rs1);
    }

    private void csrrc(int funct7, int rd, int rs1) {

        ControlStatusRegister controlStatusRegister = csr[funct7];

        if (controlStatusRegister == null) {
            throw new IllegalInstruction(
                    "Undefined CSR " + Integer.toBinaryString(funct7) + " in CSRRC at " + Long.toHexString(pc), pc);
        }

        gpr[rd] = controlStatusRegister.readAndClear(this, rs1);
    }
}
