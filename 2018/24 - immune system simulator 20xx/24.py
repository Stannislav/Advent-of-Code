#!/usr/bin/env python3

import re
from itertools import count


faction = {"immune": 0, "infection": 1}

oldprint = print
def print(*args, **kwargs):
    return


class Group:
    def __init__(self, name, n_units, hp, damage, damage_type, initiative, immune=[], weak=[]):
        self.name = name
        self.n_units = n_units
        self.hp = hp
        self.damage = damage
        self.damage_type = damage_type
        self.initiative = initiative
        self.weak = weak
        self.immune = immune

        self.target = None
        self.target_damage = 0


    @property
    def alive(self): return self.n_units > 0

    @property
    def eff_power(self): return self.n_units * self.damage


    def estimate_damage(self, other):
        dam = self.eff_power
        if self.damage_type in other.weak:
            dam *= 2
        elif self.damage_type in other.immune:
            dam = 0

        return dam


    def select_target(self, other_army):
        for other in other_army:
            if self.target is None or (other.alive and self.estimate_damage(other) > self.target_damage):
                self.target = other
                self.target_damage = self.estimate_damage(other)


    def __lt__(self, other):
        if self.eff_power == other.eff_power:
            return self.initiative > other.initiative
        return self.eff_power > self.eff_power


    def attack(self):
        if not self.target:
            return
        self.target_damage = self.estimate_damage(self.target)
        kill = min(self.target_damage // self.target.hp, self.target.n_units)
        print(f"{self.name} attacks {self.target.name}, killing {kill} units")
        self.target.n_units -= kill
        self.target = None
        self.target_damage = 0


def parse_unit(s, name):
    m = re.match("(\d+) units each with (\d+) hit points (\(.*\) )*with an attack that does (\d+) (\w+) damage at initiative (\d+)", s)
    if m is None:
        return None

    n_units, hp, weak_immune, damage, damage_type, initiative = m.groups()
    n_units, hp, damage, initiative = map(int, (n_units, hp, damage, initiative))

    # Parse the weak/immune block in parentheses
    weak, immune = [], []
    if weak_immune:
        weak_immune = weak_immune[1:-2]  # remove parentheses
        if ';' in weak_immune:
            parse = weak_immune.split('; ')
        else:
            parse = (weak_immune, )
        for entry in parse:
            m = re.match("(immune|weak) to (.*)", entry)
            if m.lastindex != 2:  # want to match exactly two entries
                continue
            keyword, types = m.groups()
            if keyword == "immune":
                immune = types.split(', ')
            elif keyword == "weak":
                weak = types.split(', ')

    return Group(name, n_units, hp, damage, damage_type, initiative, weak=weak, immune=immune)


def parse_input(inpt):
    army_immune = []
    army_infection = []

    lines = iter(inpt.split('\n'))
    n = 1
    assert next(lines) == "Immune System:"
    while True:
        u = parse_unit(next(lines), name=f"Immune group {n}")
        if not u:
            break
        army_immune.append(u)
        n += 1

    assert next(lines) == "Infection:"
    n = 1
    while True:
        u = parse_unit(next(lines), name=f"Infection group {n}")
        if not u:
            break
        army_infection.append(u)
        n += 1

    return army_immune, army_infection


def play(army_immune, army_infection):
    for round in count(start=1):
        print(f"=== ROUND {round} ===")
        # Print groups
        print("Immune System:")
        for n, g in enumerate(army_immune):
            if g.alive:
                print(f"Group {n+1} contains {g.n_units} units")
        print("Infection:")
        for n, g in enumerate(army_infection):
            if g.alive:
                print(f"Group {n+1} contains {g.n_units} units")
        print()

        if not any([g.alive for g in army_immune]) or not any([g.alive for g in army_infection]):
            break

        # Target selection
        other_army = [group for group in army_immune if group.alive]
        for n, g in enumerate(sorted(army_infection)):
            if not g.alive:
                continue
            for k, other in enumerate(other_army):
                print(f"Infection group {n+1} would deal defending group {k+1} {g.estimate_damage(other)} damage")
                damage = g.estimate_damage(other)
                if not damage:
                    continue
                if g.target is None or damage > g.target_damage:
                    g.target = other
                    g.target_damage = damage
                elif damage == g.target_damage:
                    if other.eff_power > g.target.eff_power:
                        g.target = other
                        g.target_damage = damage
                    elif other.eff_power == g.target.eff_power:
                        if other.initiative > g.target.initiative:
                            g.target = other
                            g.target_damage = damage
            other_army = [group for group in other_army if group is not g.target]
            if not other_army:
                break

        other_army = [group for group in army_infection if group.alive]
        for n, g in enumerate(sorted(army_immune)):
            if not g.alive:
                continue
            for k, other in enumerate(other_army):
                print(f"Immune group {n+1} would deal defending group {k+1} {g.estimate_damage(other)} damage")
                damage = g.estimate_damage(other)
                if not damage:
                    continue
                if g.target is None or damage > g.target_damage:
                    g.target = other
                    g.target_damage = damage
                elif damage == g.target_damage:
                    if other.eff_power > g.target.eff_power:
                        g.target = other
                        g.target_damage = damage
                    elif other.eff_power == g.target.eff_power:
                        if other.initiative > g.target.initiative:
                            g.target = other
                            g.target_damage = damage
            other_army = [group for group in other_army if group is not g.target]
            if not other_army:
                break
        print()

        # Attacking
        for g in sorted(army_immune + army_infection, key=lambda g: -g.initiative):
            if g.alive:
                g.attack()
        print()

    print("Game finished.\n")


test = """\
Immune System:
17 units each with 5390 hit points (weak to radiation, bludgeoning) with an attack that does 4507 fire damage at initiative 2
989 units each with 1274 hit points (immune to fire; weak to bludgeoning, slashing) with an attack that does 25 slashing damage at initiative 3

Infection:
801 units each with 4706 hit points (weak to radiation) with an attack that does 116 bludgeoning damage at initiative 1
4485 units each with 2961 hit points (immune to radiation; weak to fire, cold) with an attack that does 12 slashing damage at initiative 4
"""

def main():
    with open("./24_input.txt", 'r') as f:
        army_immune, army_infection = parse_input(f.read())
    # army_immune, army_infection = parse_input(test)
    play(army_immune, army_infection)

    score = sum(g.n_units for g in army_immune + army_infection if g.alive)
    print = oldprint
    print("Score:", score)
    # 9878


if __name__ == '__main__':
    main()
