#!/usr/bin/env python

try:
    from Queue import Queue
except ImportError:
    from queue import Queue  # python 2 fall-back

from copy import deepcopy

player, boss = range(2)
magic_missle, drain, shield, poison, recharge = range(5)
spells = [magic_missle, drain, shield, poison, recharge]
spell_names = ["Magic Missile", "Drain", "Shield", "Poison", "Recharge"]
spell_cost = [53, 73, 113, 173, 229]
spell_duration = {shield: 6, poison: 6, recharge: 5}


class State:
    def __init__(self):
        self.stats = {
            player: dict(zip(["hp", "mana", "armor"], [50, 500, 0])),
            boss: dict(zip(["hp", "damage"],
                           [int(line.strip().split()[-1]) for line in open("../input/input_22.txt", 'r').readlines()]))
        }

        self.finished = False
        self.winner = None
        self.effects = dict(zip(spells, [0, 0, 0, 0, 0]))
        self.spell = None
        self.printing = False
        self.mana_spent = 0
        self.hard_mode = False

    def msg(self, txt):
        if (self.printing):
            print(txt)

    def msg_stats(self):
        self.msg("- Player has {} hit points, {} armor, {} mana".format(self.stats[player]["hp"],
                                                                        self.stats[player]["armor"],
                                                                        self.stats[player]["mana"]))
        self.msg("- Boss has {} hit points".format(self.stats[boss]["hp"]))

    def get_possible_spells(self):
        return [s for s in spells if self.effects[s] <= 1 and spell_cost[s] <= self.stats[player]["mana"]]

    def check_finished(self):
        if self.stats[player]["hp"] <= 0:
            self.finished = True
            self.winner = boss
            self.msg("Player is dead, boss wins!")
            return True
        if self.stats[boss]["hp"] <= 0:
            self.finished = True
            self.winner = player
            self.msg("Boss is dead, player wins!")
            return True
        return False

    def apply_effects(self):
        self.stats[player]["armor"] = 0
        for key in self.effects:
            if self.effects[key]:
                if (key == shield and self.effects[key] > 1):
                    self.stats[player]["armor"] = 7
                elif (key == poison):
                    self.stats[boss]["hp"] -= 3
                    self.msg("Poison deals 3 damage.")
                elif (key == recharge):
                    self.stats[player]["mana"] += 101
                    self.msg("Recharge provides 101 mana.")

                self.effects[key] -= 1
                self.msg("{}'s timer is now {}".format(spell_names[key], self.effects[key]))
                if (self.effects[key] == 0):
                    self.msg("{} wears off.".format(spell_names[key]))

        return self.check_finished()

    def player_turn(self):
        # player's turn consists of casting a spell
        if self.spell is None:
            self.msg("- Player runs out of mana.")
            self.finished = True
            self.winner = boss
            return True

        self.msg("Player casts {}.".format(spell_names[self.spell]))
        if self.spell == magic_missle:
            self.stats[boss]["hp"] -= 4
        elif self.spell == drain:
            self.stats[boss]["hp"] -= 2
            self.stats[player]["hp"] += 2
        else:
            self.effects[self.spell] = spell_duration[self.spell]
            if self.spell == shield:
                self.stats[player]["armor"] = 7

        self.stats[player]["mana"] -= spell_cost[self.spell]
        self.mana_spent += spell_cost[self.spell]
        self.spell = None

        return self.check_finished()

    def boss_turn(self):
        dam = self.stats[boss]["damage"] - self.stats[player]["armor"]
        if dam <= 0:
            dam = 1
        self.msg("Boss attacks for {} damage!".format(dam))
        self.stats[player]["hp"] -= dam

        return self.check_finished()

    def step(self):
        self.msg("\n-- Player turn --")
        self.msg_stats()
        # apply hard mode penalty
        if self.hard_mode:
            self.msg("Hard mode enabled, player suffers 1 damage!")
            self.stats[player]["hp"] -= 1
            if self.check_finished():
                return True

        # apply effects
        if self.apply_effects():
            return True

        # player attack
        if self.player_turn():
            return True

        self.msg("\n-- Boss turn --")
        self.msg_stats()
        # apply effects
        if self.apply_effects():
            return True

        # boss attack
        if self.boss_turn():
            return True

        return False


def test1():
    game = State()
    game.stats[player]["hp"] = 10
    game.stats[player]["mana"] = 250
    game.stats[boss]["hp"] = 13
    game.stats[boss]["damage"] = 8
    print(game.stats)

    sp = Queue()
    [sp.put(i) for i in [poison, magic_missle]]

    while not game.finished:
        game.spell = sp.get()
        game.step()


def test2():
    game = State()
    game.stats[player]["hp"] = 10
    game.stats[player]["mana"] = 250
    game.stats[boss]["hp"] = 14
    game.stats[boss]["damage"] = 8
    print(game.stats)

    sp = Queue()
    # [sp.put(i) for i in [poison, magic_missle]]
    [sp.put(i) for i in [recharge, shield, drain, poison, magic_missle]]

    while not game.finished:
        game.spell = sp.get()
        game.step()


min_mana = None


def run_dfs(game):
    global min_mana
    if min_mana is None or game.mana_spent < min_mana:
        if game.finished:
            if game.winner == player:
                min_mana = game.mana_spent
        else:
            for spell in game.get_possible_spells():
                g = deepcopy(game)
                g.spell = spell
                g.step()
                run(g)
                if min_mana is not None and game.mana_spent > min_mana:
                    break


min_mana = None


def run_bfs(game):
    global min_mana

    q = Queue()
    game = State()
    game.hard_mode = True
    q.put(game)
    while not q.empty():
        game = q.get()
        if min_mana is None or game.mana_spent < min_mana:
            if game.finished:
                if game.winner == player:
                    min_mana = game.mana_spent
                    print(min_mana)
            else:
                for spell in game.get_possible_spells():
                    g = deepcopy(game)
                    g.spell = spell
                    g.step()
                    q.put(g)
                    if min_mana is not None and game.mana_spent > min_mana:
                        break


min_mana = None
game = State()
run_dfs(game)
print("part 1: {}".format(min_mana))

min_mana = None
game = State()
game.hard_mode = True
run_dfs(game)
print("part 2: {}".format(min_mana))
