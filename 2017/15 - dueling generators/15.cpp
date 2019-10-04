#include <iostream>


using namespace std;


int main()
{
	int valA = 634;
	int valB = 301;

	long fA = 16807;
	long fB = 48271;
	long rem = 2147483647;
	long comp = (1 << 16) - 1;

	int count = 0;
	for(int i = 0; i < 40000000; ++i) {
		valA = (valA * fA) % rem;
		valB = (valB * fB) % rem;

		if((valA & comp) == (valB & comp))
			count++;
	}
	cout << "part 1: " << count << endl;

	// part 2
	valA = 634;
	valB = 301;
	count = 0;
	for(int i = 0; i < 5000000; ++i) {
		valA = (valA * fA) % rem;
		while(valA % 4)
			valA = (valA * fA) % rem;
		valB = (valB * fB) % rem;
		while(valB % 8)
			valB = (valB * fB) % rem;

		if((valA & comp) == (valB & comp))
			count++;
	}
	cout << "part 2: " << count << endl;

	return 0;
}