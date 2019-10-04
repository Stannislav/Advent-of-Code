#include <iostream>
#include <fstream>
#include <vector>


using namespace std;


// model solution from other users
int main()
{
	vector<pair<int, int>> firewall;
	string _;
	int pos, depth;

	fstream inF("13_input.txt");
	while(inF >> pos >> _ >> depth)
		firewall.emplace_back(pos, depth);
	inF.close();

	// part 1
	int sev{0};
	for(auto [pos, depth]: firewall)
		if(pos % (2 * (depth - 1)) == 0)
			sev += pos * depth;
	cout << "part 1: " << sev << endl;

	// part 2
	int delay{0};
	while(any_of(firewall.begin(), firewall.end(), [&](auto &p) {
		return (p.first + delay) % (2 * (p.second - 1)) == 0;
	})) ++delay;
	cout << "part 2: " << delay << endl;

	return 0;
}