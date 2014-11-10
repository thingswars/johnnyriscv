package org.thingswars.johnnyriscv.support.elf;

import java.io.IOException;

public class ElfFormatException extends IOException {

	private static final long serialVersionUID = -57752767116928300L;

	public ElfFormatException(String message) {
        super(message);
    }
}
