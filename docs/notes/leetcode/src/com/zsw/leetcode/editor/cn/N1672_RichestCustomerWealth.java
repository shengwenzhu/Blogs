package com.zsw.leetcode.editor.cn;
public class N1672_RichestCustomerWealth {
    public static void main(String[] args) {
        Solution solution = new N1672_RichestCustomerWealth().new Solution();
    }
    //leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int maximumWealth(int[][] accounts) {
        int max = 0;
        int m = accounts.length, n = accounts[0].length;
        for (int i = 0; i < m; i++) {
            int temp = 0;
            for (int j = 0; j < n; j++) {
                temp += accounts[i][j];
            }
            if(temp > max) {
                max = temp;
            }
        }
        return max;
    }
}
//leetcode submit region end(Prohibit modification and deletion)

}
