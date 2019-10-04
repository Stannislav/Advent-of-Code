#include<iostream>
#include<fstream>
#include<vector>


using namespace std;


int solve(const vector<int> &cmd_orig, bool part2)
{
	vector<int> cmd(cmd_orig);
	int len = cmd.size();
	int steps = 0;
	int pos = 0;
	int old_pos;

	while(pos < len) {
		steps++;
		old_pos = pos;
		pos += cmd[pos];
		cmd[old_pos] += !part2 || cmd[old_pos] < 3 ? 1 : -1;
	}

	return steps;
}

int main()
{
	ifstream inF("05_input.txt");
	vector<int> cmd{istream_iterator<int>(inF), {}};
	inF.close();

	cout << "part 1: " << solve(cmd, false) << endl;
	cout << "part 2: " << solve(cmd, true) << endl;

	return 0;
}