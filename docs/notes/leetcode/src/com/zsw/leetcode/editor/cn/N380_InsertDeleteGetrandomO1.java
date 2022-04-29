package com.zsw.leetcode.editor.cn;

import java.util.*;

public class N380_InsertDeleteGetrandomO1 {

    //leetcode submit region begin(Prohibit modification and deletion)
    class RandomizedSet {

        List<Integer> list;
        Map<Integer, Integer> map;
        Random random;


        public RandomizedSet() {
            list = new ArrayList<>();
            map = new HashMap<>();
            random = new Random();
        }

        public boolean insert(int val) {
            if (map.containsKey(val)) {
                return false;
            }
            int index = list.size();
            list.add(val);
            map.put(val, index);
            return true;
        }

        /**
         * 这道题的难点之处，当元素val存在时，需要同时从两个集合中删除
         * 从list集合中删除元素时可能从中间进行删除，此时将该集合最后一个元素交换到当前位置
         *
         * @param val
         * @return
         */
        public boolean remove(int val) {
            if (!map.containsKey(val)) {
                return false;
            }
            // 获取元素val在列表中的索引
            int index = map.get(val);
            // 将列表中的最后一个元素交换到index索引处：注意修改map中相应元素对应的下标
            int lastElement = list.get(list.size() - 1);
            list.set(index, lastElement);
            map.put(lastElement, index);
            list.remove(list.size() - 1);
            // 从map集合中删除元素
            map.remove(val);
            return true;
        }

        public int getRandom() {
            int index = random.nextInt(list.size());
            return list.get(index);
        }
    }

/**
 * Your RandomizedSet object will be instantiated and called as such:
 * RandomizedSet obj = new RandomizedSet();
 * boolean param_1 = obj.insert(val);
 * boolean param_2 = obj.remove(val);
 * int param_3 = obj.getRandom();
 */
//leetcode submit region end(Prohibit modification and deletion)

}
