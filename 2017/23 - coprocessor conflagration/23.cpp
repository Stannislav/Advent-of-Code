#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>


using namespace std;


long getval(string s, map<char, long> &reg)
{
	if('a' <= s[0] && s[0] <= 'z')
		return reg[s[0]];
	return stol(s);
}


int simul1(int a_in = 0)
{
	/*
		This is the most direct translation of the input assembly.
		It's very inefficient and won't return in any reasonable time;
	*/

	int a{a_in}, b{0}, c{0}, d{0}, e{0}, f{0}, g{0}, h{0};

	// lines 1 - 8
	b = c = 84;

	if(a) {
		b = 100000 + 100 * b;
		c = b + 17000;
	}

	// lines 9 - end
	do {
		f = 1;
		d = 2;
		do {
			e = 2;
			while(g) {
				if(d * e == b)
					f = 0;
				e += 1;
				g = e - b;
			}

			d += 1;
			g = d - b;
		} while(g);

		if(f == 0)
			h += 1;
		g = b - c;

		if(g)
			b += 17;
		else
			break;
	} while(g);


	cout << "a = " << a << endl;
	cout << "b = " << b << endl;
	cout << "c = " << c << endl;
	cout << "d = " << d << endl;
	cout << "e = " << e << endl;
	cout << "f = " << f << endl;
	cout << "g = " << g << endl;
	cout << "h = " << h << endl;

	return h;
}

int simul2(int a = 0)
{
	/*
		This is an improved translation of the input assembly code.
		We trade registers for local variables and introduce for-loops.
		The implementation is still as inefficient as in simul1.
	*/

	int b, c;
	int h = 0;


	b = c = 84;

	if(a) {
		b = 100000 + 100 * b;
		c = b + 17000;
	}

	// count the number of composite numbers in the interval [b, c]
	do {
		int f = 1;
		for(int d = 2; d != b; ++d) {
			for(int e = 2; e != b; ++e) {
				if(d * e == b)
					f = 0;
			}
		}

		// if b is not prime then h += 1 
		if(f == 0)
			h += 1;

		if(b != c)
			b += 17;
	} while(b != c);

	return h;
}


int simul3(int a, int b)
{
	/*
		Efficient implementation of the algorithm in the assembly code.
		The code considers all numbers in the range [b, c] in steps of 17,
		and the number of composite numbers found is saved in register h.
		If a = 0 then b = c = 84, otherwise b = 100000 + 8400, and c = b + 17000,
		so that for a != 0 there are 1000 numbers to consider.
	*/

	int c = b;
	int h = 0;

	if(a) {
		b = 100000 + 100 * b;
		c = b + 17000;
	}

	for(int n = b; n <= c; n += 17) {
		for(int div = 2; div * div <= n; ++div) {
			if(n % div == 0) {
				h += 1;
				break;
			}
		}
	}

	return h;
}

int main()
{
	// read input
	vector<string> lines;
	fstream inF("23_input.txt");
	for(string line; getline(inF, line); )
		lines.push_back(line);
	inF.close();


	map<char, long> reg;
	string cmd, x, y;

	// part 1
	int cnt = 0;

	for(int i = 0; i < lines.size(); ++i) {		
		stringstream is(lines[i]);
		is >> cmd >> x >> y;
		if(cmd == "set") {
			reg[x[0]] = getval(y, reg);
		} else if(cmd == "sub") {
			reg[x[0]] -= getval(y, reg);
		} else if(cmd == "mul") {
			reg[x[0]] *= getval(y, reg);
			++cnt;
		} else if(cmd == "jnz") {
			if(getval(x, reg) != 0)
				i += getval(y, reg) - 1;
		}
	}

	cout << "part 1: " << cnt << endl;

	// part 2
	stringstream(lines[0]) >> cmd >> x >> y;
	cout << "part 2: " << simul3(1, stoi(y)) << endl;

	return 0;
}