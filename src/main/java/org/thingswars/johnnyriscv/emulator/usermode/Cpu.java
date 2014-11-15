package org.thingswars.johnnyriscv.emulator.usermode;

/**
 * Created by rob on 13/11/14.
 */
public class Cpu {

    private final long gpr[] = new long[32];

    private long pc = 0x2000;

    private final MemoryManagementUnit mmu;

    public Cpu(MemoryManagementUnit mmu) {
        this.mmu = mmu;
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
                throw new UnsupportedInstruction("BRANCH", pc);

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
    }

    public void opImm(int instruction) {
        int rd = (instruction >>> 7) & 0b11111;
        int rs1 = (instruction >>> 15) & 0b11111;
        int funct3 = (instruction >>> 12) & 0xb111;
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

            case 0xb111: // ANDI
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
            case 0b010:
                throw new UnsupportedInstruction("CSRRS", pc);
            default:
                throw new IllegalInstruction(pc);
        }

    }
}
