## hashCode() 与 equals() 

hashCode() 方法用于获取对象的哈希值，返回一个 int 整数；**HashCode 只有才使用哈希表时才有效**，用于确定对象在哈希表中的存储位置；

equlas() 方法用于比较两个对象是否相等；

**为什么重写 equals 时必须重写 hashCode 方法**?

以 HashSet 为例，当使用 HashSet 集合存储元素时，向 HashSet 中插入元素，需要判断插入的元素是否重复；HashSet 判断两个对象是否相同时，首先计算出插入对象的哈希值，然后通过哈希值得到对象在底层数组中插入的位置；然后通过 equals() 方法判断插入位置是否有重复的元素；所以在 HashSet集合中 equals 方法建立在 hashcode 方法基础上；

所以如果两个对象相等，则 `hashcode` 一定也是相同的；两个对象相等，调用 `equals()` 方法返回 true；两个对象有相同的 `hashcode` 值，它们也不一定是相等的；



## == 与 equals 的区别

+ **==** 

  + 既可以用于比较基本类型，也可以用于比较引用类型；

  + 对于基本类型，比较的是值是否相同；

  + 对于引用类型，比较的是两个对象是否为同一个对象（即两个对象的地址是否相同）；

+ **equals 方法**

  + 只能用于引用类型的比较；

  + 如果类中没有重写 equals 方法，equals 方法与 == 等价；

    ```java
    public boolean equals(Object obj) 
    {
    	return (this == obj);
    }
    ```

    + 如果类重写了 equals 方法，通过对象的内容进行比较，如 String 类重写了 equals 方法，比较字符串是否相同；

  

## 