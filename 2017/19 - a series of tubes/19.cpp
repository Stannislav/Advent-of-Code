#include <iostream>
#include <fstream>
#include <vector>


using namespace std;


int main()
{
	vector<vector<char>> map;


	fstream inF("19_input.txt");
	for(string line; getline(inF, line); ) {
		map.push_back(vector<char>(line.begin(), line.end()));
	}
	inF.close();


	// part 1
	pair<int, int> pos(0, find(map[0].begin(), map[0].end(), '|') - map[0].begin());
	pair<int, int> dir(1, 0);
	char c;
	int cnt = 0;
	cout << "part 1: ";
	while((c = map[pos.first][pos.second]) != ' ') {
		if(c == '+') {
			if(dir.first == 0) {  // we're going horizontally
				dir.second = 0;
				dir.first = map[pos.first + 1][pos.second] == '|' ? 1 : -1;
			} else {  // we're going vertically and dir.second = 0
				dir.first = 0;
				dir.second = map[pos.first][pos.second + 1] == '-' ? 1 : -1;
			}

		} else if (c != '-' && c != '|') {
			cout << c;
		}
		pos.first += dir.first;
		pos.second += dir.second;
		c = map[pos.first][pos.second];
		cnt += 1;
	}
	cout << endl;
	cout << "part 2: " << cnt << endl;

	return 0;
}