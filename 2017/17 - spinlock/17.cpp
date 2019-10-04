/*
	Note that the solution for part 2 is very slow,
	as we actually generate the whole buffer.
*/

#include <iostream>
#include <cassert>


using namespace std;


// The node struct to be used in the linked list.
struct Node
{
	int n;
	struct Node* next;

	Node(int init_n) : n{init_n}, next{0} {}
};


/*
	The class implementing the spinlock with rotation length p,
	and number of iterations it.
	We generate the whole spinlock as a linked list and therefore
	implement the methods rotate and push_back.
*/
class Spinlock
{
public:
	Spinlock(int p, int it);
	int part1();
	int part2();

private:
	int period;
	int iterations;
	Node *head;
	Node *tail;

	void rotate(Node* &head, Node* &tail, int steps);
	void push_back(Node* &tail, int n);
};


Spinlock::Spinlock(int p, int it)
{
	period = p;
	iterations = it;

	head = new Node(0);
	tail = head;

	int len = 1;
	for(int i = 1; i <= iterations; ++i) {
		rotate(head, tail, period % len);
		push_back(tail, i);
		++len;
	}
}


int Spinlock::part1()
{
	// The last inserted element is the tail, so the element after it is the head
	return head->n;
}


int Spinlock::part2()
{
	Node *tmp = head;
	while(tmp->n != 0)
		tmp = tmp->next;
	
	return tmp->next->n;
}


void Spinlock::rotate(Node* &head, Node* &tail, int steps)
{
	if(steps == 0)
		return;

	tail->next = head;
	tail = head;
	for(int i = 0; i < steps-1; ++i)
		tail = tail->next;
	head = tail->next;
	tail->next = 0;
}


void Spinlock::push_back(Node* &tail, int n)
{
	Node* new_node = new Node(n);
	tail->next = new_node;
	tail = new_node;
}


int main()
{
	int input = 380;

	assert(Spinlock(3, 2017).part1() == 638);
	
	cout << "part 1: " << Spinlock(input, 2017).part1() << endl;
	cout << "part 2: " << Spinlock(input, 50000000).part2() << endl;
	return 0;
}