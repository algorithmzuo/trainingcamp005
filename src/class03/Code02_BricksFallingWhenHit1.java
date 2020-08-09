package class03;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Code02_BricksFallingWhenHit1 {

	// 每一个1，都有一个专属于自己的Dot
	public static class Dot {

	}

	public static class UnionFind {
		// 原始数组，经过炮弹的影响之后，所得到的grid
		private int[][] grid; // 主函数处理后的原始矩
		// 如果gird[i][j] == 1, dots[i][j] = new 的点
		private Dot[][] dots; // 位置到点的对应关系
		private int N; // 行数
		private int M; // 列数
		private int cellingAll; // 有多少个1能连接到天花板

		// 某个集合，是不是整体接到天花版上去了，如果是，假设这个集合代表点是X,cellingSet包含x
		// 如果不是，假设这个集合代表点是X,cellingSet不包含X
		private HashSet<Dot> cellingSet; // 集合能够连到天花板，它的代表点才在里面
		private HashMap<Dot, Dot> fatherMap; // 任何一个dot都有记录，value就是父节点
		private HashMap<Dot, Integer> sizeMap;
		// 只有一个dot是代表点，才有记录，value表示这个集合的大小

		// matrix，炮弹会让某些位置的1变成2，2和0一样，表示不连通；只有1是联通的，上下左右
		public UnionFind(int[][] matrix) {
			initSpace(matrix);
			initConnect();
		}

		private void initSpace(int[][] matrix) {
			grid = matrix;
			N = grid.length;
			M = grid[0].length;
			// 接到天花板上砖的数量
			cellingAll = 0;
			dots = new Dot[N][M]; // dot null
			cellingSet = new HashSet<>();
			fatherMap = new HashMap<>();
			sizeMap = new HashMap<>();
			for (int row = 0; row < N; row++) {
				for (int col = 0; col < M; col++) {
					// 遍历每一个[i][j] 2 0 直接跳过
					if (grid[row][col] == 1) { // 该点是我关心的，不是1就不关心
						Dot cur = new Dot();
						dots[row][col] = cur;
						fatherMap.put(cur, cur);
						sizeMap.put(cur, 1);
						if (row == 0) { // dot是天花板上的点
							cellingSet.add(cur);
							cellingAll++;
						}
					}
				}
			}
		}

		private void initConnect() {
			for (int row = 0; row < N; row++) {
				for (int col = 0; col < M; col++) {
					union(row, col, row - 1, col);
					union(row, col, row + 1, col);
					union(row, col, row, col - 1);
					union(row, col, row, col + 1);
				}
			}
		}

		// row，col 的dot，所在的集合，代表dot是谁返回
		private Dot find(int row, int col) {
			Dot cur = dots[row][col];
			Stack<Dot> stack = new Stack<>();
			while (cur != fatherMap.get(cur)) {
				stack.add(cur);
				cur = fatherMap.get(cur);
			}
			while (!stack.isEmpty()) {
				fatherMap.put(stack.pop(), cur);
			}
			return cur;
		}

		private void union(int r1, int c1, int r2, int c2) {
			if (valid(r1, c1) && valid(r2, c2)) {
				Dot father1 = find(r1, c1);
				Dot father2 = find(r2, c2);
				if (father1 != father2) {
					int size1 = sizeMap.get(father1);
					int size2 = sizeMap.get(father2);

//					Dot big = size1 >= size2 ? father1 : father2;
//					Dot small = big == father1 ? father2 : father1;
//					fatherMap.put(small, big);
//					sizeMap.put(big, size1 + size2);
//					// 集合1整体连不连的到天花板上
//					boolean status1 = cellingSet.contains(father1);
//					// 集合2整体连不连的到天花板上
//					boolean status2 = cellingSet.contains(father2);
//					if(status1 ^ status2) {
//						cellingSet.add(big);
//						cellingAll += status1 ? size2 : size1;
//					}
//					
					// 集合1整体连不连的到天花板上
					boolean status1 = cellingSet.contains(father1);
					// 集合2整体连不连的到天花板上
					boolean status2 = cellingSet.contains(father2);
					if (size1 <= size2) {
						// 集合1与集合2，已经合完了，共同的父节点father2
						fatherMap.put(father1, father2);
						sizeMap.put(father2, size1 + size2);
						// 如果两个集合能否接到天花板的状态不一样
						if (status1 ^ status2) {
							cellingSet.add(father2);
							cellingAll += status1 ? size2 : size1;
						}
					} else {
						fatherMap.put(father2, father1);
						sizeMap.put(father1, size1 + size2);
						if (status1 ^ status2) {
							cellingSet.add(father1);
							cellingAll += status1 ? size2 : size1;
						}
					}
				}
			}
		}

		private boolean valid(int row, int col) {
			return row >= 0 && row < N && col >= 0 && col < M && grid[row][col] == 1;
		}

		public int cellingNum() {
			return cellingAll;
		}

		// 原来row,col 2 finger 1
		// 在该位置变成1的情况下，并查集该如何变化，接到天花板上的1又会如何变化
		public int finger(int row, int col) {
			int pre = cellingAll;
			grid[row][col] = 1;
			Dot cur = new Dot();
			dots[row][col] = cur;
			if (row == 0) {
				cellingSet.add(cur);
				cellingAll++;
			}
			fatherMap.put(cur, cur);
			sizeMap.put(cur, 1);
			union(row, col, row - 1, col);
			union(row, col, row + 1, col);
			union(row, col, row, col - 1);
			union(row, col, row, col + 1);
			int now = cellingAll;
			return now == pre ? 0 : now - pre - 1;
		}
	}

	public static int[] hitBricks(int[][] grid, int[][] hits) {
		// 把炮弹影响加上，grid会怎么变
		for (int i = 0; i < hits.length; i++) {
			if (grid[hits[i][0]][hits[i][1]] == 1) {
				grid[hits[i][0]][hits[i][1]] = 2;
			}
		}

		UnionFind unionFind = new UnionFind(grid);
		int[] ans = new int[hits.length];
		for (int i = hits.length - 1; i >= 0; i--) {
			if (grid[hits[i][0]][hits[i][1]] == 2) {
				ans[i] = unionFind.finger(hits[i][0], hits[i][1]);
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		int[][] grid = { { 1, 0, 1 }, { 1, 1, 1 } };
		int[][] hits = { { 0, 0 }, { 0, 2 }, { 1, 1 } };
		int[] ans = hitBricks(grid, hits);
		for (int i = 0; i < ans.length; i++) {
			System.out.println(ans[i]);
		}
	}

}
