package class08;

public class CreateMaximumNumber {

	public static int[] maxNumber1(int[] nums1, int[] nums2, int k) {
		int len1 = nums1.length;
		int len2 = nums2.length;
		if (k < 0 || k > len1 + len2) {
			return null;
		}
		int[] res = new int[k];
		int[][] dp1 = getdp(nums1); // 生成dp1这个表，以后从nums1中，只要固定拿N个数，
		int[][] dp2 = getdp(nums2);
		
		
		for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
			// arr1 挑 get1个，怎么得到一个最优结果
			int[] pick1 = maxPick(nums1, dp1, get1);
			int[] pick2 = maxPick(nums2, dp2, k - get1);
			int[] merge = merge(pick1, pick2);
			res = preMoreThanLast(res, 0, merge, 0) ? res : merge;
		}
		return res;
	}

	public static int[] merge(int[] nums1, int[] nums2) {
		int k = nums1.length + nums2.length;
		int[] ans = new int[k];
		for (int i = 0, j = 0, r = 0; r < k; ++r) {
			ans[r] = preMoreThanLast(nums1, i, nums2, j) ? nums1[i++] : nums2[j++];
		}
		return ans;
	}

	public static boolean preMoreThanLast(int[] nums1, int i, int[] nums2, int j) {
		while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
			i++;
			j++;
		}
		return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
	}

	public static class Suffix {
		public int n;
		public int[] wa;
		public int[] wb;
		public int[] wv;
		public int[] wt;
		public int[] r;
		public int[] sa;
		public int[] ch;
		public int MAXN;

		// arr[i]认为是一个字符
		// max arr中的最大值+1 >
		// 别让arr中有0
		// arr 中Value最大值
		// arr 中不能有0这个值
		public Suffix(int[] arr, int max) {
			MAXN = max;
			n = arr.length;
			init(Math.max(MAXN, n + 3));
			create(arr);
		}

		private void init(int N) {
			wa = new int[N];
			wb = new int[N];
			wv = new int[N];
			wt = new int[N];
			r = new int[N];
			sa = new int[N];
			ch = new int[N];
		}

		private void create(int[] arr) {
			for (int i = 0; i < n; i++) {
				ch[i] = arr[i];
			}
			for (int i = 0; i <= n; i++)
				r[i] = (int) ch[i];
			DA(r, sa, n + 1, MAXN);
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
			for (i = 0; i < n; i++) {
				sa[i] = sa[i + 1];
			}
		}

		public int[] getRank() {
			int[] ans = new int[n];
			for (int i = 0; i < n; i++) {
				ans[sa[i]] = i + 1;
			}
			return ans;
		}

	}

	public static int[] maxNumber2(int[] nums1, int[] nums2, int k) {
		int len1 = nums1.length;
		int len2 = nums2.length;
		if (k < 0 || k > len1 + len2) {
			return null;
		}
		int[] res = new int[k];
		int[][] dp1 = getdp(nums1);
		int[][] dp2 = getdp(nums2);
		for (int get1 = Math.max(0, k - len2); get1 <= Math.min(k, len1); get1++) {
			int[] pick1 = maxPick(nums1, dp1, get1);
			int[] pick2 = maxPick(nums2, dp2, k - get1);
			int[] merge = mergeBySuffixArray(pick1, pick2);
			res = moreThan(res, merge) ? res : merge;
		}
		return res;
	}

	public static boolean moreThan(int[] pre, int[] last) {
		int i = 0;
		int j = 0;
		while (i < pre.length && j < last.length && pre[i] == last[j]) {
			i++;
			j++;
		}
		return j == last.length || (i < pre.length && pre[i] > last[j]);
	}

	public static int[] mergeBySuffixArray(int[] nums1, int[] nums2) {
		int size1 = nums1.length;
		int size2 = nums2.length;
		int[] nums = new int[size1 + 1 + size2];
		for (int i = 0; i < size1; i++) {
			nums[i] = nums1[i] + 2;
		}
		nums[size1] = 1;
		for (int j = 0; j < size2; j++) {
			nums[j + size1 + 1] = nums2[j] + 2;
		}
		Suffix suffix = new Suffix(nums, 12);
		int[] rank = suffix.getRank();
		int[] ans = new int[size1 + size2];
		int i = 0;
		int j = 0;
		int r = 0;
		while (i < size1 && j < size2) {
			ans[r++] = rank[i] > rank[j + size1 + 1] ? nums1[i++] : nums2[j++];
		}
		while (i < size1) {
			ans[r++] = nums1[i++];
		}
		while (j < size2) {
			ans[r++] = nums2[j++];
		}
		return ans;
	}

	public static int[][] getdp(int[] arr) {
		int size = arr.length; // 0~N-1
		int pick = arr.length + 1; // 1 ~ N
		int[][] dp = new int[size][pick];
		// get 不从0开始，因为拿0个无意义
		// get 1
		for (int get = 1; get < pick; get++) { // 1 ~ N
			int maxIndex = size - get;
			// i~N-1
			for (int i = size - get; i >= 0; i--) {
				if (arr[i] >= arr[maxIndex]) {
					maxIndex = i;
				}
				dp[i][get] = maxIndex;
			}
		}
		return dp;
	}

	public static int[] maxPick(int[] arr, int[][] dp, int pick) {
		int[] res = new int[pick];
		for (int resIndex = 0, dpRow = 0; pick > 0; pick--, resIndex++) {
			res[resIndex] = arr[dp[dpRow][pick]];
			dpRow = dp[dpRow][pick] + 1;
		}
		return res;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static int[] generateArray(int len, int varible) {
		int[] arr = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * varible);
		}
		return arr;
	}

	public static boolean isEqual(int[] arr1, int[] arr2) {
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int len = 100;
		int varible = 9;
		for (int i = 0; i < 10000; i++) {
			int[] nums1 = generateArray(len, varible);
			int[] nums2 = generateArray(len, varible);
			int k = (int) (Math.random() * (nums1.length + nums2.length)) + 1;
			int[] ans1 = maxNumber1(nums1, nums2, k);
			int[] ans2 = maxNumber2(nums1, nums2, k);
			if (!isEqual(ans1, ans2)) {
				printArray(nums1);
				printArray(nums2);
				printArray(ans1);
				printArray(ans2);
				break;
			}
		}
	}
}
