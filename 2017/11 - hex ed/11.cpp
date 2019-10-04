#include<iostream>
#include<fstream>
#include<string>
#include<sstream>
#include<map>
#include<vector>


using namespace std;


/* Since the order of going in different directions commutes,
   opposite directions cancel each other, so we only need to
   record three directions that are 60 degrees relative to
   each other, e.g. {n, sw, se}. If "max" is the biggest among
   these numbers and "min" the smallest, then the total distance "d"
   is given by
   d = max - min
*/
int main()
{
	fstream inF("11_input.txt");
	string input(istreambuf_iterator<char>(inF), {});
	inF.close();

	replace(input.begin(), input.end(), ',', '\n');

	vector<int> d = {0, 0, 0};  // {n, sw, se}
	map<string, vector<int>> trans = {
		{"n", {1, 0, 0}},
		{"s", {-1, 0, 0}},
		{"sw", {0, 1, 0}},
		{"ne", {0, -1, 0}},
		{"se", {0, 0, 1}},
		{"nw", {0, 0, -1}}
	};
	int dist, maxdist{0};

	for(auto [is, dir] = make_pair(stringstream(input), string()); getline(is, dir); ) {
		for(int i: {0, 1, 2})
			d[i] += trans[dir][i];

		dist = *max_element(d.begin(), d.end()) - *min_element(d.begin(), d.end());
		if(dist > maxdist)
			maxdist = dist;
	}

	cout << "part 1: " << dist << endl;
	cout << "part 2: " << maxdist << endl;

	return 0;
}