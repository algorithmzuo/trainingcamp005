package class08;

import java.util.Arrays;

public class SuffixArray {

	// sa[i]: 排第 i 的是谁？
	// rank[i]: i排第几 rank[sa[i]]=i=sa[rank[i]];
	// height[i]: 排名第i的和排名第 i-1 的lcp
	// h[i]: suffix[i] 和在他前一位的lcp, h[i]=height[rank[i]]; height[i]=h[sa[i]];
	public static class Suffix {
		public int n;
		public int[] wa;
		public int[] wb;
		public int[] wv;
		public int[] wt;
		public int[] r;
		public int[] rak;
		public int[] height;
		public int[] sa;
		public int[] h;
		public int[][] rmq;
		public char[] ch;
		public int MAXV;

		 // "abcfg"
		// [ ahao, bhao, chao, fhao ]
		// str N ,    max  256
		public Suffix(String str, int max) {
			MAXV = max;
			n = str.length();
			init(Math.max(MAXV, n + 3));
			create(str);
		}

		private void init(int N) {
			wa = new int[N];
			wb = new int[N];
			wv = new int[N];
			wt = new int[N];
			r = new int[N];
			rak = new int[N];
			height = new int[N];
			sa = new int[N];
			h = new int[N];
			rmq = new int[N][18];
			ch = new char[N];
		}

		private void create(String str) {
			for (int i = 0; i < n; i++) {
				ch[i] = str.charAt(i);
			}
			for (int i = 0; i <= n; i++)
				r[i] = (int) ch[i];
			// r[n] = 0;
			DA(r, sa, n + 1, MAXV);
			for (int i = 0; i < n; i++) {
				rak[sa[i]] = i;
			}
			for (int i = 0; i < n; i++) {
				if (rak[i] == 0) {
					h[i] = 0;
				} else if (i == 0 || h[i - 1] <= 1) {
					h[i] = lcp(i, sa[rak[i] - 1]);
				} else {
					h[i] = h[i - 1] - 1 + lcp(i + h[i - 1] - 1, sa[rak[i] - 1] + h[i - 1] - 1);
				}
			}
			for (int i = 0; i < n; i++) {
				height[i] = h[sa[i]];
			}
			RMQ();
		}

		private boolean cmp(int[] r, int a, int b, int l) {
			return r[a] == r[b] && r[a + l] == r[b + l];
		}

		private void DA(int[] r, int[] sa, int n, int m) {
			int i, j, p;
			int[] x = wa;
			int[] y = wb;
			int[] t;
			for (i = 0; i < m; i++)
				wt[i] = 0;
			for (i = 0; i < n; i++)
				wt[x[i] = r[i]]++;
			for (i = 1; i < m; i++)
				wt[i] += wt[i - 1];
			for (i = n - 1; i >= 0; i--)
				sa[--wt[x[i]]] = i;
			for (j = 1, p = 1; p < n; j *= 2, m = p) {
				for (p = 0, i = n - j; i < n; i++)
					y[p++] = i;
				for (i = 0; i < n; i++)
					if (sa[i] >= j)
						y[p++] = sa[i] - j;
				for (i = 0; i < n; i++)
					wv[i] = x[y[i]];
				for (i = 0; i < m; i++)
					wt[i] = 0;
				for (i = 0; i < n; i++)
					wt[wv[i]]++;
				for (i = 1; i < m; i++)
					wt[i] += wt[i - 1];
				for (i = n - 1; i >= 0; i--)
					sa[--wt[wv[i]]] = y[i];
				for (t = x, x = y, y = t, p = 1, x[sa[0]] = 0, i = 1; i < n; i++)
					x[sa[i]] = cmp(y, sa[i - 1], sa[i], j) ? p - 1 : p++;
			}
			for (i = 0; i < n; i++) { // 下标从 0 到 n-1
				sa[i] = sa[i + 1];
			}
		}

		private int lcp(int i, int j) {
			int ret = 0;
			while (i < n && j < n && ch[i] == ch[j]) {
				i++;
				j++;
				ret++;
			}
			return ret;
		}

		private void RMQ() {
			for (int i = 0; i < n; i++)
				rmq[i][0] = height[i];
			for (int j = 1; j < 18; j++) {
				for (int i = 0; i < n; i++) {
					rmq[i][j] = rmq[i][j - 1];
					if (i - (1 << (j - 1)) >= 0) {
						rmq[i][j] = Math.min(rmq[i][j], rmq[i - (1 << (j - 1))][j - 1]);
					}
				}
			}
		}

		public int get_LCP(int x, int y) {
			if (x == y) {
				return n - x;
			}
			x = rak[x];
			y = rak[y];
			if (x > y) {
				int tmp = y;
				y = x;
				x = tmp;
			}
			int ans = height[y];
			for (int i = 17; i >= 0; i--) {
				if (y - (1 << i) >= x) {
					ans = Math.min(ans, rmq[y][i]);
					y -= (1 << i);
				}
			}
			return ans;
		}

		public int[] getSA() {
			int[] ans = new int[n];
			for (int i = 0; i < n; i++) {
				ans[i] = sa[i];
			}
			return ans;
		}

		public int[] getRank() {
			int[] ans = new int[n];
			for (int i = 0; i < n; i++) {
				ans[sa[i]] = i + 1;
			}
			return ans;
		}

	}

	// for test
	public static int[] right(String str) {
		int N = str.length();
		String[] arr = new String[N];
		for (int i = 0; i < N; i++) {
			arr[i] = str.substring(i);
		}
		Arrays.sort(arr);
		int[] sa = new int[N];
		for (int i = 0; i < N; i++) {
			sa[N - arr[i].length()] = i + 1;
		}
		return sa;
	}

	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static String random(int len, int max) {
		char[] str = new char[(int) (Math.random() * len) + 2];
		for (int i = 0; i < str.length; i++) {
			str[i] = (char) ((int) (Math.random() * max) + 'a');
		}
		return String.valueOf(str);
	}

	public static boolean isEqualArray(int[] arr1, int[] arr2) {
		if (arr1.length != arr2.length) {
			return false;
		}
		int N = arr1.length;
		for (int i = 0; i < N; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int len = 10000;
		int max = 26;
//		for (int i = 0; i < 100; i++) {
//			String test = random(len, max);
//			int[] sa1 = right(test);
//			Suffix suffix = new Suffix(test, 150);
//			int[] sa2 = suffix.getRank();
//			if (!isEqualArray(sa1, sa2)) {
//				System.out.println(test);
//				printArray(sa1);
//				printArray(sa2);
//			}
//		}

		len = 20000;
		max = 26;
		long start;
		long end;

		char[] str = new char[len];
		for (int i = 0; i < str.length; i++) {
			str[i] = (char) ((int) (Math.random() * max) + 'a');
		}

		String test = String.valueOf(str);
		start = System.nanoTime();
		right(test);
		end = System.nanoTime();
		System.out.println(end - start);
		start = System.nanoTime();
		new Suffix(test, 150).getRank();
		end = System.nanoTime();
		System.out.println(end - start);

	}

}
