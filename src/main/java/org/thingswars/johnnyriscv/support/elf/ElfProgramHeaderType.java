package org.thingswars.johnnyriscv.support.elf;

public enum ElfProgramHeaderType {
	NULL,
	LOAD,
	DYNAMIC,
	INTERP,
	NOTE,
	SHLIB,
	PHDR,
	ENVIRONMENT_SPECIFIC,
	PROCESSOR_SPECIFIC,
	UNKNOWN;

	private static int LOOS = 0x60000000;
	private static int HIOS = 0x6fffffff;
	private static int LOPROC = 0x70000000;
	private static int HIPROC = 0x7fffffff;
	
	public static ElfProgramHeaderType fromFileValue(int fileValue) throws ElfFormatException {
		switch (fileValue) {
		case 0:
			return NULL;
		case 1:				
			return LOAD;
		case 2:				
			return DYNAMIC;
		case 3:				
			return INTERP;
		case 4:				
			return NOTE;
		case 5:				
			return SHLIB;
		case 6:				
			return PHDR;
		default:
			if (fileValue >= LOOS && fileValue <= HIOS) {
				return ENVIRONMENT_SPECIFIC;
			}
			if (fileValue >= LOPROC && fileValue <= HIPROC) {
				return PROCESSOR_SPECIFIC;
			}
			return UNKNOWN;
		}
	}
	
}