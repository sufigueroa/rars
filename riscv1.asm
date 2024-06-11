.global _start

.data

.text
_start:
addi a0, zero, 1
addi t1, zero, 3
slli t1, t1, 8
add a0, a0, t1
sw a0, 0xffff0000, t1
addi a0, zero, 1
sw a0, 0xffff0004, t1
addi a0, zero, 2
sw a0, 0xffff000c, t1
