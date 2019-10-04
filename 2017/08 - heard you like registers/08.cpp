#include<iostream>
#include<fstream>
#include<string>
#include<sstream>
#include<map>
#include<vector>


using namespace std;


int main()
{
	using regmap = map<string, int>;  // all registers
	using pair_type = regmap::value_type;  // = pair(string, int)
	using pfunc_cmp =  bool (*)(regmap &regs, string reg, int n);  // pointer to a comparator

	regmap regs;
	string reg1, incdec, iff, reg2, cmp;
	int n1, n2;
	vector<int> maxvalues;

	map<string, pfunc_cmp> fcmp = {
		{"==", [](regmap &regs, string reg, int n) {return regs[reg] == n;} },
		{"!=", [](regmap &regs, string reg, int n) {return regs[reg] != n;} },
		{"<=", [](regmap &regs, string reg, int n) {return regs[reg] <= n;} },
		{">=", [](regmap &regs, string reg, int n) {return regs[reg] >= n;} },
		{"<", [](regmap &regs, string reg, int n) {return regs[reg] < n;} },
		{">", [](regmap &regs, string reg, int n) {return regs[reg] > n;} },
	};
	
	fstream inF("08_input.txt");
	for(string line; getline(inF, line); ) {
		stringstream is(line);
		is >> reg1 >> incdec >> n1 >> iff >> reg2 >> cmp >> n2;

		if(fcmp[cmp](regs, reg2, n2))
			regs[reg1] += (incdec == "inc" ? 1 : -1) * n1;
		
		pair_type max = *max_element(regs.begin(), regs.end(),
			[](const pair_type &p1, const pair_type &p2) {return p1.second < p2.second;});
		maxvalues.push_back(max.second);
	}
	inF.close();

	cout << "part 1: " << maxvalues.back() << endl;
	cout << "part 2: " << *max_element(maxvalues.begin(), maxvalues.end()) << endl;

	return 0;
}