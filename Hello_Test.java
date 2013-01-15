class Batoh
{
	int main ()
	{
		int MIN_INT = -2147483646;
	
		String strInput = "9150 20 250 22 175 4 131 2 30 7 11 26 135 6 71 1 249 16 141 43 138 15 164 40 252 21 172 3 9 19 88 48 70 18 42 49 146 8 182 41 68 27 67";
		String delim = "~"; // Znacka pro mezeru
		int_array input = split(strInput, delim);
		
		int len = input[1];
		int maxWeight = input[2];
		
		int_array weights = int_array[len];
		int_array costs = int_array[len];
		int_array permutations = int_array[len];
		
		for (int j = 0; j < len; j = j + 1)
		{
			int idx = 3 + j*2;
			int weight = input[idx];
			weights[j] = weight;
			
			int idx2 = idx + 1;
			int cost = input[idx2];
			costs[j] = cost;
		}
		
		int result = permute(weights,
					   costs,
					   permutations,
					   0,
					   maxWeight,
					   len);
	}
	
	int permute(int_array weights,
				int_array costs,
				int_array permutations,
				int depth,
				int maxWeight,
				int len)
	{
	
		if (depth < len - 1)
		{
			permutations[depth] = 0;
			int out0 = permute(weights,
							   costs,
							   permutations,
							   depth + 1,
							   maxWeight,
							   len);
			permutations[depth] = 1;
			int out1 = permute(weights,
							   costs,
							   permutations,
							   depth + 1,
							   maxWeight,
							   len);
			if (out0 > out1)
			{
				return out0;
			}
			else
			{
				return out1;
			}
		}
		
		if (depth == len - 1)
		{
			permutations[depth] = 0;
			int out0 = evaluate(weights,
								costs,
								permutations,
								maxWeight,
								len);
								
			permutations[depth] = 1;
			int out1 = evaluate(weights,
								costs,
								permutations,
								maxWeight,
								len);
			
			if (out0 > out1)
			{
				return out0;
			}
			else
			{
				return out1;
			}
		}
		
	}
	
	int evaluate(int_array weights,
				 int_array costs,
				 int_array permutations,
				 int maxWeight,
				 int len)
	{
		int totalWeight = 0;
		int totalCost = 0;
		for (int i = 0; i < len; i = i+1)
		{
			int permutation = permutations[i];
			if (permutation == 1)
			{
				int weight = weights[i];
				int cost = costs[i];
				totalWeight = totalWeight + weight;
				totalCost = totalCost + cost;
			}
		}
		if (totalWeight > maxWeight)
		{
			return 0;
		} else {
			return totalCost;
		}
	}
	
}