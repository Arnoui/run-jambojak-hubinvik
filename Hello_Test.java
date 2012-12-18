class Test4
{
	int main()
	{
		int c = 0;
		for (int i = 0; i < 3; i = i + 1) {
			c = c + i*2 + i*3; // 1st run 0; 2nd 5; 3rd 15
		}
		int b = 25;
		int a = 24;
		int result = b-a+c; // 25-24+15 = 16
	}
}
