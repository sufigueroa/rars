.global absorb, acid, acid_armor, agility, amnesia, aurora_beam, barrage, barrier, bide, bind, bite, blizzard, body_slam, bone_club, bonemerang, bubble, bubble_beam, clamp, comet_punch, confuse_ray, confusion, constrict, conversion, counter, crabhammer, cut, defense_curl, dig, disable, dizzy_punch, double_kick, double_slap, double_team, double_edge, dragon_rage, dream_eater, drill_peck, earthquake, egg_bomb, ember, explosion, fire_blast, fire_punch, fire_spin, fissure, flamethrower, flash, fly, focus_energy, fury_attack, fury_swipes, glare, growl, growth, guillotine, gust, harden, haze, headbutt, high_jump_kick, horn_attack, horn_drill, hydro_pump, hyper_beam, hyper_fang, hypnosis, ice_beam, ice_punch, jump_kick, karate_chop, kinesis, leech_life, leech_seed, leer, lick, light_screen, lovely_kiss, low_kick, meditate, mega_drain, mega_kick, mega_punch, metronome, mimic, minimize, mirror_move, mist, night_shade, pay_day, peck, petal_dance, pin_missile, poison_gas, poison_powder, poison_sting, pound, psybeam, psychic, psywave, quick_attack, rage, razor_leaf, razor_wind, recover, reflect, rest, roar, rock_slide, rock_throw, rolling_kick, sand_attack, scratch, screech, seismic_toss, self_destruct, sharpen, sing, skull_bash, sky_attack, slam, slash, sleep_powder, sludge, smog, smokescreen, soft_boiled, solar_beam, sonic_boom, spike_cannon, splash, spore, stomp, strength, string_shot, struggle, stun_spore, submission, substitute, super_fang, supersonic, surf, swift, swords_dance, tackle, tail_whip, take_down, teleport, thrash, thunder, thunder_punch, thunder_shock, thunder_wave, thunderbolt, toxic, transform, tri_attack, twineedle, vine_whip, vise_grip, water_gun, waterfall, whirlwind, wing_attack, withdraw, wrap

.data
absorb:
	.word 	1	# ID
			4	# Type Grass
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			20	# Poder
			100	# Precision
			25	# PP

acid:
	.word 	2	# ID
			7	# Type Poison
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			30	# PP

acid_armor:
	.word 	3	# ID
			7	# Type Poison
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

agility:
	.word 	4	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

amnesia:
	.word 	5	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

aurora_beam:
	.word 	6	# ID
			5	# Type Ice
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			20	# PP

barrage:
	.word 	7	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			85	# Precision
			20	# PP

barrier:
	.word 	8	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

bide:
	.word 	9	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

bind:
	.word 	10	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			85	# Precision
			20	# PP

bite:
	.word 	11	# ID
			15	# Type Dark
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			60	# Poder
			100	# Precision
			25	# PP

blizzard:
	.word 	12	# ID
			5	# Type Ice
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			110	# Poder
			70	# Precision
			5	# PP

body_slam:
	.word 	13	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			85	# Poder
			100	# Precision
			15	# PP

bone_club:
	.word 	14	# ID
			11	# Type Ground
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			85	# Precision
			20	# PP

bonemerang:
	.word 	15	# ID
			11	# Type Ground
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			90	# Precision
			10	# PP

bubble:
	.word 	16	# ID
			2	# Type Water
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			30	# PP

bubble_beam:
	.word 	17	# ID
			2	# Type Water
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			20	# PP

clamp:
	.word 	18	# ID
			2	# Type Water
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			35	# Poder
			85	# Precision
			15	# PP

comet_punch:
	.word 	19	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			18	# Poder
			85	# Precision
			15	# PP

confuse_ray:
	.word 	20	# ID
			13	# Type Ghost
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			10	# PP

confusion:
	.word 	21	# ID
			14	# Type Psychic
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			100	# Precision
			25	# PP

constrict:
	.word 	22	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			10	# Poder
			100	# Precision
			35	# PP

conversion:
	.word 	23	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

counter:
	.word 	24	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

crabhammer:
	.word 	25	# ID
			2	# Type Water
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			100	# Poder
			90	# Precision
			10	# PP

cut:
	.word 	26	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			95	# Precision
			30	# PP

defense_curl:
	.word 	27	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			40	# PP

dig:
	.word 	28	# ID
			11	# Type Ground
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			10	# PP

disable:
	.word 	29	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

dizzy_punch:
	.word 	30	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			70	# Poder
			100	# Precision
			10	# PP

double_kick:
	.word 	31	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			30	# Poder
			100	# Precision
			30	# PP

double_slap:
	.word 	32	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			85	# Precision
			10	# PP

double_team:
	.word 	33	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			15	# PP

double_edge:
	.word 	34	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			120	# Poder
			100	# Precision
			15	# PP

dragon_rage:
	.word 	35	# ID
			9	# Type Dragon
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			10	# PP

dream_eater:
	.word 	36	# ID
			14	# Type Psychic
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			100	# Poder
			100	# Precision
			15	# PP

drill_peck:
	.word 	37	# ID
			10	# Type Flying
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			20	# PP

earthquake:
	.word 	38	# ID
			11	# Type Ground
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			100	# Poder
			100	# Precision
			10	# PP

egg_bomb:
	.word 	39	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			100	# Poder
			75	# Precision
			10	# PP

ember:
	.word 	40	# ID
			1	# Type Fire
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			25	# PP

explosion:
	.word 	41	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			250	# Poder
			100	# Precision
			5	# PP

fire_blast:
	.word 	42	# ID
			1	# Type Fire
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			110	# Poder
			85	# Precision
			5	# PP

fire_punch:
	.word 	43	# ID
			1	# Type Fire
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			75	# Poder
			100	# Precision
			15	# PP

fire_spin:
	.word 	44	# ID
			1	# Type Fire
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			35	# Poder
			85	# Precision
			15	# PP

fissure:
	.word 	45	# ID
			11	# Type Ground
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			30	# Precision
			5	# PP

flamethrower:
	.word 	46	# ID
			1	# Type Fire
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			100	# Precision
			15	# PP

flash:
	.word 	47	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

fly:
	.word 	48	# ID
			10	# Type Flying
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			95	# Precision
			15	# PP

focus_energy:
	.word 	49	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

fury_attack:
	.word 	50	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			85	# Precision
			20	# PP

fury_swipes:
	.word 	51	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			18	# Poder
			80	# Precision
			15	# PP

glare:
	.word 	52	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			30	# PP

growl:
	.word 	53	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			40	# PP

growth:
	.word 	54	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

guillotine:
	.word 	55	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			30	# Precision
			5	# PP

gust:
	.word 	56	# ID
			10	# Type Flying
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			35	# PP

harden:
	.word 	57	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

haze:
	.word 	58	# ID
			5	# Type Ice
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

headbutt:
	.word 	59	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			70	# Poder
			100	# Precision
			15	# PP

high_jump_kick:
	.word 	60	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			130	# Poder
			90	# Precision
			10	# PP

horn_attack:
	.word 	61	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			25	# PP

horn_drill:
	.word 	62	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			30	# Precision
			5	# PP

hydro_pump:
	.word 	63	# ID
			2	# Type Water
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			110	# Poder
			80	# Precision
			5	# PP

hyper_beam:
	.word 	64	# ID
			0	# Type Normal
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			150	# Poder
			90	# Precision
			5	# PP

hyper_fang:
	.word 	65	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			90	# Precision
			15	# PP

hypnosis:
	.word 	66	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			60	# Precision
			20	# PP

ice_beam:
	.word 	67	# ID
			5	# Type Ice
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			100	# Precision
			10	# PP

ice_punch:
	.word 	68	# ID
			5	# Type Ice
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			75	# Poder
			100	# Precision
			15	# PP

jump_kick:
	.word 	69	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			100	# Poder
			95	# Precision
			10	# PP

karate_chop:
	.word 	70	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			100	# Precision
			25	# PP

kinesis:
	.word 	71	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			80	# Precision
			15	# PP

leech_life:
	.word 	72	# ID
			8	# Type Bug
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			10	# PP

leech_seed:
	.word 	73	# ID
			4	# Type Grass
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			10	# PP

leer:
	.word 	74	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			30	# PP

lick:
	.word 	75	# ID
			13	# Type Ghost
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			30	# Poder
			100	# Precision
			30	# PP

light_screen:
	.word 	76	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

lovely_kiss:
	.word 	77	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			75	# Precision
			10	# PP

low_kick:
	.word 	78	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

meditate:
	.word 	79	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			40	# PP

mega_drain:
	.word 	80	# ID
			4	# Type Grass
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			15	# PP

mega_kick:
	.word 	81	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			120	# Poder
			75	# Precision
			5	# PP

mega_punch:
	.word 	82	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			85	# Precision
			20	# PP

metronome:
	.word 	83	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

mimic:
	.word 	84	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

minimize:
	.word 	85	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

mirror_move:
	.word 	86	# ID
			10	# Type Flying
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

mist:
	.word 	87	# ID
			5	# Type Ice
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

night_shade:
	.word 	88	# ID
			13	# Type Ghost
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			15	# PP

pay_day:
	.word 	89	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			20	# PP

peck:
	.word 	90	# ID
			10	# Type Flying
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			35	# Poder
			100	# Precision
			35	# PP

petal_dance:
	.word 	91	# ID
			4	# Type Grass
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			120	# Poder
			100	# Precision
			10	# PP

pin_missile:
	.word 	92	# ID
			8	# Type Bug
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			25	# Poder
			95	# Precision
			20	# PP

poison_gas:
	.word 	93	# ID
			7	# Type Poison
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			40	# PP

poison_powder:
	.word 	94	# ID
			7	# Type Poison
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			75	# Precision
			35	# PP

poison_sting:
	.word 	95	# ID
			7	# Type Poison
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			100	# Precision
			35	# PP

pound:
	.word 	96	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			35	# PP

psybeam:
	.word 	97	# ID
			14	# Type Psychic
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			20	# PP

psychic:
	.word 	98	# ID
			14	# Type Psychic
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			100	# Precision
			10	# PP

psywave:
	.word 	99	# ID
			14	# Type Psychic
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			15	# PP

quick_attack:
	.word 	100	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			30	# PP

rage:
	.word 	101	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			20	# Poder
			100	# Precision
			20	# PP

razor_leaf:
	.word 	102	# ID
			4	# Type Grass
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			55	# Poder
			95	# Precision
			25	# PP

razor_wind:
	.word 	103	# ID
			0	# Type Normal
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			10	# PP

recover:
	.word 	104	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			5	# PP

reflect:
	.word 	105	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

rest:
	.word 	106	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			5	# PP

roar:
	.word 	107	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

rock_slide:
	.word 	108	# ID
			12	# Type Rock
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			75	# Poder
			90	# Precision
			10	# PP

rock_throw:
	.word 	109	# ID
			12	# Type Rock
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			90	# Precision
			15	# PP

rolling_kick:
	.word 	110	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			60	# Poder
			85	# Precision
			15	# PP

sand_attack:
	.word 	111	# ID
			11	# Type Ground
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			15	# PP

scratch:
	.word 	112	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			35	# PP

screech:
	.word 	113	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			85	# Precision
			40	# PP

seismic_toss:
	.word 	114	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

self_destruct:
	.word 	115	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			200	# Poder
			100	# Precision
			5	# PP

sharpen:
	.word 	116	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			30	# PP

sing:
	.word 	117	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			55	# Precision
			15	# PP

skull_bash:
	.word 	118	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			130	# Poder
			100	# Precision
			10	# PP

sky_attack:
	.word 	119	# ID
			10	# Type Flying
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			140	# Poder
			90	# Precision
			5	# PP

slam:
	.word 	120	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			75	# Precision
			20	# PP

slash:
	.word 	121	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			70	# Poder
			100	# Precision
			20	# PP

sleep_powder:
	.word 	122	# ID
			4	# Type Grass
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			75	# Precision
			15	# PP

sludge:
	.word 	123	# ID
			7	# Type Poison
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			20	# PP

smog:
	.word 	124	# ID
			7	# Type Poison
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			30	# Poder
			70	# Precision
			20	# PP

smokescreen:
	.word 	125	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			20	# PP

soft_boiled:
	.word 	126	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			5	# PP

solar_beam:
	.word 	127	# ID
			4	# Type Grass
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			120	# Poder
			100	# Precision
			10	# PP

sonic_boom:
	.word 	128	# ID
			0	# Type Normal
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			20	# PP

spike_cannon:
	.word 	129	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			20	# Poder
			100	# Precision
			15	# PP

splash:
	.word 	130	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			40	# PP

spore:
	.word 	131	# ID
			4	# Type Grass
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			15	# PP

stomp:
	.word 	132	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			65	# Poder
			100	# Precision
			20	# PP

strength:
	.word 	133	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			15	# PP

string_shot:
	.word 	134	# ID
			8	# Type Bug
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			95	# Precision
			40	# PP

struggle:
	.word 	135	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			50	# Poder
			0	# Precision
			0	# PP

stun_spore:
	.word 	136	# ID
			4	# Type Grass
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			75	# Precision
			30	# PP

submission:
	.word 	137	# ID
			6	# Type Fighting
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			80	# Precision
			20	# PP

substitute:
	.word 	138	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

super_fang:
	.word 	139	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			10	# PP

supersonic:
	.word 	140	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			55	# Precision
			20	# PP

surf:
	.word 	141	# ID
			2	# Type Water
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			100	# Precision
			15	# PP

swift:
	.word 	142	# ID
			0	# Type Normal
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			60	# Poder
			255	# Precision
			20	# PP

swords_dance:
	.word 	143	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

tackle:
	.word 	144	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			35	# PP

tail_whip:
	.word 	145	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			100	# Precision
			30	# PP

take_down:
	.word 	146	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			85	# Precision
			20	# PP

teleport:
	.word 	147	# ID
			14	# Type Psychic
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

thrash:
	.word 	148	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			120	# Poder
			100	# Precision
			10	# PP

thunder:
	.word 	149	# ID
			3	# Type Electric
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			110	# Poder
			70	# Precision
			10	# PP

thunder_punch:
	.word 	150	# ID
			3	# Type Electric
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			75	# Poder
			100	# Precision
			15	# PP

thunder_shock:
	.word 	151	# ID
			3	# Type Electric
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			30	# PP

thunder_wave:
	.word 	152	# ID
			3	# Type Electric
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			20	# PP

thunderbolt:
	.word 	153	# ID
			3	# Type Electric
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			90	# Poder
			100	# Precision
			15	# PP

toxic:
	.word 	154	# ID
			7	# Type Poison
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			90	# Precision
			10	# PP

transform:
	.word 	155	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			10	# PP

tri_attack:
	.word 	156	# ID
			0	# Type Normal
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			10	# PP

twineedle:
	.word 	157	# ID
			8	# Type Bug
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			25	# Poder
			100	# Precision
			20	# PP

vine_whip:
	.word 	158	# ID
			4	# Type Grass
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			45	# Poder
			100	# Precision
			25	# PP

vise_grip:
	.word 	159	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			55	# Poder
			100	# Precision
			30	# PP

water_gun:
	.word 	160	# ID
			2	# Type Water
			1	# Categoria (0_Status, 1_Especial, 2_Fisico)
			40	# Poder
			100	# Precision
			25	# PP

waterfall:
	.word 	161	# ID
			2	# Type Water
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			80	# Poder
			100	# Precision
			15	# PP

whirlwind:
	.word 	162	# ID
			0	# Type Normal
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			20	# PP

wing_attack:
	.word 	163	# ID
			10	# Type Flying
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			60	# Poder
			100	# Precision
			35	# PP

withdraw:
	.word 	164	# ID
			2	# Type Water
			0	# Categoria (0_Status, 1_Especial, 2_Fisico)
			0	# Poder
			0	# Precision
			40	# PP

wrap:
	.word 	165	# ID
			0	# Type Normal
			2	# Categoria (0_Status, 1_Especial, 2_Fisico)
			15	# Poder
			90	# Precision
			20	# PP

