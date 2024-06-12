.global _start

.data
.include "pokemon_data.asm"

.text
beq zero, zero, _start

.include "pokemon_utils.asm"


_start:
la a0, charmander
jal ra, _getVector
sw a0, 0xffff0000, t1

la a0, squirtle
jal ra, _getVector
sw a0, 0xffff0008, t1