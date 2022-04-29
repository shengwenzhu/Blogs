package com.zsw.leetcode.editor.cn;

import java.util.Random;

public class N382_LinkedListRandomNode {
    public static void main(String[] args) {
    }

    class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {

        ListNode head;
        Random random;

        public Solution(ListNode head) {
            this.head = head;
            random = new Random();
        }

        public int getRandom() {
            // 记录当前遍历节点的索引（从1开始）
            int i = 0;
            int res = -1;
            for (ListNode cursor = head; cursor != null; cursor = cursor.next) {
                i++;
                // 第一个节点被选中的概率为1，其余节点被选中的概率为1/i
                if (random.nextInt(i) == 0) {
                    res = cursor.val;
                }
            }
            return res;
        }
    }

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(head);
 * int param_1 = obj.getRandom();
 */
//leetcode submit region end(Prohibit modification and deletion)

}
