package com.calc;

public class CalculatorImpl implements Calculator {

	@Override
	public int add(int i, int j) {
		long a = i;
		long b = j;
		long result = a + b;

		if (result > Integer.MAX_VALUE) {
			throw new OverflowException();
		}

		if (result < Integer.MIN_VALUE) {
			throw new UnderflowException();
		}
		return (int) result;
	}

	@Override
	public int sub(int i, int j) {
		long a = i;
		long b = j;
		long result = a - b;

		if (result < Integer.MIN_VALUE) {
			throw new UnderflowException();
		}
		return (int) result;
	}

	@Override
	public int add(String string) {
		String[] coms = string.split(",");
		int total = 0;
		for (int i = 0; i < coms.length; i++) {
			if (coms[i].indexOf("-") > -1) {
				String[] nums = coms[i].split("-");
				int sum = 0;
				for (int j = Integer.parseInt(nums[0]); j <= Integer.parseInt(nums[1]); j++) {
					sum += j;
				}
				total += sum;
			} else {
				total += Integer.parseInt(coms[i]);
			}
		}
		return total;
	}

	@Override
	public int mul(int i, int j) {
		return i * j;
	}
}
