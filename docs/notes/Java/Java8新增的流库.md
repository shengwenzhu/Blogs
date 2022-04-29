Java 提供了 `java.util.stream` 包支持对集合中元素的批量操作。

# 1. stream包





+ stream自身不存储数据，将其他数据结构（如数组、集合）作为数据源

  > 产生流的方法：
  >
  > + 集合：通过调用 stream() 和 parallelStream() 方法
  > + 数组：通过调用 `Arrays.stream(Object[])`
  > + stream类的静态方法：`Stream.of(Object[])`，`IntStream.range(int, int)`
  > + 随机数流：Random.ints()

+ 对流的操作不会修改数据源

+ 对流的操作可以分为两种：中间操作（intermediate operations）、末端操作（terminal operations）；

  > 一条 stream pipeline 一般由三部分组成：数据源、零个或多个中间操作、一个终端操作。
  >
  > ```java
  > int n = 100;
  > int[] nums = new int[n];
  > Random random = new Random();
  > for (int i = 0; i < n; i++) {
  >     nums[i] = random.nextInt(100);
  > }
  > int sum = Arrays.stream(nums).filter(x -> x > 60).map(x -> x + 1).sum();
  > System.out.println(sum);
  > ```
  >
  > + 中间操作
  >
  >   产生一条新流；
  >
  >   中间操作一般被实现为 `lazy`，这样能够提高性能，如上代码中filter-map-sum示例，对于流中的每个元素x，在调用filter方法过滤后直接调用map方法，并不需要等待流中所有的元素都执行完filter操作后才执行map操作。这种实现方式也允许在非必要的情况下遍历所有元素，诸如“查找第一个超过 1000 个字符的字符串”之类的操作（当输入流的大小是无限的而不仅仅是大时，这种行为变得更加重要）。
  >
  >   中间操作进一步分为无状态操作和有状态操作。无状态操作（such as filter and map）在处理新元素时不保留先前看到的元素的状态，即每个元素都可以独立于其他元素的操作进行处理；有状态操作（such as distinct and sorted）可能需要在生成结果之前获取到整个输入，例如，对流进行排序。因此在并行计算下，某些包含有状态中间操作的管道可能需要对数据进行多次传递，或者可能需要缓冲数据，仅包含无状态中间操作的管道可以在单次传递中完成数据处理，具有最小的数据缓冲。
  >
  > + 终端操作：
  >
  >   可能会遍历流进而产生一个结果，如 `IntStream.sum`，终端操作执行后，这个流管道被视为消耗，不能再使用。









### 3.    并行Parallelism

除非明确请求并行性，否则JDK中的流实现会创建串行流；

可以通过调用BaseStream.parallel()方法对任何流进行有效并行化；

可以使用BaseStream.isParallel()方法确定流是不是并行模式；

### 4.    接口

​                                                    

### 5.    BaseStream接口

1)    中间方法

l boolean isParallel()

返回此流是否将并行执行；

l S parallel()

返回并行的等效流。可能会返回自己，因为流已经并行，或者因为基础流状态被修改为并行。

l S sequential()

返回顺序的等效流。 可能会返回自己，因为流已经是顺序的，或者因为基础流状态被修改为顺序。

2)    终端方法

l Iterator<T> iterator()

返回此流的元素的迭代器

3)    子接口

DoubleStream，IntStream，LongStream，Stream<T>

### 6.    Stream接口

1)    创建流对象

首先使用static <T> Stream.Builder<T> builder()方法返回一个Stream.builder构造器；

然后可以使用Stream.builder接口的default Stream.Builder<T> add(T t)方法向正在构建的流添加元素。然后使用Stream<T> build()构建流，将此构建器转换为构建状态。

  import  java.util.stream.Stream;  class People  {   public String name;   public int age;   public char sex;   public People(String name, int age, char sex)   {        this.name=name;        this.age=age;        this.sex=sex;   }   public void peopleDesc()   {        System.out.println("姓名："+this.name+"，年龄："+this.age+"，性别："+this.sex);   }  }     public class  test  {   public static void main(String[] args)   {        //创建一个流s        *Stream.Builder  s=Stream.builder();*        s.add(new People("薛秋林", 21, '男'));        s.add(new People("何帅", 22, '男'));        s.add(new People("蒋松", 22, '女'));        *Stream  s2=s.build();*        s2.filter(obj->((People)obj).age==22).forEach(obj->((People)obj).peopleDesc());        //姓名：何帅，年龄：22，性别：男        //姓名：蒋松，年龄：22，性别：女          }  }  

也可以通过Collection接口的default Stream<E> stream()和default Stream<E> parallelStream()创建流对象；

Stream接口也提了很多类方法来创建流：

​     

2)    常用方法

