.global _start

.data
.include "pokemon_data.asm"

.text
beq zero, zero, _start

.include "pokemon_utils.asm"


_start:
la a0, butterfree
sw a0, 0xffff0004, t1

la a0, pidgey
sw a0, 0xffff000C, t1

addi t0, zero, 1
sw t0, 0xffff0000, t1
