package com.zsw.leetcode.editor.cn;

public class N2024_MaximizeTheConfusionOfAnExam {
    public static void main(String[] args) {
        Solution solution = new N2024_MaximizeTheConfusionOfAnExam().new Solution();
        System.out.println(solution.maxConsecutiveAnswers("TTFTTFTT", 1));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxConsecutiveAnswers(String answerKey, int k) {
            return Math.max(maxConsecutiveChar(answerKey, k, 'T'),
                    maxConsecutiveChar(answerKey, k, 'F'));
        }

        /**
         * 指定一个字符（T or F），然后使用滑动窗口寻找该字符最大的连续数目
         * 滑动窗口内允许存在至多k个其他字符
         */
        private int maxConsecutiveChar(String answerKey, int k, char ch) {
            int len = answerKey.length();
            // 滑动窗口左右边界
            int left = 0, right = 0;
            // 记录滑动窗口内不是字符ch的字符数目
            int num = 0;
            // 存储最大值
            int max = 0;
            while (right < len) {
                num += answerKey.charAt(right) != ch ? 1 : 0;
                while (num > k) {
                    max = Math.max(max, right - left);
                    num -= answerKey.charAt(left) != ch ? 1 : 0;
                    left++;
                }
                max = Math.max(max, right - left + 1);
                right++;
            }
            return max;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
