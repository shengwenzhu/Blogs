package com.zsw.leetcode.editor.cn;

import java.util.*;

public class N1823_FindTheWinnerOfTheCircularGame {
    public static void main(String[] args) {
        Solution solution = new N1823_FindTheWinnerOfTheCircularGame().new Solution();
        System.out.println(solution.findTheWinner(5, 2));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * @param n  初始时n个人
         * @param k  第k个人游戏出局
         * @return
         */
        public int findTheWinner(int n, int k) {
            Queue<Integer> queue = new ArrayDeque<>();
            for (int i = 1; i <= n; i++) {
                queue.add(i);
            }
            while (queue.size() != 1) {
                for (int i = 1; i < k; i++) {
                    queue.add(queue.remove());
                }
                queue.remove();
            }
            return queue.element();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
