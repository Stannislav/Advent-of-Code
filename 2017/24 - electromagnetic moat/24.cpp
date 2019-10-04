#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>


using namespace std;
using wire = struct wire;


/*
	Struct representing the connection between components
	in the tree of all possible bridges.
	Fields:
		in:			the incoming port we're connecting to
		roots:		the components used to for the bridge so far, saved as indices in the vector of components
		leaves:		all possible outgoing connections
		strength:	strength of the bridge so far
*/
struct wire {
	int in = 0;
	vector<int> roots{};
	vector<wire> leaves{};
	int strength = 0;
};


void build_tree(wire &n, vector<pair<int, int>> &comp, vector<wire> &tips)
{
	for(int i = 0; i < comp.size(); ++i) {
		if(comp[i].first == n.in || comp[i].second == n.in) {
			if(find(n.roots.begin(), n.roots.end(), i) == n.roots.end()) {
				wire l;
				l.in = comp[i].first == n.in ? comp[i].second : comp[i].first;
				l.roots = n.roots;
				l.roots.push_back(i);
				l.strength = n.strength + comp[i].first + comp[i].second;
				build_tree(l, comp, tips);
				n.leaves.push_back(l);
			}
		}
	}

	if(n.leaves.size() == 0)
		tips.push_back(n);
}


int main()
{
	vector<pair<int, int>> comp;
	int strength_max, len_max;


	// read
	char slash;
	int n1, n2;

	fstream inf("24_input.txt");
	for(string line; getline(inf, line); ) {
		stringstream ss(line);
		ss >> n1 >> slash >> n2;
		comp.emplace_back(n1, n2);
	}
	inf.close();


	/*
		Build the tree.
		We just brute-force the solution by constructing all possible bridges,
		and saving them as a tree. The wire `root` is the initial port with
		signal "0", and the vector `tips` saves all wires that are at the end
		of a bridge. It's actually only the tips that we need and not the actual tree.
	*/
	wire root;
	vector<wire> tips;

	build_tree(root, comp, tips);


	// part 1
	strength_max = 0;
	for(wire n: tips) {
		if(n.strength > strength_max)
			strength_max = n.strength;
	}

	cout << "part 1: " << strength_max << endl;


	// part 2
	len_max = 0;
	strength_max = 0;
	for(wire n: tips) {
		if(n.roots.size() > len_max) {
			len_max = n.roots.size();
			strength_max = n.strength;
		} else if(n.roots.size() == len_max && n.strength > strength_max) {
			strength_max = n.strength;
		}
	}

	cout << "part 2: " << strength_max << endl;


	return 0;
}