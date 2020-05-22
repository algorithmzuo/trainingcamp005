package class07;

import java.util.ArrayList;

public class Code01_TSP {

	// matrix[i][j]  -> i城市到j城市的距离
	public static int tsp1(int[][] matrix, int origin) {
		if (matrix == null || matrix.length < 2 || origin < 0 || origin >= matrix.length) {
			return 0;
		}
		// 要考虑的集合
		ArrayList<Integer> cities = new ArrayList<>();
		// cities[0] != null 表示0城在集合里
		// cities[i] != null 表示i城在集合里
		for (int i = 0; i < matrix.length; i++) {
			cities.add(1);
		}
		// null,1,1,1,1,1,1
		// origin城不参与集合 
		cities.set(origin, null);
		return process(matrix, origin, cities, origin);
	}

	// matrix 所有距离，存在其中
	// origin 固定参数，唯一的目标
	// cities 要考虑的集合，一定不含有origin
	// 当前来到的城市是谁，cur
	public static int process(
			int[][] matrix, 
			int aim,
			ArrayList<Integer> cities, 
			int cur) {
		boolean hasCity = false; // 集团中还是否有城市
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i < cities.size(); i++) {
			if (cities.get(i) != null) {
				hasCity = true;
				cities.set(i, null);
				// matrix[cur][i]   +   f(i,  集团(去掉i)   )
				ans = Math.min(ans, matrix[cur][i] + process(matrix, aim, cities, i));
				cities.set(i, 1);
			}
		}
		return hasCity ? ans : matrix[cur][aim];
	}
	
	
	// cities 里，一定含有cur这座城
	// 解决的是，集合从cur出发，通过集合里所有的城市，最终来到aim，最短距离
	public static int process2(
			int[][] matrix, 
			int aim,
			ArrayList<Integer> cities, 
			int cur) {
		if(cities.size()==1) {
			return matrix[cur][aim];
		}
		cities.set(cur, null);
		int ans = Integer.MAX_VALUE;
		for(int i = 0 ; i < cities.size();i++) {
			if(cities.get(i) != null) {
				int dis = matrix[cur][i] + process2(matrix,aim,cities ,i);
				ans = Math.min(ans, dis);
			}
		}
		cities.set(cur, 1);
		return ans;
	}
	

	public static int tsp2(int[][] matrix, int origin) {
		if (matrix == null || matrix.length < 2 || origin < 0 || origin >= matrix.length) {
			return 0;
		}
		int N = matrix.length - 1; // 除去origin之后是n-1个点
		int S = 1 << N; // 状态数量
		int[][] dp = new int[S][N];
		int icity = 0;
		int kcity = 0;
		for (int i = 0; i < N; i++) {
			icity = i < origin ? i : i + 1;
			// 00000000   i
			dp[0][i] = matrix[icity][origin];
		}
		for (int status = 1; status < S; status++) {
			// 尝试每一种状态   status =  0 0 1 0 0 0 0 0 0
			//                    下标   8 7 6 5 4 3 2 1 0
			for (int i = 0; i < N; i++) {
				// i 枚举的出发城市
				dp[status][i] = Integer.MAX_VALUE;
				if ((1 << i & status) != 0) { 
					// 如果i这座城是可以枚举的，i = 6 ， i对应的原始城的编号，icity
					icity = i < origin ? i : i + 1;
					for (int k = 0; k < N; k++) { // i 这一步连到的点，k
						if ((1 << k & status) != 0) { // i 这一步可以连到k
							kcity = k < origin ? k : k + 1; // k对应的原始城的编号，kcity
							dp[status][i] 
									= 
									Math.min(dp[status][i], 
											dp[status ^ (1 << i)][k] 
													+ matrix[icity][kcity]);
						}
					}
				}
			}
		}
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			icity = i < origin ? i : i + 1;
			ans = Math.min(ans, dp[S - 1][i] + matrix[origin][icity]);
		}
		return ans;
	}

	public static int[][] generateGraph(int maxSize, int maxValue) {
		int len = (int) (Math.random() * maxSize) + 1;
		int[][] matrix = new int[len][len];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				matrix[i][j] = (int) (Math.random() * maxValue) + 1;
			}
		}
		for (int i = 0; i < len; i++) {
			matrix[i][i] = 0;
		}
		return matrix;
	}

	public static void main(String[] args) {
		int len = 9;
		int value = 100;
		for (int i = 0; i < 20000; i++) {
			int[][] matrix = generateGraph(len, value);
			int origin = (int) (Math.random() * matrix.length);
			int ans1 = tsp1(matrix, origin);
			int ans2 = tsp2(matrix, origin);
			if (ans1 != ans2) {
				System.out.println("fuck");
			}
		}
		System.out.println("test finished!");
	}

}
