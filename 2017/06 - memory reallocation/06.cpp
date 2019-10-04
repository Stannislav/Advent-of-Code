#include<iostream>
#include<fstream>
#include<vector>
#include<set>


using namespace std;

auto solve(vector<int> &banks) {
	int len = banks.size();
	auto beg = banks.begin();
	auto end = banks.end();
	set<vector <int>> seen;

	int iterations = 0;
	auto max_el = max_element(beg, end);
	int n, d;
	while(seen.insert(banks).second) {
		iterations++;

		max_el = max_element(beg, end);
		n = exchange(*max_el, 0);
		d = distance(beg, max_el) + 1;
		for(int i = d; i < n + d; i++)
			banks[i % len] += 1;
	}

	return iterations;
}

int main()
{
	ifstream inF("06_input.txt");	
	vector<int> banks(istream_iterator<int>(inF), {});
	inF.close();

	// banks = {0, 2, 7, 0};

	cout << solve(banks) << endl;
	cout << solve(banks) << endl;

	return 0;
}