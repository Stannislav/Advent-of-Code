#include<iostream>
#include<fstream>
#include<unordered_map>
#include<vector>


using namespace std;


int main()
{
	unordered_map<string, int> regs;
	string reg1, incdec, iff, reg2, cmp;
	int n1, n2;
	vector<int> maxvalues;

	unordered_map<string, function<bool(int, int)>> fcmp = {
		{"==", equal_to<int>()},
		{"!=", not_equal_to<int>()},
		{">=", greater_equal<int>()},
		{"<=", less_equal<int>()},
		{">", greater<int>()},
		{"<", less<int>()},
	};
	
	fstream inf("08_input.txt");
	while(inf >> reg1 >> incdec >> n1 >> iff >> reg2 >> cmp >> n2) {
		if(fcmp[cmp](regs[reg2], n2))
			regs[reg1] += (incdec == "inc" ? 1 : -1) * n1;
		
		auto max = *max_element(regs.begin(), regs.end(),
			[](auto&& p1, auto&& p2) {return p1.second < p2.second;});
		maxvalues.push_back(max.second);
	}
	inf.close();

	cout << "part 1: " << maxvalues.back() << endl;
	cout << "part 2: " << *max_element(maxvalues.begin(), maxvalues.end()) << endl;

	return 0;
}