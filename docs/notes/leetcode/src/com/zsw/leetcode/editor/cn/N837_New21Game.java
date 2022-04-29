package com.zsw.leetcode.editor.cn;

public class N837_New21Game {
    public static void main(String[] args) {
        Solution solution = new N837_New21Game().new Solution();
        System.out.println(solution.new21Game(21, 17, 10));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        // 存储所有结果数量
        double total = 0;
        // 存储满足答案的结果数量
        double success = 0;

        public double new21Game(int n, int k, int maxPts) {
            dfs(n, k, maxPts, 0);
            return success / total;
        }

        private void dfs(int n, int k, int maxPts, int curPts) {
            if (curPts >= k) {
                total++;
                success += curPts <= n ? 1 : 0;
                return;
            }
            for (int i = 1; i <= maxPts; i++) {
                dfs(n, k, maxPts, curPts + i);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
