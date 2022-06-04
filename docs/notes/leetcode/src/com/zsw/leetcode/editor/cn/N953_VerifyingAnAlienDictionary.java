package com.zsw.leetcode.editor.cn;

import java.util.HashMap;
import java.util.Map;

public class N953_VerifyingAnAlienDictionary {
    public static void main(String[] args) {
        Solution solution = new N953_VerifyingAnAlienDictionary().new Solution();
        String[] words = {"fxasxpc", "dfbdrifhp", "nwzgs", "cmwqriv", "ebulyfyve", "miracx", "sxckdwzv", "dtijzluhts", "wwbmnge", "qmjwymmyox"};
        String order = "zkgwaverfimqxbnctdplsjyohu";
        System.out.println(solution.isAlienSorted(words, order));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean isAlienSorted(String[] words, String order) {
            Map<Character, Integer> letters = new HashMap<>();
            int len = order.length();
            for (int i = 0; i < len; i++) {
                letters.putIfAbsent(order.charAt(i), i);
            }
            for (int i = 1; i < words.length; i++) {
                // 判断当前单词与前一个单词是否有序
                String pre = words[i - 1];
                String word = words[i];
                char[] char1 = pre.toCharArray();
                char[] char2 = word.toCharArray();
                int lim = Math.min(char1.length, char2.length);
                for (int k = 0; k < lim; k++) {
                    char c1 = char1[k];
                    char c2 = char2[k];
                    if (c1 == c2) {
                        continue;
                    } else if (letters.get(c1) < letters.get(c2)) {
                        break;
                    } else {
                        return false;
                    }
                }
                
            }
            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
