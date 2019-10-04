#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <queue>


using namespace std;


long getval(string s, map<char, long> &reg)
{
	if('a' <= s[0] && s[0] <= 'z')
		return reg[s[0]];
	return stol(s);
}


long part1(vector<string> &lines)
{
	map<char, long> reg;
	long snd;
	string cmd, x, y;

	for(int i = 0; i < lines.size(); ++i) {		
		stringstream is(lines[i]);
		is >> cmd >> x >> y;
		if(cmd == "snd") {
			snd = reg[x[0]];
		} else if(cmd == "set") {
			reg[x[0]] = getval(y, reg);
		} else if(cmd == "add") {
			reg[x[0]] += getval(y, reg);
		} else if(cmd == "mul") {
			reg[x[0]] *= getval(y, reg);
		} else if(cmd == "mod") {
			reg[x[0]] %= getval(y, reg);
		} else if(cmd == "rcv") {
			if(getval(x, reg))
				break;
		} else if(cmd == "jgz") {
			if(getval(x, reg) > 0)
				i += getval(y, reg) - 1;
		}
	}
	
	return snd;
}


bool exec_step(vector<string> &lines, int &pt, map<char, long> &reg, queue<long> &snd, queue<long> &rcv, int &cnt)
{
	string cmd, x, y;
	stringstream is(lines[pt]);
	is >> cmd >> x >> y;

	if(cmd == "set") {
		reg[x[0]] = getval(y, reg);
	} else if(cmd == "add") {
		reg[x[0]] += getval(y, reg);
	} else if(cmd == "mul") {
		reg[x[0]] *= getval(y, reg);
	} else if(cmd == "mod") {
		reg[x[0]] %= getval(y, reg);
	} else if(cmd == "jgz") {
		if(getval(x, reg) > 0)
			pt += getval(y, reg) - 1;
	} else if(cmd == "snd") {
		snd.push(getval(x, reg));
		++cnt;
	} else if(cmd == "rcv") {
		if(rcv.empty())
			return true;
		else {
			reg[x[0]] = rcv.front();
			rcv.pop();
		}
	}

	++pt;
	return false;
}


int main()
{
	// read input
	vector<string> lines;
	fstream inF("18_input.txt");
	for(string line; getline(inF, line); )
		lines.push_back(line);
	inF.close();


	// part 1
	cout << "part 1: " << part1(lines) << endl;


	// part 2
	map<char, long> reg0, reg1;
	queue<long> q0, q1;
	int pt0 = 0, pt1 = 0;
	bool wait0 = false, wait1 = false;
	int cnt0 = 0, cnt1 = 0;

	reg1['p'] = 1;

	while(!(wait0 && wait1)) {
		wait0 = exec_step(lines, pt0, reg0, q0, q1, cnt0);
		wait1 = exec_step(lines, pt1, reg1, q1, q0, cnt1);
	}

	cout << "part 2: " << cnt1 << endl;

	return 0;
}