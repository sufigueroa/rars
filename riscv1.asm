.global _start

.data

.text
_start:
addi a0, zero, 1
sw a0, 0xffff0000, t1
addi a0, zero, 1
sw a0, 0xffff0004, t1
addi a0, zero, 3
sw a0, 0xffff0008, t1
addi a0, zero, 2
sw a0, 0xffff000c, t1
