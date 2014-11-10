package org.thingswars.johnnyriscv.support.elf;

public enum ElfObjectType {
	NONE,
	RELOCATABLE,
	EXCUTABLE,
	SHARED,
	CORE,
	ENVIRONMENT_SPECIFIC,
	PROCESSOR_SPECIFIC,
	UNKNOWN;
	
	private static final int LOOS = 0xFE00;
	private static final int HIOS = 0xFEFF;
	private static final int LOPROC =0xFF00;
	private static final int HIPROC =0xFFFF;
	
	public static ElfObjectType fromFileValue(int fileValue) throws ElfFormatException {
		
		switch (fileValue) {
		case 0:
			return NONE;
		case 1:			
			return RELOCATABLE;
		case 2:			
			return EXCUTABLE;
		case 3:			
			return SHARED;
		case 4:			
			return CORE;
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