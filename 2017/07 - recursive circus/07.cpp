#include<iostream>
#include<fstream>
#include<sstream>
#include<vector>
#include<map>
#include<set>


using namespace std;


class Disc
{
public:
	Disc() : name{""}, weight{-1} {};
	Disc(string n, int w) : name{n}, weight{w} {};

	string name;
	int weight = -1;
	Disc *parent = 0x0;
	vector<Disc *> children{};

	int get_total_weight();
	bool is_balanced();

};

// A disc is balanced if all their children's total weight is equal
bool Disc::is_balanced()
{
	for(int i = 1; i < children.size(); ++i) {
		if(children[i]->get_total_weight() != children[i-1]->get_total_weight())
			return false;
	}

	return true;
}

// The total weight is own weight + the weight of all children
// If the weight of the children has not been initialized yet, then do it.
int Disc::get_total_weight()
{
	int weight_children = 0;
	for(Disc *c: children)
		weight_children += c->get_total_weight();

	return weight + weight_children;
}


int main()
{
	ifstream inF("07_input.txt");

	string parent;
	int weight;
	vector<string> children_names;
	set<char> del = {'-', '>', ',', '(', ')'};

	// All discs will be saved in this map by their name.
	// The discs by themselves form a bidirectinoal tree and
	// so we don't need this map to navigate around the tree.
	map<string, Disc> discs;

	for(string s; getline(inF, s); ) {
		replace_if(s.begin(), s.end(), [&](char c) {return del.find(c) != del.end();}, ' ');

		stringstream is(s);
		is >> parent >> weight;
		children_names = vector<string>(istream_iterator<string>(is), {});

		discs[parent].name = parent;
		discs[parent].weight = weight;

		for(string child: children_names) {
			discs[child].name = child;
			discs[parent].children.push_back(&discs[child]);
			discs[child].parent = &(discs[parent]);
		}
	}

	inF.close();

	// part 1
	// Start with the last entry read in the input file and
	// descend down the tree until the disc has no parent,
	// this is then the root of the tree
	string root = parent;
	while(discs[root].parent != 0x0)
		root = discs[root].parent->name;

	cout << "part 1: " << root << endl;

	/* part 2
	   We know that either all children will have equal weight or
	   one child's weight will be off. If the latter is the case then
	   we need to find which one is off. Count the number of occurrences
	   of of all weights, and the one with the least count is the one off.
	   Save the weight it should have had for the balance in "target_weight"
	   and let it check it's own children for imbalance. If all it's kids are
	   balanced that this disc's weight is the wrong one. Using "target_weight"
	   we can compute which weight it should have to restore balance.
	*/
	Disc *current = &discs[root];
	int target_weight = 0;
	while(!current->is_balanced()) {
		int w1 = current->children[0]->get_total_weight();
		int w2 = 0;
		int c1 = 1, c2 = 0;

		for(Disc *c: current->children) {
			if(c->get_total_weight() == w1) {
				c1 += 1;
			} else {
				w2 = c->get_total_weight();
				c2 += 1;
			}
		}

		target_weight = c1 > c2 ? w1 : w2;

		for(Disc *c: current->children) {
			if(c->get_total_weight() != target_weight) {
				current = c;
				break;
			}
		}
	}

	cout << "part 2: ";
	cout << current->weight + (target_weight - current->get_total_weight()) << endl;

	// check that with the new weight the whole tree is balanced
	current->weight = current->weight + (target_weight - current->get_total_weight());
	cout << discs[root].is_balanced() << endl;

	return 0;
}
