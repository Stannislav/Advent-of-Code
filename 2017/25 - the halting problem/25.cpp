#include <iostream>
#include <fstream>
#include <regex>
#include <vector>
#include <map>
#include <cassert>


using namespace std;


int turing_check(map<int, map<int, vector<int>>> turing, int start_state, int iterations)
{
	map<int, int> tape;

	int state = start_state;
	int pos = 0;
	int checksum = 0;
	int val;

	for(int i = 0; i < iterations; ++i) {
		val = tape[pos];
		tape[pos] = turing[state][val][0];
		pos += turing[state][val][1];
		state = turing[state][val][2];
	}

	for(auto kv: tape)
		checksum += kv.second;

	return checksum;
}


void parse_input(string filename, map<int, map<int, vector<int>>> &turing, int &start_state, int &iterations)
{
	regex re1("Begin in state ([A-Z]).");
	regex re2("Perform a diagnostic checksum after ([0-9]+) steps.");
	regex re3(
		"In state ([A-Z]):\n"
		"  If the current value is ([0-1]):\n"
		"    - Write the value ([0-1]).\n"
		"    - Move one slot to the (left|right).\n"
		"    - Continue with state ([A-Z]).\n"
		"  If the current value is ([0-1]):\n"
		"    - Write the value ([0-1]).\n"
		"    - Move one slot to the (left|right).\n"
		"    - Continue with state ([A-Z]).\n"
	);
	smatch m;


	ifstream inf(filename);
	string input;

	// First line
	getline(inf, input);
	regex_match(input, m, re1);
	start_state = m[1].str()[0];

	// Second line
	getline(inf, input);
	regex_match(input, m, re2);
	iterations = stoi(m[1].str());

	// The rest
	input = string(istreambuf_iterator<char>(inf), {});
	while(regex_search(input, m, re3)) {
		turing[m[1].str()[0]] = {
			{stoi(m[2]),
				{
					stoi(m[3]),
					m[4] == "right" ? 1 : -1,
					m[5].str()[0]
				}
			},
			{stoi(m[6].str()),
				{
					stoi(m[7]),
					m[8] == "right" ? 1 : -1,
					m[9].str()[0]
				}
			}
		};
		input = m.suffix();
	}

	inf.close();
}


int main()
{
	// [state, curr_value] -> (write, move, new_state)
	// map<int, map<int, vector<int>>> turing_test = {
	// 	{'A', {{0, {1, 1, 'B'}}, {1, {0, -1, 'B'}}}},
	// 	{'B', {{0, {1, -1, 'A'}}, {1, {1, 1, 'A'}}}}
	// };

	// map<int, map<int, vector<int>>> turing = {
	// 	{'A', {{0, {1, 1, 'B'}}, {1, {0, -1, 'F'}}}},
	// 	{'B', {{0, {0, 1, 'C'}}, {1, {0, 1, 'D'}}}},
	// 	{'C', {{0, {1, -1, 'D'}}, {1, {1, 1, 'E'}}}},
	// 	{'D', {{0, {0, -1, 'E'}}, {1, {0, -1, 'D'}}}},
	// 	{'E', {{0, {0, 1, 'A'}}, {1, {1, 1, 'C'}}}},
	// 	{'F', {{0, {1, -1, 'A'}}, {1, {1, 1, 'A'}}}},
	// };

	map<int, map<int, vector<int>>> turing;
	int start_state, iterations;

	parse_input("25_input_test.txt", turing, start_state, iterations);
	assert(turing_check(turing, start_state, iterations) == 3);

	parse_input("25_input.txt", turing, start_state, iterations);
	cout << "answer: " << turing_check(turing, start_state, iterations) << endl;


	return 0;
}