#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>


using namespace std;


string transform(vector<int> &trans_pos, vector<int> &trans_char, int iterations)
{
	vector<int> programs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	vector<int> chars = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	string ret = "";
	
	for(int i = 0; i < iterations; ++i) {
		vector<char> tmp(16);
		for(int j = 0; j < 16; ++j)
			tmp[j] = programs[trans_pos[j]];
		copy(tmp.begin(), tmp.end(), programs.begin());
		for(int j = 0; j < 16; ++j)
			tmp[j] = chars[trans_char[j]];
		copy(tmp.begin(), tmp.end(), chars.begin());
	}

	for(int i = 0; i < 16; ++i)
		ret += char(chars[programs[i]] + 'a');
	
	return ret;
}


int main()
{
	fstream inF("16_input.txt");

	vector<int> trans_pos = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	vector<int> trans_char = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

	char c, p1, p2;
	int n1, n2;
	stringstream is;

	for(string s; getline(inF, s, ','); ) {
		c = s[0];
		s.erase(0, 1);
		is = stringstream(s);
		switch(c) {
			case 's':
				is >> n1;
				rotate(trans_pos.begin(), trans_pos.begin() + (16 - n1), trans_pos.end());
				break;

			case 'x':
				is >> n1 >> c >> n2;
				iter_swap(trans_pos.begin() + n1, trans_pos.begin() + n2);
				break;
			case 'p':
				is >> p1 >> c >> p2;
				iter_swap(
					find(trans_char.begin(), trans_char.end(), p1 - 'a'),
					find(trans_char.begin(), trans_char.end(), p2 - 'a')
				);
				break;
		}
	}
	inF.close();

	cout << "part 1: " << transform(trans_pos, trans_char, 1) << endl;
	cout << "part 2: " << transform(trans_pos, trans_char, 1000000000) << endl;
}