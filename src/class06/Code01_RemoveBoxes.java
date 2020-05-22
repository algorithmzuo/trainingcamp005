package class06;

public class Code01_RemoveBoxes {

	public int removeBoxes(int[] boxes) {
		int N = boxes.length;
		int[][][] dp = new int[N][N][N];
		return process(boxes, 0, N - 1, 0, dp);
	}

	public static int process(int[] boxes, int i, int j, int k, int[][][] dp) {
		if (i > j) {
			return 0;
		}
		if (dp[i][j][k] != 0) {
			return dp[i][j][k];
		}
		if (i == j) {
			dp[i][j][k] = (k + 1) * (k + 1);
			return dp[i][j][k];
		}
		while (i < j && boxes[i] == boxes[i + 1]) {
			i++;
			k++;
		}
		int ans = (k + 1) * (k + 1) + process(boxes, i + 1, j, 0, dp);
		for (int m = i + 1; m <= j; m++) {
			if (boxes[i] == boxes[m]) {
				ans = Math.max(ans, process(boxes, i + 1, m - 1, 0, dp) + process(boxes, m, j, k + 1, dp));
			}
		}
		dp[i][j][k] = ans;
		return ans;
	}

}
