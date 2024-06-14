import re

types = {"Normal": 0, 
         "Fire": 1,
         "Water": 2,
         "Electric": 3,
         "Grass": 4,
         "Ice": 5,
         "Fighting": 6,
         "Poison": 7,
         "Bug": 8,
         "Dragon": 9,
         "Flying": 10,
         "Ground": 11,
         "Rock": 12,
         "Ghost": 13,
         "Psychic": 14,
         "Dark": 15,
         }

def parse_rows(lines):
    lines = [line.strip() for line in lines]
    lines = "".join(lines)
    matches = re.findall(r"<tr>(.*?)</tr>", lines)
    return matches

def parse_row(row):
    if "special" in row:
        category = 1
    elif "physical" in row:
        category = 2
    elif "status" in row:
        category = 0
    mat = re.findall(r">([.\w].*?)<", row)
    mat.append(category)
    return mat

def moves_RISCV_data(rows):
    imports = ".global "
    text = ".data\n"
    for i, row in enumerate(rows):
        data, name = move_RISCV(row, i+1)
        imports += name + ", "
        text += data + "\n"

    imports = imports[:-2]
    whole_text = imports + "\n\n" + text
    with open("pokemon_moves.asm", "w") as file:
        file.write(whole_text)

def move_RISCV(row, id):
    name = row[0].lower().replace(" ", "_")
    text = ""
    text += name + ":\n\t.word "
    text += "\t" + str(id) + "\t# ID\n"
    text += "\t\t\t" + str(types[row[1]]) + f"\t# Type {row[1]}\n"
    text += "\t\t\t" + str(row[-1]) + "\t# Categoria (0-Status, 1-Especial, 2-Fisico)\n"
    text += "\t\t\t" + row[2] + "\t# Poder\n"
    text += "\t\t\t" + row[3] + "\t# Precision\n"
    text += "\t\t\t" + row[4] + "\t# PP\n"
    return text, name


with open("Pokémon moves from Generation 1 _ Pokémon Database.html", "r") as file:
    lines = file.readlines()


moves = {}
rows = parse_rows(lines)
rows = [parse_row(row) for row in rows]
# moves_RISCV_data(rows)

with open("test.txt", "w") as file:
    for i, row in enumerate(rows):
        file.writelines(f'pokemonMoves.put({i+1}, "{row[0]}");\n')