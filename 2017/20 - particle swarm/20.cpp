#include <iostream>
#include <fstream>
#include <regex>
#include <climits>
#include <vector>


using namespace std;


typedef struct 
{
	int px, py, pz;
	int vx, vy, vz;
	int ax, ay, az;
	bool dead = false;

} particle;


void collide(vector<particle> &particles)
{
	for(auto p1 = particles.begin(); p1 != particles.end(); ++p1) {
		for(auto p2 = p1 + 1; p2 != particles.end(); ) {
			if(p1->px == p2->px && p1->py == p2->py && p1->pz == p2->pz) {
				p1->dead = true;
				p2 = particles.erase(p2);
			} else {
				++p2;
			}
		}
	}
	particles.erase(remove_if(
		particles.begin(),
		particles.end(),
		[](const particle &p) {return p.dead;}
	), particles.end());
}


void evolve(vector<particle> &particles)
{
	for(auto pit = particles.begin(); pit != particles.end(); ++pit) {
		pit->vx += pit->ax;
		pit->vy += pit->ay;
		pit->vz += pit->az;
		pit->px += pit->vx;
		pit->py += pit->vy;
		pit->pz += pit->vz;
	}
}


int main()
{
	vector<particle> particles;

	// read
	regex re(
		"p=<(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)>, "
		"v=<(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)>, "
		"a=<(-?[0-9]+),(-?[0-9]+),(-?[0-9]+)"
	);
	smatch match;
	fstream inF("20_input.txt");
	for(string line; getline(inF, line); ) {
		if(regex_search(line, match, re)) {
			particles.push_back({
				stoi(match[1]), stoi(match[2]), stoi(match[3]),
				stoi(match[4]), stoi(match[5]), stoi(match[6]),
				stoi(match[7]), stoi(match[8]), stoi(match[9])
			});
		}
	}
	inF.close();

	// part 1
	int line_num = 0;
	int a;
	int min_a = INT_MAX;
	int min_line_num;
	for(particle p: particles) {
		a = abs(p.ax) + abs(p.ay) + abs(p.az);
		if(a < min_a) {
			min_a = a;
			min_line_num = line_num;
		}
		++line_num;
	}
	cout << "part 1: " << min_line_num << endl;


	// part 2
	for(int i = 0; i < 1000; ++i) {
		collide(particles);
		evolve(particles);
	}
	cout << "part 2: " << particles.size() << endl;


	return 0;
}