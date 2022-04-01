package com.zsw.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class N406_QueueReconstructionByHeight {
    public static void main(String[] args) {
        Solution solution = new N406_QueueReconstructionByHeight().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 首先对二维数组进行排序：按照身高从大到小降序排列，身高相同时按照第二个关键字升序排列
         * 按照排完后的顺序，依次将每个人插入队列中，插入的位置由 people[i][1] 决定
         * 示例：输入 people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
         * 第一步：排序结果：[7,0],[7,1],[6,1],[5,0],[5,2],[4,4]
         * 第二步：按照顺序依次插入队列中：
         * 1）[7,0]
         * 2）[7,0],[7,1]
         * 3）[7,0],[6,1],[7,1]
         * 4）[5,0],[7,0],[6,1],[7,1]
         * 5）[5,0],[7,0],[5,2],[6,1],[7,1]
         * 6）[5,0],[7,0],[5,2],[6,1],[4,4]，[7,1]
         *
         * @param people
         * @return
         */
        public int[][] reconstructQueue(int[][] people) {
            Comparator<int[]> comparator = new Comparator<int[]>() {
                @Override
                public int compare(int[] people1, int[] people2) {
                    if (people1[0] != people2[0]) {
                        // 按照身高从大到小降序排列
                        return people2[0] - people1[0];
                    } else {
                        // 身高相同时按照第二个关键字升序排列
                        return people1[1] - people2[1];
                    }
                }
            };
            Arrays.sort(people, comparator);
            List<int[]> res = new ArrayList<>();
            for (int[] person : people) {
                res.add(person[1], person);
            }
            return res.toArray(new int[res.size()][]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
