#include <iostream>
#include <fstream>
#include <vector>


using namespace std;


int main()
{
	vector<int> numbers;
	ifstream inFile;

	inFile.open("./01_input.txt");

	if(!inFile) {
		cerr << "Unable to open file." << endl;
		exit(1);
	}

	char c;
	while (inFile >> c)
		numbers.push_back(c - '0');

	// part 1
	int sum = 0;
	for(int i = 0; i < numbers.size() - 1; i++) {
		if(numbers[i] == numbers[i + 1])
			sum += numbers[i];
	}
	if(numbers.back() == numbers[0])
		sum += numbers[0];

	cout << "part 1: " << sum << endl;

	// part 2
	sum = 0;
	int l = numbers.size() / 2;
	for(int i = 0; i < l; i++) {
		if(numbers[i] == numbers[i + l])
			sum += numbers[i];
	}
	cout << "part 2: " << 2 * sum << endl;


	inFile.close();
	return 0;
}
