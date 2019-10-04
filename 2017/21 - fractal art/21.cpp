#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <regex>


using namespace std;
using matrix = vector<vector<char>>;


matrix rot(matrix &m)
{
	/*
		Rotate a matrix clockwise;
	*/

	matrix tmp = m;
	for(int x = 0; x < m.size(); ++x) {
		for(int y = 0; y < m[x].size(); ++y)
			tmp[x][y] = m[m.size() - 1 - y][x];
	}
	m = tmp;
	return m;
}


matrix flip(matrix &m)
{
	/*
		Flip a matrix vertically.
	*/

	reverse(m.begin(), m.end());

	return m;
}

void parse_rule(string line, map<matrix, matrix> &rules)
{
	/*
		Parses a line in the input file to a replacement rule.
		First we split the line on the string "=>" into two parts,
		the in string and the out string. Next convert these strings
		into matrices. Finally, rotate and flip the in-matrix in
		all possible ways and add the corresponding rule to the map.
	*/

	string in = line.substr(0, line.find(" => "));
	string out = line.substr(line.find(" => ") + 4, line.size());
	matrix m_in;
	matrix m_out;
	stringstream ss;

	ss = stringstream(in);
	for(string sub; getline(ss, sub, '/'); )
		m_in.push_back(vector<char>(sub.begin(), sub.end()));
	ss = stringstream(out);
	for(string sub; getline(ss, sub, '/'); )
		m_out.push_back(vector<char>(sub.begin(), sub.end()));

	for(int i = 0; i < 4; ++i) {
		rules[m_in] = m_out;
		rot(m_in);
	}
	flip(m_in);
	for(int i = 0; i < 4; ++i) {
		rules[m_in] = m_out;
		rot(m_in);
	}
	
}


matrix subm(const matrix &m, int px, int py, int dx, int dy)
{
	/*
		Returns a submatrix of `m` located at (px, py)
		and with dimensions (dx, dy).
	*/

	matrix ret(dx, vector<char>(dy));

	for(int x = 0; x < dx; ++x) {
		for(int y = 0; y < dy; ++y) {
			ret[x][y] = m[px + x][py + y];
		}
	}

	return ret;
}


matrix grow(matrix &pix, map<matrix, matrix> &rules, int bs)
{
	/*
		Performs one evolution step on `pix`
		by splitting `pix into block of size `bs`
		and replacing each block by a new one
		according to `rules`.
	*/

	matrix newpix(pix.size() * (bs + 1) / bs, vector<char>(pix.size() * (bs + 1) / bs));

	for(int x = 0; x < pix.size() / bs; ++x) {
		for(int y = 0; y < pix.size() / bs; ++y) {
			matrix b = rules[subm(pix, bs * x, bs * y, bs, bs)];
			for(int dx = 0; dx < bs + 1; ++dx) {
				for(int dy = 0; dy < bs + 1; ++dy)
					newpix[(bs + 1) * x + dx][(bs + 1) * y + dy] = b[dx][dy];
			}
		}
	}

	return newpix;
}

void evolve(matrix &pix, map<matrix, matrix> &rules, int n)
{
	/*
		Evolves `pix` for `n` steps.
	*/

	for(int i = 0; i < n; ++i) {
		if(pix.size() % 2 == 0)
			pix = grow(pix, rules, 2);
		else
			pix = grow(pix, rules, 3);
	}
}


int solve(matrix &start, map<matrix, matrix> rules, int n)
{
	/* 
		Evolves `start` for `n` steps and returns the number
		of non-zero elements in the final product.
	*/

	matrix pix = start;
	int cnt = 0;

	evolve(pix, rules, n);

	for(auto row: pix)
		cnt += count(row.begin(), row.end(), '#');

	return cnt;
}


int main()
{
	// read
	map<matrix, matrix> rules;

	fstream inF("21_input.txt");
	for(string line; getline(inF, line); )
		parse_rule(line, rules);
	inF.close();
	

	// solve
	matrix start = {
		{'.', '#', '.'},
		{'.', '.', '#'},
		{'#', '#', '#'}
	};

	cout << "part 1: " << solve(start, rules, 5) << endl;
	cout << "part 2: " << solve(start, rules, 18) << endl;

	return 0;
}