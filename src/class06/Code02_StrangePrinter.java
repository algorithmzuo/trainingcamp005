package class06;

public class Code02_StrangePrinter {

	public static int strangePrinter(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[][] dp = new int[N][N];
		dp[N - 1][N - 1] = 1;
		for (int i = 0; i < N - 1; i++) {
			dp[i][i] = 1;
			dp[i][i + 1] = str[i] == str[i + 1] ? 1 : 2;
		}
		for (int i = N - 2; i >= 0; i--) {
			for (int j = i + 2; j < N; j++) {
				dp[i][j] = j - i + 1;
				for (int k = i + 1; k <= j; k++) {
					dp[i][j] = Math.min(dp[i][j], dp[i][k - 1] + dp[k][j] - (str[i] == str[k] ? 1 : 0));
				}
			}
		}
		return dp[0][N - 1];
	}

}
