.global bulbasaur, charmander, squirtle

.data
bulbasaur:
	.word	1	# ID
			0	# Tipo 		0: Planta	1: Fuego	2: Agua
			5	# Nivel
			0	# Exp
			0	# Estado	0: Saludable	1: Envenenado	2: Dormido	3: Desmayado
			21	# HP
			21	# HP Total
			11	# Ataque
			8	# Defensa
			13	# Ataque Especial
			13	# Defensa Especial
			11	# Velocidad
	
charmander:
	.word	4	# ID
			1	# Tipo 		0: Planta	1: Fuego	2: Agua
			5	# Nivel
			0	# Exp
			0	# Estado	0: Saludable	1: Envenenado	2: Dormido	3: Desmayado
			20	# HP
			20	# HP Total
			12	# Ataque
			7	# Defensa
			10	# Ataque Especial
			11	# Defensa Especial
			13	# Velocidad

squirtle:
	.word	7	# ID
			2	# Tipo 		0: Planta	1: Fuego	2: Agua
			5	# Nivel
			0	# Exp
			0	# Estado	0: Saludable	1: Envenenado	2: Dormido	3: Desmayado
			20	# HP
			20	# HP Total
			11	# Ataque
			10	# Defensa
			11	# Ataque Especial
			12	# Defensa Especial
			10	# Velocidad
	