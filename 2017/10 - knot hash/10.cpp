#include<iostream>
#include<fstream>
#include<vector>
#include<numeric>
#include<string>
#include<sstream>


using namespace std;


// instead of cycling the current position round the back of a vector
// we'll cycle the elements of the vector so that the current position
// is always at zero.
// take the first n elements of v and append them at the end
void cycle(vector<int> &v, int n)
{
	v.insert(v.end(),v.begin(), v.begin() + n);
	v.erase(v.begin(), v.begin() + n);
}

vector<int> knot_hash(vector<int> lengths, int size, int rounds)
{
	int skip = 0;
	int total_offset = 0;
	vector<int> list(size);
	iota(list.begin(), list.end(), 0);  // populate list with numbers 0..(size-1)

	for(int i = 0; i < rounds; ++i) {
		for(int len: lengths) {
			reverse(list.begin(), list.begin() + len);
			cycle(list, (len + skip) % size);
			total_offset += len + skip;
			skip++;
		}
	}

	// undo the all the cycles done in the loop so that the vector is in the right order
	cycle(list, size - (total_offset % size));
	
	return list;
}

int main()
{
	// Since we'll need to parse the input file twice in different ways
	// we first read it into a string and remove trailing new lines.
	fstream inF("10_input.txt");
	string input(istreambuf_iterator<char>(inF), {});
	while(input.back() == '\n')
		input.pop_back();
	inF.close();
	
	// PART 1
	// parse the string as comma-separated integers
	vector<int> lengths1;
	stringstream is(input);
	for(int i; is >> i; ) {
		lengths1.push_back(i);
		if(is.peek() == ',')
			is.ignore();
	}

	// The answer is just the knot hash with one round
	vector<int> v = knot_hash(lengths1, 256, 1);
	cout << "part 1: " << v[0] * v[1] << endl;

	// PART 2
	// Now we parse the input as a set of chars and append the default lengths at the end
	vector<int> lengths2(input.begin(), input.end());
	lengths2.insert(lengths2.end(), {17, 31, 73, 47, 23});

	// The hash is the knot hash from part 1 but with 64 rounds
	v = knot_hash(lengths2, 256, 64);

	// Compute the dense hash by xoring together blocks of 16
	vector<int> dense_hash(16);  // initialize with zeroes, 0 ^ num = num;
	for(int i = 0; i < 16; ++i) {
		for(int block = 0; block < 16; ++block)
			dense_hash[block] = dense_hash[block] ^ v[16 * block + i];
	}
	cout << "part 2: ";
	for(auto i: dense_hash)
		cout << hex << i;
	cout << endl;

	return 0;
}