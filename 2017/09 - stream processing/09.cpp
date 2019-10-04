#include<iostream>
#include<fstream>


using namespace std;


int main()
{
	int level = 0;
	int level_sum = 0;
	int garbage_cnt = 0;
	char c;
	bool garbage = false;
	bool cancel = false;

	fstream inF("09_input.txt");
	while(inF >> c) {
		if(cancel)
			cancel = false;
		else if(c == '!')
			cancel = true;
		else if(garbage) {
			if(c == '>')
				garbage = false;
			else
				garbage_cnt++;
		}
		else if(c == '<')
			garbage = true;
		else if(c == '{')
			level += 1;
		else if(c == '}') {
			level_sum += level;
			level -= 1;
		}
	}
	inF.close();
	
	cout << "part 1: " << level_sum << endl;
	cout << "part 2: " << garbage_cnt << endl;

	return 0;
}