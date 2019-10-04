#include <iostream>
#include <fstream>
#include <map>


using namespace std;


#define CLEAN		0
#define WEAKENED	1
#define INFECTED	2
#define FLAGGED		3

const int turns[4] = {1, 0, -1, 2};  // {turn left, do nothing, turn right, reverse}
const int dirs[4][2] = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};  // {up, left, down, right}


int solve(map<int, map<int, int>> grid, bool part2 = true)
{
	// Current position, the starting point is in the middle of the grid
	int x = (grid.size() - 1) / 2;
	int y = (grid[0].size() - 1) / 2;
	
	int dir = 0;  // current direction
	int n_infect = 0;  // infection count

	// perform 10,000 bursts for part 1 and 10,000,000 bursts for part 2
	for(int n = 0; n < (part2 ? 10000000 : 10000); ++n) {
		// the direction changes according to the current grid value
		dir = (dir + turns[grid[x][y]] + 4) % 4;

		// update the grid value, for part 1 we only consider 'clean = 0' and 'infected = 2'
		grid[x][y] = (grid[x][y] + (part2 ? 1 : 2)) % 4;

		// count the infected cells
		if(grid[x][y] == INFECTED)
			++n_infect;
		
		// update the position
		x += dirs[dir][0];
		y += dirs[dir][1];
	}

	return n_infect;
}

int main()
{
	map<int, map<int, int>> grid;

	// Read the grid from the input file, '#' = infected, '.' = clean.
	fstream inf("22_input.txt", fstream::in);
	string line;
	for(int x = 0; getline(inf, line); ++x) {
		for(int y = 0; y < line.size(); ++y)
			grid[x][y] = (line[y] == '#' ? INFECTED : CLEAN);
	}
	inf.close();

	cout << "part 1: " << solve(grid, false) << endl;
	cout << "part 2: " << solve(grid, true) << endl;

}