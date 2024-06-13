.global _start

.data
.include "pokemon_data.asm"

.text
beq zero, zero, _start

#.include "pokemon_utils.asm"


_start:
la a0, butterfree
sw a0, 0xffff0008, t1

la a0, pidgey
sw a0, 0xffff0010, t1

addi a0, zero, 2
addi t0, zero, 0
slli t0, t0, 4
add a0, a0, t0
sw a0, 0xffff0004, t1

addi a0, zero, 1
sw a0, 0xffff0004, t1

addi a0, zero, 3
addi t0, zero, 1
slli t0, t0, 4
add a0, a0, t0
addi t0, zero, 5
slli t0, t0, 7
add a0, a0, t0
sw a0, 0xffff0004, t1



addi a0, zero, 4
sw a0, 0xffff0004, t1

addi a0, zero, 4
sw a0, 0xffff0004, t1

addi a0, zero, 4
sw a0, 0xffff0004, t1


addi a0, zero, 4
sw a0, 0xffff0004, t1

#addi a0, zero, 4
#sw a0, 0xffff0004, t1


#addi a0, zero, 4
#sw a0, 0xffff0004, t1
