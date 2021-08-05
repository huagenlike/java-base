1、命令描述
返回或保存给定列表、集合、有序集合key中经过排序的元素。
排序默认以数字作为对象，值被解释为双精度浮点数，然后进行比较。

2、一般sort用法
最简单的sort使用方法是sort key和sort key desc。
sort key：返回键值从小到大排序的结果。
sort key desc：返回键值从大到小排序的结果。

假设price列表保存了今日的物品价格， 那么可以用sort命令对它进行排序：
# 开销金额列表
redis> lpush price 30 1.5 10 8
(integer) 4

# 排序
redis> sort price
1) "1.5"
2) "8"
3) "10"
4) "30"

# 逆序排序
redis 127.0.0.1:6379> sort price desc
1) "30"
2) "10"
3) "8"
4) "1.5"

3、使用alpha修饰符对字符串进行排序
因为sort命令默认排序对象为数字，当需要对字符串进行排序时，需要显式地在sort命令之后添加alpha修饰符。

# 网址
redis> lpush website "www.ceddit.com"
(integer) 1

redis> lpush website "www.hlashdot.com"
(integer) 2

redis> lpush website "www.bnfoq.com"
(integer) 3

# 默认(按数字)排序
redis 127.0.0.1:8888> sort website
1) "www.bnfoq.com"
2) "www.hlashdot.com"
3) "www.ceddit.com"

# 按字符排序
redis 127.0.0.1:8888> sort website alpha
1) "www.bnfoq.com"
2) "www.ceddit.com"
3) "www.hlashdot.com"

redis 127.0.0.1:8888> sort website alpha desc
1) "www.hlashdot.com"
2) "www.ceddit.com"
3) "www.bnfoq.com"

4、使用limit修饰符限制返回结果
排序之后返回元素的数量可以通过limit修饰符进行限制，修饰符接受offset和count两个参数。
offset：指定要跳过的元素数量，即起始位置。
count：指定跳过offset个指定的元素之后，要返回多少个对象。

以下例子返回排序结果的前5个对象(offset为0表示没有元素被跳过)。
# 添加测试数据，列表值为1~10
redis 127.0.0.1:6379> rpush age 1 3 5 7 9
(integer) 5

redis 127.0.0.1:6379> rpush age 2 4 6 8 10
(integer) 10

# 返回列表中最小的5个值
redis 127.0.0.1:6379> sort age limit 0 5
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"

5、使用外部key进行排序
可以使用外部 key 的数据作为权重，代替默认的直接对比键值的方式来进行排序。

假设现在有用户数据如下：


以下代码将数据输入到redis中：
# admin
redis 127.0.0.1:6379> lpush uid 1
(integer) 1

redis 127.0.0.1:6379> set user_name_1 admin
ok

redis 127.0.0.1:6379> set user_level_1 9999
ok

# jack
redis 127.0.0.1:6379> lpush uid 2
(integer) 2

redis 127.0.0.1:6379> set user_name_2 jack
ok

redis 127.0.0.1:6379> set user_level_2 10
ok

# peter
redis 127.0.0.1:6379> lpush uid 3
(integer) 3

redis 127.0.0.1:6379> set user_name_3 peter
ok

redis 127.0.0.1:6379> set user_level_3 25
ok

# mary
redis 127.0.0.1:6379> lpush uid 4
(integer) 4

redis 127.0.0.1:6379> set user_name_4 mary
ok

redis 127.0.0.1:6379> set user_level_4 70
ok

6、by选项
默认情况下， sort uid直接按uid中的值排序：

redis 127.0.0.1:6379> sort uid
1) "1" # admin
2) "2" # jack
3) "3" # peter
4) "4" # mary
通过使用by选项，可以让uid按其他键的元素来排序。

比如说， 以下代码让uid键按照user_level_{uid}的大小来排序：
redis 127.0.0.1:6379> sort uid by user_level_*
1) "2" # jack , level = 10
2) "3" # peter, level = 25
3) "4" # mary, level = 70
4) "1" # admin, level = 9999
user_level_*是一个占位符，它先取出uid中的值，然后再用这个值来查找相应的键。

比如在对uid列表进行排序时，程序就会先取出uid的值1、2、3、4，然后使用user_level_1、user_level_2、user_level_3和 user_level_4的值作为排序uid的权重。

7、get选项
使用get选项，可以根据排序的结果来取出相应的键值。

比如说，以下代码先排序uid，再取出键user_name_{uid}的值：

redis 127.0.0.1:6379> sort uid get user_name_*
1) "admin"
2) "jack"
3) "peter"
4) "mary"
现在的排序结果要比只使用 sort uid by user_level_* 要直观得多。

8、获取多个外部键
可以同时使用多个get选项，获取多个外部键的值。

以下代码就按 uid 分别获取 user_level_{uid} 和 user_name_{uid} ：
redis 127.0.0.1:6379> sort uid get user_level_* get user_name_*
1) "9999" # level
2) "admin" # name
3) "10"
4) "jack"
5) "25"
6) "peter"
7) "70"
8) "mary"

get有一个额外的参数规则，那就是可以用#获取被排序键的值。

以下代码就将 uid 的值、及其相应的 user_level_* 和 user_name_* 都返回为结果：
redis 127.0.0.1:6379> sort uid get # get user_level_* get user_name_*
1) "1" # uid
2) "9999" # level
3) "admin" # name
4) "2"
5) "10"
6) "jack"
7) "3"
8) "25"
9) "peter"
10) "4"
11) "70"
12) "mary"

9、获取外部键，但不进行排序
通过将一个不存在的键作为参数传给 by 选项， 可以让 sort 跳过排序操作，直接返回结果：
redis 127.0.0.1:6379> sort uid by not-exists-key
1) "4"
2) "3"
3) "2"
4) "1"
这种用法在单独使用时，没什么实际用处。

不过，通过将这种用法和get选项配合，就可以在不排序的情况下，获取多个外部键，相当于执行一个整合的获取操作（类似于 sql数据库的join关键字）。

以下代码演示了，如何在不引起排序的情况下，使用sort、by和get获取多个外部键：
redis 127.0.0.1:6379> sort uid by not-exists-key get # get user_level_* get user_name_*
1) "4" # id
2) "70" # level
3) "mary" # name
4) "3"
5) "25"
6) "peter"
7) "2"
8) "10"
9) "jack"
10) "1"
11) "9999"
12) "admin"

10、将哈希表作为get或by的参数

除了可以将字符串键之外， 哈希表也可以作为 get 或 by 选项的参数来使用。
比如说，对于前面给出的用户信息表：
   uid         user_name_{uid}         user_level_{uid}
    1               admin                   9999
    2               jack                    10     
    3               peter                   25
    4               mary                    70
    
我们可以不将用户的名字和级别保存在 user_name_{uid} 和 user_level_{uid} 两个字符串键中， 而是用一个带有 name 域和 level 域的哈希表 user_info_{uid} 来保存用户的名字和级别信息：
redis 127.0.0.1:6379> hmset user_info_1 name admin level 9999
ok

redis 127.0.0.1:6379> hmset user_info_2 name jack level 10
ok

redis 127.0.0.1:6379> hmset user_info_3 name peter level 25
ok

redis 127.0.0.1:6379> hmset user_info_4 name mary level 70
ok

之后， by 和 get 选项都可以用 key->field 的格式来获取哈希表中的域的值， 其中 key 表示哈希表键， 而 field 则表示哈希表的域：

redis 127.0.0.1:6379> sort uid by user_info_*->level
1) "2"
2) "3"
3) "4"
4) "1"

redis 127.0.0.1:6379> sort uid by user_info_*->level get user_info_*->name
1) "jack"
2) "peter"
3) "mary"
4) "admin"

11、保存排序结果
默认情况下， sort 操作只是简单地返回排序结果，并不进行任何保存操作。
通过给 store 选项指定一个 key 参数，可以将排序结果保存到给定的键上。
如果被指定的 key 已存在，那么原有的值将被排序结果覆盖。

# 测试数据
redis 127.0.0.1:6379> rpush numbers 1 3 5 7 9
(integer) 5

redis 127.0.0.1:6379> rpush numbers 2 4 6 8 10
(integer) 10

redis 127.0.0.1:6379> lrange numbers 0 -1
1) "1"
2) "3"
3) "5"
4) "7"
5) "9"
6) "2"
7) "4"
8) "6"
9) "8"
10) "10"

redis 127.0.0.1:6379> sort numbers store sorted-numbers
(integer) 10

# 排序后的结果
redis 127.0.0.1:6379> lrange sorted-numbers 0 -1
1) "1"
2) "2"
3) "3"
4) "4"
5) "5"
6) "6"
7) "7"
8) "8"
9) "9"
10) "10"

13、返回值
没有使用 store 参数，返回列表形式的排序结果。
使用 store 参数，返回排序结果的元素数量。