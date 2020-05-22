package class01;

public class Problem02_MinWindowLength {

	public static int minLength(String str1, String str2) {
		if (str1 == null || str2 == null || str1.length() < str2.length()) {
			return 0;
		}
		char[] chas1 = str1.toCharArray();
		char[] chas2 = str2.toCharArray();
		int[] map = new int[256];
		for (int i = 0; i != chas2.length; i++) {
			map[chas2[i]]++;
		}
		int left = 0;
		int right = 0;
		int all = chas2.length;
		int minLen = Integer.MAX_VALUE;
		while (right != chas1.length) {
			map[chas1[right]]--;
			if (map[chas1[right]] >= 0) {
				all--;
			}
			if (all == 0) {
				while (map[chas1[left]] < 0) {
					map[chas1[left++]]++;
				}
				minLen = Math.min(minLen, right - left + 1);
				all++;
				map[chas1[left++]]++;
			}
			right++;
		}
		return minLen == Integer.MAX_VALUE ? 0 : minLen;
	}

	public static void main(String[] args) {
		String str1 = "adabbca";
		String str2 = "acb";
		System.out.println(minLength(str1, str2));

	}

}
