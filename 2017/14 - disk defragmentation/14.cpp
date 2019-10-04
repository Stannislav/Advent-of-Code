#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <queue>
#include <numeric>  // for iota(...)


using namespace std;
using matrix = vector<vector<int>>;


vector<int> knot_hash(vector<int> lengths, int size, int rounds)
{
	int skip = 0;
	int total_offset = 0;
	vector<int> list(size);
	iota(list.begin(), list.end(), 0);

	for(int i = 0; i < rounds; ++i) {
		for(int len: lengths) {
			reverse(list.begin(), list.begin() + len);
			rotate(list.begin(), list.begin() + (len + skip) % size, list.end());
			total_offset += len + skip;
			skip++;
		}
	}

	rotate(list.begin(), list.begin() + size - (total_offset % size), list.end());
	
	return list;
}


vector<int> dense_hash(string input)
{
	vector<int> lengths(input.begin(), input.end());
	lengths.insert(lengths.end(), {17, 31, 73, 47, 23});
	vector<int> v = knot_hash(lengths, 256, 64);
	vector<int> dense_hash(16);  // initialize with zeroes, 0 ^ num = num;
	for(int i = 0; i < 16; ++i) {
		for(int block = 0; block < 16; ++block)
			dense_hash[block] = dense_hash[block] ^ v[16 * block + i];
	}

	return dense_hash;
}


void fill_region_at(matrix &grid, matrix &islands, int row, int col, int num)
{
	if(!grid[row][col])
		return;

	queue<pair<int, int>> q({{row, col}});
	
	for(auto c = q.front(); !q.empty(); c = q.front()) {
		q.pop();
		if(islands[c.first][c.second])
			continue;

		islands[c.first][c.second] = num;
		if(c.first > 0 && grid[c.first-1][c.second] && !islands[c.first-1][c.second])
			q.push({c.first-1, c.second});
		if(c.second > 0 && grid[c.first][c.second-1] && !islands[c.first][c.second-1])
			q.push({c.first, c.second-1});
		if(c.first < 127 && grid[c.first+1][c.second] && !islands[c.first+1][c.second])
			q.push({c.first+1, c.second});
		if(c.second < 127 && grid[c.first][c.second+1] && !islands[c.first][c.second+1])
			q.push({c.first, c.second+1});
	}
}

int main()
{
	string input = "hxtvlmkl";
	int n_used{0};
	int n_islands{0};

	matrix islands(128);
	matrix grid(128);

	// create grid and count used fields
	for(int row = 0; row < 128; ++row){
		islands[row] = vector<int>(128);
		string row_input = input + "-" + to_string(row);
		for(auto hash: dense_hash(row_input)) {
			for(int digit = 7; digit >= 0; --digit) {
				grid[row].push_back((hash >> digit) & 1);
				n_used += grid[row].back();
			}
		}
	}

	// find islands
	for(int row = 0; row < 128; ++row) {
		for(int col = 0; col < 128; ++col) {
			if(grid[row][col] && !islands[row][col]) {
				fill_region_at(grid, islands, row, col, ++n_islands);
			}
		}
	}

	cout << "part 1: " << n_used << endl;
	cout << "part 2: " << n_islands << endl;

	return 0;
}