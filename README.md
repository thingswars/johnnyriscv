johnnyriscv
===========

A JVM-based RISC-V emulator for sandboxing applications.

johnnyriscv has the following design goals:

 * ease of use and installation
 * embeddable within Java applications
 * respectable performance
 * extensible server-side api (peripherals, instruction set)
 * pre-canned client api and browser terminal

The name is pronounced "Johnny-Risk-Five", inspired by the robot who
came to life in the 1986 film Short Circuit. (Incidentally, by the
same director as WarGames).

license
=======

johnnyriscv itself is under the Apache License version 2. See the
LICENSE file for the legal details.

Like many emulators johnnyriscv will be aggregated with binaries
and tools for use in the emulated environment, each of which have
their own licenses.

see also
========

Official resources, specs and developer tools for RISC-V can be
found at [riscv.org](http://riscv.org/). This includes some important
official emulator and simulator projects:

 * [ANGEL](http://riscv.org/angel/) launches linux in your browser
 * [QEMU](http://riscv.org/download.html#tab_qemu) RISC-V emulation target
 * [Spike](http://riscv.org/download.html#tab_isa-sim) RISC-V ISA Simulator
