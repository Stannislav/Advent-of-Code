/*
	This is an improved solution based on solutions of other people.
*/

#include <iostream>
#include <fstream>
#include <vector>
#include <cassert>


using namespace std;


struct Component
{
	int a, b;
	bool used = false;
};


istream& operator>>(istream& is, Component &c)
{
	char slash;
	is >> c.a >> slash >> c.b;

	return is;
}


class Solution
{
public:

	Solution(string filename);
	int part1() {return max_strength;}
	int part2() {return max_length_strength;}

private:
	vector<Component> components;
	int max_strength = 0;
	int max_length = 0;
	int max_length_strength = 0;

	void solve(int port = 0, int strength = 0, int length = 0);
};


Solution::Solution(string filename)
{
	ifstream inf(filename);
	components = {istream_iterator<Component>(inf), {}};
	inf.close();

	solve();
}


void Solution::solve(int port, int strength, int length)
{
	max_strength = max(max_strength, strength);
	max_length = max(max_length, length);

	if(length == max_length && strength > max_length_strength)
		max_length_strength = strength;

	for(auto &c: components) {
		if(!c.used && (c.a == port || c.b == port)) {
			c.used = true;
			solve(c.a == port ? c.b : c.a, strength + c.a + c.b, length + 1);
			c.used = false;
		}
	}
}


int main()
{
	Solution solution_test("24_input_test.txt");
	assert(solution_test.part1() == 31);
	assert(solution_test.part2() == 19);

	Solution solution("24_input.txt");
	cout << "part 1: " << solution.part1() << endl;
	cout << "part 2: " << solution.part2() << endl;
	
	return 0;
}