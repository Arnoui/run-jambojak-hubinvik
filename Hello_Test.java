class Test
{
	int main()
	{
		String a = inner1();
	}
	
	String inner1() {
		int b = 0;
		for (int i = 0; i < 5; i=i+1) b = b+1;
		if (b > 4) return "Ahoj";
		else return "Nazdar";
	}
	
}