#include<iostream>
#include<fstream>
#include<vector>
#include<string>
#include<sstream>


using namespace std;


int main()
{
	ifstream inF("04_input.txt");

	int count1 = 0;
	int count2 = 0;

	for(string line; getline(inF, line); ) {
		istringstream is(line);
		vector<string> words{istream_iterator<string>(is), {}};
		int valid1 = 1;
		int valid2 = 1;

		for(int i = 0; i < words.size(); i++) {
			for(int j = i+1; j < words.size(); j++){
				// part 1
				if(words[i] == words[j])
					valid1 = 0;

				// part 2
				vector<char> c1(words[i].begin(), words[i].end());
				vector<char> c2(words[j].begin(), words[j].end());
				sort(c1.begin(), c1.end());
				sort(c2.begin(), c2.end());
				if(c1 == c2)
					valid2 = 0;
			}
			if(!valid1 && !valid2)
				break;
		}
		
		count1 += valid1;
		count2 += valid2;
	}

	cout << "part 1: " << count1 << endl;
	cout << "part 2: " << count2 << endl;

	inF.close();
	return 0;
}