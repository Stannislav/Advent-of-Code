#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>


int main()
{
	using namespace std;
	fstream inF{"02_input.txt"};

	if(!inF) {
		cerr << "Cannot open the file." << endl;
		exit(1);
	}

	string line;
	int checksum1 = 0, checksum2 = 0;

	while(getline(inF, line)) {
		// use the "uniform initialization syntax" to deal with the
		// "most vexing parse". See https://en.wikipedia.org/wiki/Most_vexing_parse
		// See also this: https://softwareengineering.stackexchange.com/questions/133688/is-c11-uniform-initialization-a-replacement-for-the-old-style-syntax
		istringstream is{line};
		vector<int> v{istream_iterator<int>{is},{}};

		checksum1 += *max_element(v.begin(), v.end());
		checksum1 -= *min_element(v.begin(), v.end());

		for(auto i = v.begin(); i < v.end() - 1; i++) {
			for(auto j = i + 1; j < v.end(); j++) {
				if(*i % *j == 0) {
					checksum2 += *i / *j;
					goto afterLoop;
				}
				if(*j % *i == 0) {
					checksum2 += *j / *i;
					goto afterLoop;
				}
			}
		}
		afterLoop: ;
	}

	cout << "part 1: " << checksum1 << endl;
	cout << "part 2: " << checksum2 << endl;

	inF.close();

	return 0;
}
