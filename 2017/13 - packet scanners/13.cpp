#include <iostream>
#include <fstream>
#include <vector>
#include <regex>


using namespace std;

/* the main idea is to save layers as cycles and the scanner position in
   a time-delayed fashion so that it reflects the configuration at the time
   when the packet gets there.
*/
int main()
{
	/* "firewall":  firewall layout as is
	   "lengths":   lengths of the cycles for each layers
	   "positions": the time-delayed position on each layer
	*/
	vector<pair<int, int>> firewall;
	vector<int> lengths;
	vector<int> positions;

	regex re("([0-9]*): ([0-9]*)");
	smatch match;
	int pos, depth;

	fstream inF("13_input.txt");
	for(string line; getline(inF, line); ) {
		if(regex_search(line, match, re)) {
			pos = stoi(match[1]);
			depth = stoi(match[2]);
			firewall.push_back({pos, depth});
			lengths.push_back(2 * depth - 2); // overcounting the first and the last element
			positions.push_back(pos % lengths.back());
		}
	}
	inF.close();

	// part 1
	int severity = 0;
	for(int i = 0; i < positions.size(); ++i) {
		if(positions[i] == 0)
			severity += firewall[i].first * firewall[i].second;
	}
	cout << "part 1: " << severity << endl;

	// part 2
	int delay = -1;
	for(bool caught = true; caught; ++delay) {
		caught = false;
		for(int i = 0; i < positions.size(); ++i) {
			if(!positions[i])
				caught = true;
			positions[i] = (positions[i] + 1) % lengths[i];
		}
	}
	cout << "part 2: " << delay << endl;

	return 0;
}