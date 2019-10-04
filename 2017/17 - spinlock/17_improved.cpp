/*
	This is an improved solution based on solutions of other people.
*/

#include <iostream>
#include <vector>


using namespace std;


int main()
{
	int input = 380;

	/*
		part 1
		For part 1 the number of iterations is small enough so that
		we can generate the entire spinlock and read off the answer.
		Instead of tracking the current position and inserting in the
		middle of the buffer, we rotate the buffer by the period
		given in the input so that the insertion point is always at
		the end. For most data structures insertion at the end is
		the most efficient one.
	*/
	vector<int> buf{0};
	for(int n = 1; n <= 2017; ++n) {
		rotate(buf.begin(), buf.begin() + (input % buf.size()), buf.end());
		buf.push_back(n);
	}

	cout << "part 1: " << buf[0] << endl;

	/*
		part 2
		Instead of generating the whole buffer it is sufficient to
		keep track of the elements that get inserted at after 0,
		i.e. at position 1. To do this we don't need to save the
		insertion point of all other numbers and it's enough to
		track the current position and current length.
	*/
	int pos = 0;
	int after_zero = 0;
	for(int n = 1; n <= 50000000; ++n) {
		/*
			the current length of the list is `n`,
			we go `input` steps while wrapping around and
			insert the new element after wherever we stopped.
		*/
		pos = (pos + input) % n + 1;
		if(pos == 1)
			after_zero = n;
	}

	cout << "part 2: " << after_zero << endl;


	return 0;
}