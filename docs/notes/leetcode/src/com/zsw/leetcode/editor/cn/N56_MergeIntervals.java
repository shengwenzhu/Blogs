package com.zsw.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class N56_MergeIntervals {
    public static void main(String[] args) {
        Solution solution = new N56_MergeIntervals().new Solution();
    }
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        /**
         * 第一步：对数组进行排序，首先按照区间的左端点升序排列，左端点相同时按照右端点升序排列
         * 第二步：使用列表存储最终结果，遍历已排序的区间数组，将每一个区间插入到列表中
         */
        public int[][] merge(int[][] intervals) {
            Arrays.sort(intervals, new Comparator<int[]>() {
                @Override
                public int compare(int[] interval1, int[] interval2) {
                    if (interval1[0] != interval2[0]) {
                        return interval1[0] - interval2[0];
                    } else {
                        return interval1[1] - interval2[1];
                    }
                }
            });
            List<int[]> merged = new ArrayList<>();
            for (int i = 0; i < intervals.length; i++) {
                int left = intervals[i][0], right = intervals[i][1];
                // 当列表尚且为空时或列表中最后一个区间的右端点小于当前区间的左端点时，之间将当前区间加入到列表中
                if (merged.size() == 0 || merged.get(merged.size() - 1)[1] < left) {
                    merged.add(new int[]{left, right});
                } else {
                    merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], right);
                }
            }
            return merged.toArray(new int[merged.size()][]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
