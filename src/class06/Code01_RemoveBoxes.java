package class06;

public class Code01_RemoveBoxes {

	public static int removeBoxes(int[] boxes) {
		int N = boxes.length;
		int[][][] dp = new int[N][N][N];
		int ans = process(boxes, 0, N - 1, 0, dp);
		return ans;
	}

	public static int process(int[] boxes, int L, int R, int K, int[][][] dp) {
		if (L > R) {
			return 0;
		}
		if (dp[L][R][K] > 0) {
			return dp[L][R][K];
		}
		int last = L;
		while (last + 1 <= R && boxes[last + 1] == boxes[L]) {
			last++;
		}
		int pre = K + last - L;
		int ans = (pre + 1) * (pre + 1) + process(boxes, last + 1, R, 0, dp);
		for (int i = last + 2; i <= R; i++) {
			if (boxes[i] == boxes[L] && boxes[i - 1] != boxes[L]) {
				ans = Math.max(ans, process(boxes, last + 1, i - 1, 0, dp) + process(boxes, i, R, pre + 1, dp));
			}
		}
		dp[L][R][K] = ans;
		return ans;
	}

}
