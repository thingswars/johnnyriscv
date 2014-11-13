package org.thingswars.johnnyriscv.emulator.usermode;

/**
 * Created by rob on 13/11/14.
 */
public class Cpu {

    public void executeInstruction(int instruction) {

        int opcode = ((instruction >>> 24) & 0x7F) >> 2;

        switch (opcode) {
            case 0b00000: // "LOAD"
            
            case 0b01000: // "STORE"
            case 0b10000: // "MADD"
            case 0b11000: // "BRANCH"

            case 0b00001: // "LOAD-FP"
            case 0b01001: // "STORE-FP"
            case 0b10001: // "MSUB"
            case 0b11001: // "JALR"

            case 0b00010: // "custom-0"
            case 0b01010: // "custom-1"
            case 0b10010: // "NMSUB"
            case 0b11010: // "reserved"

            case 0b00011: // "MISC-MEM"
            case 0b01011: // "AMO"
            case 0b10011: // "NMADD"
            case 0b11011: // "JAL"

            case 0b00100: // "OP-IMM"
            case 0b01100: // "OP"
            case 0b10100: // "OP-FP"
            case 0b11100: // "SYSTEM"

            case 0b00101: // "AUIPC"
            case 0b01101: // "LUI"
            case 0b10101: // "reserved"
            case 0b11101: // "reserved"

            case 0b00110: // "OP-IMM-32"
            case 0b01110: // "OP032"
            case 0b10110: // "custom-2"
            case 0b11110: // "custom-3"

            case 0b00111: // "48b"
            case 0b01111: // "64b"
            case 0b10111: // "48b"
            case 0b11111: // "80b+"
        }

    }
}
