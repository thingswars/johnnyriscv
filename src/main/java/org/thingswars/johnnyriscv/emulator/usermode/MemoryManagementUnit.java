package org.thingswars.johnnyriscv.emulator.usermode;

import java.util.ArrayList;
import java.util.List;

public class MemoryManagementUnit {

	private List<Segment> segments = new ArrayList<>();

	public void addSegment(Segment segment) {
		
		Segment existingSegment = find(segment.getAddressStart());
		if (existingSegment != null || existingSegment != null) {
			throw new RuntimeException("Segment overlap: " + segment + " and " + existingSegment);
		}
		existingSegment = find(segment.getAddressEnd());
		if (existingSegment != null || existingSegment != null) {
			throw new RuntimeException("Segment overlap: " + segment + " and " + existingSegment);
		}		
		segments.add(segment);
	}
	
	public int load16(long address) {
		Segment segment = forRead(address, 2);
		return segment.getByteBuffer().getShort((int)(address-segment.getAddressStart()));
	}
	
	public int load32(long address) {
		Segment segment = forRead(address, 4);
		return segment.getByteBuffer().getInt((int)(address-segment.getAddressStart()));
	}
	
	public long load64(long address) {
		Segment segment = forRead(address, 8);
		return segment.getByteBuffer().getLong((int)(address-segment.getAddressStart()));
	}
	
	public void store16(long address, short value) {
		Segment segment = forWrite(address);
		segment.getByteBuffer().putShort((int)(address-segment.getAddressStart()), value);
	}
	
	public void store32(long address, short value) {
		Segment segment = forWrite(address);
		segment.getByteBuffer().putInt((int)(address-segment.getAddressStart()), value);
	}
	
	public void store64(long address, short value) {
		Segment segment = forWrite(address);
		segment.getByteBuffer().putLong((int)(address-segment.getAddressStart()), value);
	}
	
	public int readInstruction(long address) {
		Segment segment = forExec(address);
		return segment.getByteBuffer().getInt((int)(address-segment.getAddressStart()));
	}
	
	private Segment forRead(long address, int bytes) {
		Segment segment = find(address);
		if (segment == null) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + " outside segments");
		}
		if (segment.getAddressEnd() < address+bytes) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + "+" + bytes + " past end of segment");
		}
		if (!segment.isReadable()) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + " in non-readable segment");
		}
		return segment;
	}
	
	private Segment forWrite(long address) {
		Segment segment = find(address);
		if (segment == null) {
			throw new RuntimeException("Seg Fault: " + Long.toHexString(address) + " outside segments");
		}
		if (!segment.isWriteable()) {
			throw new RuntimeException("Seg Fault: " + Long.toHexString(address) + " in non-writeable segment");
		}
		return segment;
	}
	
	private Segment forExec(long address) {
		Segment segment = find(address);
		if (segment == null) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + " outside segments");
		}
		if (segment.getAddressEnd() < address+4) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + "+4 past end of segment");
		}
		if (!segment.isExecutable()) {
			throw new RuntimeException("Seg Fault: 0x" + Long.toHexString(address) + " in non-executable segment");
		}
		return segment;
	}
	
	public Segment find(long address) {
		for (Segment segment : segments) {
			// TODO compare unsigned
			if (segment.getAddressStart() <= address && address <= segment.getAddressEnd()) {
				return segment;
			}
		}
		return null;
	}
}
