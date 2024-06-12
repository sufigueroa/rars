.global _getVector
.text
_getVector:
add t0, zero, a0
add a0, zero, zero

lw t1, 0(t0)
slli t1, t1, 24
add a0, a0, t1

lw t1, 4(t0)
slli t1, t1, 21
add a0, a0, t1

lw t1, 8(t0)
slli t1, t1, 14
add a0, a0, t1

lw t1, 12(t0)
slli t1, t1, 8
add a0, a0, t1

lw t1, 16(t0)
slli t1, t1, 6
add a0, a0, t1

lw t1, 20(t0)
slli t1, t1, 0
add a0, a0, t1

jalr zero, 0(ra)

_setHealthVector:
    add t0, zero, a0
    add a0, zero, zero

    # Modo de comando
    addi t1, zero, 2
    slli t1, t1, 28
    add a0, a0, t1

    # Estado
    lw t1, 16(t0)
    slli t1, t1, 8
    add a0, a0, t1

    # HP
    lw t1, 20(t0)
    slli t1, t1, 0
    add a0, a0, t1

    jalr zero, 0(ra)

_setExpVector:
    add t0, zero, a0
    add a0, zero, zero

    # Modo de comando
    addi t1, zero, 1
    slli t1, t1, 28
    add a0, a0, t1

    # ID
    lw t1, 0(t0)
    slli t1, t1, 18
    add a0, a0, t1

    # Tipo
    lw t1, 4(t0)
    slli t1, t1, 14
    add a0, a0, t1

    # Nivel
    lw t1, 8(t0)
    slli t1, t1, 7
    add a0, a0, t1

    # Exp
    lw t1, 12(t0)
    slli t1, t1, 0
    add a0, a0, t1

    jalr zero, 0(ra)