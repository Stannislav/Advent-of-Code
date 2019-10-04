#include<iostream>
#include<fstream>
#include<string>
#include<sstream>
#include<set>


// I looked up some useful tips after writing the first version


using namespace std;


int main()
{
	ifstream inF("04_input.txt");
	int sum1 = 0, sum2 = 0;

	for(string line; getline(inF, line); ) {
		istringstream is(line);

		set<string> s1, s2;
		int check1 = 1, check2 = 1;
		
		for(string word; is >> word; ) {
			// part 1
			if(!s1.insert(word).second)
				check1 = 0;

			// part 2
			sort(word.begin(), word.end());
			if(!s2.insert(word).second)
				check2 = 0;

			if(!check1 && !check2)
				break;
		}

		sum1 += check1;
		sum2 += check2;
	}
	inF.close();

	cout << "part 1: " << sum1 << endl;
	cout << "part 2: " << sum2 << endl;

	return 0;
}