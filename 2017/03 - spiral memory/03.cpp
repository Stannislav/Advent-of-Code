#include <iostream>
#include <map>


using namespace std;
typedef map<int, map<int, int>> myMap;


void fill_value(myMap &values, int x, int y) {
	// note that if for a given key no value in the map has been set
	// the the default value returned is the one constructed with
	// the default constructor. In this case int() = 0;
	values[x][y] +=
		values[x-1][y] + values[x+1][y] + 
		values[x][y-1] + values[x][y+1] +
		values[x+1][y+1] + values[x-1][y+1] +
		values[x+1][y-1] + values[x-1][y-1];
}

int main()
{
	int target = 325489; // puzzle input
	myMap values; // values for part 2


	int num = 1; // current cell number for part 1

	// if we only think of the edges of the spiral then their
	// coordinates can be found recursively as follows
	// starting with (x, y) = (0, 0):
	// x += 1
	// y += 1
	// x -= 2
	// y -= 2
	// x += 3
	// y += 3
	// etc.
	// so the leg length is increased by one and the sign is inverted
	// at every iteration

	int leglen = 1; // length of the current spiral leg
	int sign = 1; // sign of the increment

	int x = 0, y = 0; // current position
	int part2 = 0; // answer to part 2

	values[0][0] = 1; // initialize the starting point

	// we'll find answer to part 2 before we finish part 1
	while(num != target) {
		// just descend down the spiral as described above
		for(int dx = 0; dx < leglen; dx++) {
				x += sign;
				fill_value(values, x, y);
				if(part2 == 0 && values[x][y] > target)
					part2 = values[x][y];

				num += 1;
				if(num == target)
					goto done;
		}
		for(int dy = 0; dy < leglen; dy++) {
				y += sign;
				fill_value(values, x, y);
				if(part2 == 0 && values[x][y] > target)
					part2 = values[x][y];

				num += 1;
				if(num == target)
					goto done;
		}

		leglen += 1;
		sign = -sign;
	}
	done:

	cout << "part 1: " << target << " is at ";
	cout << "(" << x << ", " << y << ") = " << abs(x) + abs(y) << std::endl;
	cout << "Part 2: " << part2 << endl;


	return 0;
}