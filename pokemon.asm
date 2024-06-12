.global _start

.data
.include "pokemon_data.asm"

.text
beq zero, zero, _start

.include "pokemon_utils.asm"


_start:
la a0, squirtle
jal ra, _setExpVector
sw a0, 0xffff0000, t1
la a0, squirtle
jal ra, _setHealthVector
sw a0, 0xffff0000, t1


la a0, caterpie
jal ra, _setExpVector
sw a0, 0xffff0008, t1
la a0, caterpie
jal ra, _setHealthVector
sw a0, 0xffff0008, t1

la a0, caterpie
addi t0, zero, 1
sw t0, 16(a0)
jal ra, _setHealthVector
sw a0, 0xffff0008, t1


la a0, caterpie
addi t0, zero, 1
sw t0, 20(a0)
jal ra, _setHealthVector
sw a0, 0xffff0008, t1

