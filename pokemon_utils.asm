.global _getVector
.text
_getVector:
add t0, zero, a0
add a0, zero, zero
lw t1, 0(t0)
slli t1, t1, 8
add a0, a0, t1
lw t1, 16(t0)
add a0, a0, t1
jalr zero, 0(ra)
