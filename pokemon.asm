.global _start

.data
.include "pokemon_data.asm"

.text
beq zero, zero, _start

#.include "pokemon_utils.asm"


_start:
## Setear pokemon atacante
la a0, butterfree
sw a0, 0xffff0008, t1
addi a0, zero, 1
sw a0, 0xffff000C, t1

## Setear pokemon defensor
la a0, pidgey
sw a0, 0xffff0010, t1
addi a0, zero, 1
sw a0, 0xffff0014, t1

## Setear background
addi t0, zero, 0
slli t0, t0, 4
addi a0, t0, 2
sw a0, 0xffff0004, t1

## Iniciar batalla
addi a0, zero, 1
sw a0, 0xffff0004, t1

## Terminar turno 1
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Setear clima
addi a0, zero, 3
addi t0, zero, 1
slli t0, t0, 4
add a0, a0, t0
addi t0, zero, 5
slli t0, t0, 7
add a0, a0, t0
sw a0, 0xffff0004, t1

## Terminar turno 2
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Cambiar estado pokemon atacante
addi t0, zero, 1	## Estado nuevo
slli t0, t0, 4
addi a0, t0, 2		## CMD 2
sw a0, 0xffff000C, t1

## Terminar turno 3
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Terminar turno 4
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Terminar turno 5
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Terminar turno 6
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Evolucionar Pidgey
addi t0, zero, 17	## Estado nuevo
slli t0, t0, 4
addi a0, t0, 5		## CMD 5
sw a0, 0xffff0014, t1

## Terminar turno 7
addi a0, zero, 4
sw a0, 0xffff0004, t1

## Terminar batalla
addi t0, zero, 1
slli t0, t0, 4
addi a0, t0, 5
sw a0, 0xffff0004, t1
