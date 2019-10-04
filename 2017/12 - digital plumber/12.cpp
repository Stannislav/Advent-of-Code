#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <set>
#include <map>
#include <queue>


using namespace std;


typedef struct {
	bool visited = false;
	set<int> links{};
} node;


int get_group_size(map<int, node> &graph, int root)
{
	// use BSF to identify all nodes connected to root
	queue<int> q({root});
	int count = 0;

	for(int node = q.front(); !q.empty(); node = q.front()) {
		q.pop();
		if(graph[node].visited)  // this is the case if a node was in the queue more than once
			continue;
		count++;
		for(int next: graph[node].links) {
			if(!graph[next].visited)
				q.push(next);
		}
		graph[node].visited = true;
	}
	
	return count;
}


int main()
{
	map<int, node> graph;
	int n, link;
	string dummy;

	ifstream inF("12_input.txt");
	for(string line; getline(inF, line); ) {
		stringstream is(line);

		is >> n >> dummy;
		while(is >> link) {
			graph[n].links.insert(link);
			if(is.peek() == ',')
				is.ignore();
		}
	}
	inF.close();

	// part 1
	cout << "part 1: " << get_group_size(graph, 0) << endl;

	// part 2
	int groups = 1;
	// find nodes in graph that hasn't been visited yet and call
	// get_group_size on them to mark all nodes connected to them
	// as visited. continue until all nodes have been visited.
	for(bool finished = false; !finished; ) {
		int next_root = -1;
		for(pair keyval: graph) {
			if(keyval.second.visited == false) {
				next_root = keyval.first;
				break;
			}
		}
		if(next_root >= 0) {
			groups++;
			get_group_size(graph, next_root);
		} else {
			finished = true;
		}
	}
	cout << "part 2: " << groups << endl;

	return 0;
}