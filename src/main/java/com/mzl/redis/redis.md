# Redis实战
## 第1章　初识Redis
    Redis拥有两种不同形式的持久化方法，它们都可以用小而紧凑的格式将存储在内存中的数据写入硬盘：
        第一种持久化方法为时间点转储（point-in-time dump），转储操作既可以在“指定时间段内有指定数量的写操作执行”这一条件被满足时执行，又可以通过调用两条转储到硬盘（dump-to-disk）命令中的任何一条来执行；
        第二种持久化方法将所有修改了数据库的命令都写入一个只追加（append-only）文件里面，用户可以根据数据的重要程度，将只追加写入设置为从不同步（sync）、每秒同步一次或者每写入一个命令就同步一次。我们将在第4章中更加深入地讨论这些持久化选项。
### 1.2　Redis数据结构简介
    5种数据结构类型分别为STRING（字符串）、LIST（列表）、SET（集合）、HASH（散列）和ZSET（有序集合）。
    下面是一些常用命令
    String类型
        set key1 world
            新增key1，值为world
        incr key1
            将键存储的值加一，如果key1不存在，则当做0，如果值不能被解释为十进制数或浮点数，则会报错
        decr key1
            将键存储的值减一，如果key1不存在，则当做0，如果值不能被解释为十进制数或浮点数，则会报错
        incrby key1 10
            将键存储的值加上整数10，如果值不能被解释为十进制数或浮点数，则会报错
        decrby key1 20
            将键存储的值减去整数10，如果值不能被解释为十进制数或浮点数，则会报错
        incrbyfloat key1 10.10
            将键存储的值加上浮点数10.10，如果值不能被解释为十进制数或浮点数，则会报错
        get key1
            查询key
        del key1
            删除key
        append key1 world
            将值 world 追加到给定键key1当前存储的值的末尾
        getrange key1 2 4
            获取key1由偏移量start至偏移量end范围内的字符串组成的子串，包括start 和 end在内
        setrange key1 5 w
            命令用指定的字符串覆盖给定 key 所储存的字符串值，覆盖的位置从偏移量 offset（这里对应 5） 开始。
        STRLEN key
            STRLEN命令返回键值的长度，如果键不存在则返回0。UTF-8编码的长度都是3。
        MSET key1 v1 key2 v2 key3 v3
        MGET key1 key3
            MGET/MSET 可以同时获得/设置多个键的键值
        位操作
            GETBIT key offset
            SETBIT key offset value
            BITCOUNT key [start] [end] 命令可以获得字符串类型键中值是1的二进制位个数
            BITOP operation destkey key [key …] 可以对多个字符串类型键进行位运算，并将结果存储在destkey参数指定的键中
            一个字节由8个二进制位组成，Redis提供了4个命令可以直接对二进制位进行操作。
    list 列表类型[linked-list]（值可以重复）
        列表类型内部是使用双向链表（double linked list）实现的，所以向列表两端添加元素的时间复杂度为O(1)，获取越接近两端的元素速度就越快。通过索引访问元素比较慢。
        向列表两端增加元素
            LPUSH key value [value …] 将给定的值推入列表的左端
            RPUSH key value [value …] 将给定的值推入列表的右端
            LPUSH命令用来向列表左边增加元素，返回值表示增加元素后列表的长度。
        从列表两端弹出元素
            LPOP key 列表左边弹出一个值，并返回弹出的值
            RPOP key 列表右边弹出一个值，并返回弹出的值
            有进有出，LPOP命令可以从列表左边弹出一个元素。LPOP命令执行两步操作：第一步是将列表左边的元素从列表中移除，第二步是返回被移除的元素值。
        获取列表中元素的个数
            LLEN key
            当键不存在时LLEN会返回0
        获得列表片段
            LRANGE key start stop
            LRANGE命令是列表类型最常用的命令之一，它能够获得列表中的某一片段。
            LRANGE命令也支持负索引，表示从右边开始计算序数，如"−1"表示最右边第一个元素，"-2"表示最右边第二个元素，依次类推
        删除列表中指定的值
            LREM key count value
            LREM命令会删除列表中前count个值为value的元素，返回值是实际删除的元素个数。根据count值的不同，LREM命令的执行方式会略有差异。
            （1）当 count > 0时 LREM 命令会从列表左边开始删除前 count 个值为 value的元素。
            （2）当 count < 0时 LREM 命令会从列表右边开始删除前|count|个值为 value 的元素。
            （3）当 count = 0是 LREM命令会删除所有值为 value的元素。
        获得/设置指定索引的元素值
            LINDEX key index
            LSET key index value
            如果要将列表类型当作数组来用，LINDEX命令是必不可少的。LINDEX命令用来返回指定索引的元素，索引从0开始
        只保留列表指定片段
            LTRIM key start end
            LTRIM 命令可以删除指定索引范围之外的所有元素，其指定列表范围的方法和LRANGE命令相同。
        向列表中插入元素
            LINSERT key BEFORE|AFTER pivot value
            LINSERT 命令首先会在列表中从左到右查找值为 pivot 的元素，然后根据第二个参数是BEFORE还是AFTER来决定将value插入到该元素的前面还是后面。
            LINSERT命令的返回值是插入后列表的元素个数。
        将元素从一个列表转到另一个列表
            RPOPLPUSH source destination
            RPOPLPUSH是个很有意思的命令，从名字就可以看出它的功能：先执行RPOP命令再执行LPUSH命令。RPOPLPUSH命令会先从source列表类型键的右边弹出一个元素，然后将其加入到destination列表类型键的左边，并返回这个元素的值，整个过程是原子的。
    set集合（值不可重复）
        增加/删除元素
            SADD key member [member …] 命令用来向集合中增加一个或多个元素，如果键不存在则会自动创建。
            SREM key member [member …] SREM命令用来从集合中删除一个或多个元素，并返回删除成功的个数
        获得集合中的所有元素
            SMEMBERS key 命令会返回集合中的所有元素
        判断元素是否在集合中
            SISMEMBER key member
            判断一个元素是否在集合中是一个时间复杂度为O(1)的操作，无论集合中有多少个元素，SISMEMBER命令始终可以极快地返回结果。当值存在时 SISMEMBER命令返回1，当值不存在或键不存在时返回0
        集合间运算
            SDIFF key [key „] SDIFF命令用来对多个集合执行差集运算。集合A与集合B的差集表示为A−B，代表所有属于A且不属于B的元素构成的集合（如图3-13所示），即A−B ={x | x∈A且x∈B}。SDIFF setA setB setC，计算顺序是先计算 setA - setB，再计算结果与 setC的差集
            SINTER key [key „] SINTER命令用来对多个集合执行交集运算。集合A与集合B的交集表示为A ∩ B，代表所有属于A 且属于B的元素构成的集合（如图3-14所示），即A ∩ B ={x | x ∈ A 且x ∈B}。
            SUNION key [key „] SUNION命令用来对多个集合执行并集运算。集合A与集合B的并集表示为A∪B，代表所有属于A 或属于B的元素构成的集合（如图3-15所示）即A∪B ={x | x∈A或x ∈B}。
            示例：
                local:0>sadd post:1:tags java
                "1"
                local:0>sadd post:2:tags java mysql
                "2"
                local:0>sadd post:3:tags java mysql redis
                "3"
                local:0>sadd tag:redis:posts 3
                "1"
                local:0>sadd tag:mysql:posts 2 3
                "2"
                local:0>sadd tag:java:posts 1 2 3
            当需要获取标记“MySQL”标签的文章时只需要使用命令 SMEMBERS tag：MySQL：posts即可。
            如果要实现找到同时属于Java、MySQL和Redis 3 个标签的文章，只需要将tag：Java：posts、tag：MySQL：posts和tag：Redis：posts这3个键取交集，借助SINTER命令即可轻松完成。（sinter tag:java:posts tag:mysql:posts tag:redis:posts）
        获得集合中元素个数
            SCARD key SCARD命令用来获得集合中的元素个数
        进行集合运算并将结果存储
            SDIFFSTORE destination key [key …]
            SINTERSTORE destination key [key …]
            SUNIONSTORE destination key [key …]
            SDIFFSTORE命令和SDIFF命令功能一样，唯一的区别就是前者不会直接返回运算结果，而是将结果存储在destination键中。
            SDIFFSTORE命令常用于需要进行多步集合运算的场景中，如需要先计算差集再将结果和其他键计算交集。
            SINTERSTORE和SUNIONSTORE命令与之类似，不再赘述。   
        随机获得集合中的元素
            SRANDMEMBER key [count]
            SRANDMEMBER命令用来随机从集合中获取一个元素，
            （1）当count为正数时，SRANDMEMBER会随机从集合里获得count个不重复的元素。如果count的值大于集合中的元素个数，则SRANDMEMBER会返回集合中的全部元素。
            （2）当count为负数时，SRANDMEMBER会随机从集合里获得|count|个的元素，这些元素有可能相同。
        从集合中弹出一个元素
            SPOP key  从集合中随机选择一个元素弹出
    HASH（散列，key不可重复）
        赋值与取值 文章发布
            HSET key field value  往散列里面关联起给定的键值对，新增时返回1，更新时返回0。
            HGET key field  获取制定散列的键的值
            HMSET key field value [field value …]
            HMGET key field [field …]
            HGETALL key  获取散列包含的所有键值对
            HSET命令用来给字段赋值，而HGET命令用来获得字段的值。
        判断字段是否存在
            HEXISTS key field
            HEXISTS命令用来判断一个字段是否存在。如果存在则返回1，否则返回0（如果键不存在也会返回0）。
        当字段不存在时赋值
            HSETNX key field value
            HSETNX 命令与HSET命令类似，区别在于如果字段已经存在，HSETNX命令将不执行任何操作。HSETNX命令是原子操作，不用担心竞态条件。
        增加数字
            HINCRBY key field increment
            上一节的命令拾遗部分介绍了字符串类型的命令INCRBY，HINCRBY命令与之类似，可以使字段值增加指定的整数。
            HINCRBY命令的示例如下：
                redis> HINCRBY person score 60
                (integer) 60
                之前person键不存在，HINCRBY命令会自动建立该键并默认score字段在执行命令前的值为“0”。命令的返回值是增值后的字段值。
        删除字段
            HDEL key field [field …]
            HDEL命令可以删除一个或多个字段，返回值是被删除的字段个数
        只获取字段名或字段值
            HKEYS key
            HVALS key
        获得字段数量
            HLEN key
    ZSET（有序集合）
        有序集合类型在某些方面和列表类型有些相似。
            （1）二者都是有序的。
            （2）二者都可以获得某一范围的元素。
        但是二者有着很大的区别，这使得它们的应用场景也是不同的。
            （1）列表类型是通过链表实现的，获取靠近两端的数据速度极快，而当元素增多后，访问中间数据的速度会较慢，所以它更加适合实现如“新鲜事”或“日志”这样很少访问中间元素的应用。
            （2）有序集合类型是使用散列表和跳跃表（Skip list）实现的，所以即使读取位于中间部分的数据速度也很快（时间复杂度是O(log(N))）。
            （3）列表中不能简单地调整某个元素的位置，但是有序集合可以（通过更改这个元素的分数）。
            （4）有序集合要比列表类型更耗费内存。
        增加元素
            ZADD key score member [score member …]
            ZADD 命令用来向有序集合中加入一个元素和该元素的分数，如果该元素已经存在则会用新的分数替换原有的分数。ZADD命令的返回值是新加入到集合中的元素个数（不包含之前已经存在的元素）。
        获得元素的分数
            ZSCORE key member
        获得排名在某个范围的元素列表
            ZRANGE key start stop [WITHSCORES] 从小到大
            ZREVRANGE key start stop [WITHSCORES]  ZREVRANGE命令和ZRANGE的唯一不同在于ZREVRANGE命令是按照元素分数从大到小的顺序给出结果的。
            ZRANGE命令会按照元素分数从小到大的顺序返回索引从 start到stop之间的所有元素（包含两端的元素）。ZRANGE命令与LRANGE命令十分相似，如索引都是从0开始，负数代表从后向前查找（−1表示最后一个元素）。
            如果需要同时获得元素的分数的话可以在 ZRANGE 命令的尾部加上 WITHSCORES 参数，这时返回的数据格式就从“元素1, 元素2, „, 元素n”变为了“元素1, 分数1, 元素2, 分数2, „, 元素n, 分数n”
            ZRANGE命令的时间复杂度为O(log n+m)（其中n为有序集合的基数，m为返回的元素个数）。
            如果两个元素的分数相同，Redis会按照字典顺序（即"0"<"9"<"A"<"Z"<"a"<"z"这样的顺序）来进行排列。再进一步，如果元素的值是中文怎么处理呢？答案是取决于中文的编码方式，如使用UTF-8编码
        获得指定分数范围的元素
            ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
            ZRANGEBYSCORE 命令参数虽然多，但是都很好理解。该命令按照元素分数从小到大的顺序返回分数在min和max之间（包含min和max）的元素
            想获得分数高于60分的从第二个人开始的3个人：            
                zrangebyscore scoreboard 60 +inf limit 1 3
            想获取分数低于或等于 100 分的前 3 个人怎么办呢？
                ZREVRANGEBYSCORE scoreboard 100 0 LIMIT 0 3
        获得集合中元素的数量
            ZCARD key
        获得指定分数范围内的元素个数
            ZCOUNT key min max
            ZCOUNT命令的min和max参数的特性与ZRANGEBYSCORE命令中的一样：
        删除一个或多个元素
            ZREM key member [member …]
            ZREM命令的返回值是成功删除的元素数量（不包含本来就不存在的元素）。
        按照排名范围删除元素
            ZREMRANGEBYRANK key start stop
            ZREMRANGEBYRANK 命令按照元素分数从小到大的顺序（即索引 0表示最小的值）删除处在指定排名范围内的所有元素，并返回删除的元素数量。如：
        按照分数范围删除元素
           ZREMRANGEBYSCORE key min max
           ZREMRANGEBYSCORE命令会删除指定分数范围内的所有元素，参数min和max的特性和ZRANGEBYSCORE命令中的一样。返回值是删除的元素数量。如：
        获得元素的排名
           ZRANK key member
           ZREVRANK key member
           ZRANK命令会按照元素分数从小到大的顺序获得指定的元素的排名（从0开始，即分数最小的元素排名为0）。如：
           ZREVRANK命令则相反（分数最大的元素排名为0）：
        计算有序集合的交集
            ZINTERSTORE destination numkeys key [key …] [WEIGHTS weight [weight …]] [AGGREGATE SUM|MIN|MAX]
            ZINTERSTORE命令用来计算多个有序集合的交集并将结果存储在destination键中（同样以有序集合类型存储），返回值为destination键中的元素个数。
            destination键中元素的分数是由AGGREGATE参数决定的。
                （1）当AGGREGATE是SUM时（也就是默认值），destination键中元素的分数是每个参与计算的集合中该元素分数的和。例如：ZINTERSTORE sortedSetsResult 2 sortedSets1 sortedSets2
                （2）当AGGREGATE是MIN时，destination键中元素的分数是每个参与计算的集合中该元素分数的最小值。例如：ZINTERSTORE sortedSetsResult 2 sortedSets1 sortedSets2 AGGREGATE MIN
                （3）当AGGREGATE是MAX时，destination键中元素的分数是每个参与计算的集合中该元素分数的最大值。例如：ZINTERSTORE sortedSetsResult 2 sortedSets1 sortedSets2 AGGREGATE MAX
            ZINTERSTORE命令还能够通过WEIGHTS参数设置每个集合的权重，每个集合在参与计算时元素的分数会被乘上该集合的权重。
                ZINTERSTORE sortedSetsResult 2 sortedSets1 sortedSets2 WEIGHTS 1 0.1
            另外还有一个命令与ZINTERSTORE命令的用法一样，名为ZUNIONSTORE，它的作用是计算集合间的并集，这里不再赘述。
        有序集合类型算得上是Redis的5种数据类型中最高级的类型了，在学习时可以与列表类型和集合类型对照理解。
        有序集合的键被称为成员（member），每个成员都是独一无二的；而有序集合的值则被称为分值（score），分值必须为浮点数。
        根据元素在有序排列中所处的位置，从有序集合里面获取多个元素 zrange zset-key 0 -1 withscores
        获取有序集合在给定分值范围内的所有元素 zrangebyscore zset-key 0 800 withscores
        如果给定成员存在于有序集合，那么移除这个成员 zrem zset-key member1
### 事务
    简单示例
        local:0>multi
        "OK"
        local:0>sadd user:1:following 2
        "QUEUED"
        local:0>sadd user:2:followers 1
        "QUEUED"
        local:0>exec
    Redis保证一个事务中的所有命令要么都执行，要么都不执行。如果在发送EXEC命令前客户端断线了，则 Redis 会清空事务队列，事务中的所有命令都不会执行。而一旦客户端发送了EXEC命令，所有的命令就都会被执行，即使此后客户端断线也没关系，因为Redis中已经记录了所有要执行的命令。
    Redis 的事务还能保证一个事务内的命令依次执行而不被其他命令插入。
#### 错误处理
    （1）语法错误。语法错误指命令不存在或者命令参数的个数不对。  
        跟在MULTI命令后执行了3个命令：一个是正确的命令，成功地加入事务队列；其余两个命令都有语法错误。而只要有一个命令有语法错误，执行 EXEC 命令后 Redis 就会直接返回错误，连语法正确的命令也不会执行。
    （2）运行错误。运行错误指在命令执行时出现的错误，比如使用散列类型的命令操作集合类型的键，这种错误在实际执行之前 Redis 是无法发现的，所以在事务里这样的命令是会被 Redis 接受并执行的。如果事务里的一条命令出现了运行错误，事务里其他的命令依然会继续执行（包括出错命令之后的命令）
    Redis的事务没有关系数据库事务提供的回滚（rollback） 功能。为此开发者必须在事务执行出错后自己收拾剩下的摊子（将数据库复原回事务执行前的状态等）。
    不过由于 Redis 不支持回滚功能，也使得 Redis 在事务上可以保持简洁和快速。另外回顾刚才提到的会导致事务执行失败的两种错误，其中语法错误完全可以在开发时找出并解决，另外如果能够很好地规划数据库（保证键名规范等）的使用，是不会出现如命令与数据类型不匹配这样的运行错误的。
#### WATCH命令介绍
    WATCH 命令可以监控一个或多个键，一旦其中有一个键被修改（或删除），之后的事务就不会执行。监控一直持续到 EXEC 命令（事务中的命令是在 EXEC 之后才执行的，所以在 MULTI 命令后可以修改WATCH监控的键值）
    执行 EXEC 命令后会取消对所有键的监控，如果不想执行事务中的命令也可以使用UNWATCH命令来取消监控。
### 过期时间
#### 命令介绍
    在Redis中可以使用 EXPIRE命令设置一个键的过期时间，到时间后Redis会自动删除它。
    EXPIRE key seconds，其中 seconds 参数表示键的过期时间，单位是秒。EXPIRE命令返回1表示设置成功，返回0则表示键不存在或设置失败。
    PEXPIRE key millisecond，PEXPIRE命令与 EXPIRE的唯一区别是前者的时间单位是毫秒，即 PEXPIRE key 1000 与 EXPIRE key 1 等价。对应地可以用 PTTL命令以毫秒为单位返回键的剩余时间。
    TTL key，返回值是键的剩余时间（单位是秒）。当键不存在时TTL命令会返回−2。没有设置过期时间，返回−1。
    PTTL key，返回值是键的剩余时间（单位是毫秒）。当键不存在时TTL命令会返回−2。没有设置过期时间，返回−1。
    PERSIST key，取消键的过期时间设置（即将键恢复成永久的）
    EXPIREAT和PEXPIREAT，EXPIREAT命令与EXPIRE命令的差别在于前者使用Unix时间作为第二个参数表示键的过期时刻。PEXPIREAT命令与EXPIREAT命令的区别是前者的时间单位是毫秒。
        示例：EXPIREAT foo 1351858600
    如果使用 WATCH命令监测了一个拥有过期时间的键，该键时间到期自动删除并不会被WATCH命令认为该键被改变。
#### 实现缓存
    redis提供6中数据淘汰策略，可在redis.conf中配置：maxmemory-policy noeviction
        noeviction：禁止驱逐数据。默认配置都是这个。当内存使用达到阀值的时候，所有引起申请内存的命令都会报错。
        volatile-lru：从设置了过期时间的数据集中挑选最近最少使用的数据淘汰。
        volatile-ttl：从已设置了过期时间的数据集中挑选即将要过期的数据淘汰。
        volatile-random：从已设置了过期时间的数据集中任意选择数据淘汰。
        allkeys-lru：从数据集中挑选最近最少使用的数据淘汰。
        allkeys-random：从数据集中任意选择数据淘汰。
    当Redis确定好要驱逐某个键值对后，会删除这个数据，并将这个数据变更消息同步到本地和从机。
### 排序
#### SORT命令
    可以借助 Redis 提供的 SORT命令来解决小白的问题。
    SORT命令可以对列表类型、集合类型和有序集合类型键进行排序，并且可以完成与关系数据库中的连接查询相类似的任务。
    集合类型
        sadd tag:ruby:posts 2 6 12 26
        SORT tag：ruby：posts
    列表类型
        redis> LPUSH mylist 4 2 6 1 3 7
        redis> SORT mylist
    对有序集合类型排序时会忽略元素的分数，只针对元素自身的值进行排序。
        redis> ZADD myzset 50 2 40 3 20 1 60 5
        redis> SORT myzset
    除了可以排列数字外，SORT命令还可以通过ALPHA参数实现按照字典顺序排列非数字元素，就像这样：    
        redis> LPUSH mylistalpha a c e d B C A
        redis> SORT mylistalpha ALPHA
        如果没有加ALPHA参数的话，SORT命令会尝试将所有元素转换成双精度浮点数来比较，如果无法转换则会提示错误。
    SORT命令的DESC参数可以实现将元素按照从大到小的顺序排列：
        redis> SORT tag：ruby：posts DESC
    SORT命令还支持LIMIT参数来返回指定范围的结果。用法和 SQL 语句一样，LIMIT offset count，表示跳过前 offset 个元素并获取之后的count个元素。
        SORT命令的参数可以组合使用，像这样：
        redis> SORT tag：ruby：posts DESC LIMIT 1 2
#### BY参数
    如果提供了 BY 参数，SORT 命令将不再依据元素自身的值进行排序，而是对每个元素使用元素的值替换参考键中的第一个“*”并获取其值，然后依据该值对元素排序。
        sort tag:ruby:posts by post:*->time desc
    除了散列类型之外，参考键还可以是字符串类型，比如
        127.0.0.1:6379> lpush sortbylist 2 1 3
        127.0.0.1:6379> set itemscore:1 50
        127.0.0.1:6379> set itemscore:2 100
        127.0.0.1:6379> set itemscore:3 -10
        127.0.0.1:6379> sort sortbylist by itemscore:* desc
        1) "2"
        2) "1"
        3) "3"
        127.0.0.1:6379> sort sortbylist by itemscore:*
        1) "3"
        2) "1"
        3) "2"
    当参考键名不包含“*”时（即常量键名，与元素值无关），SORT 命令将不会执行排序操作，因为Redis认为这种情况是没有意义的（因为所有要比较的值都一样）。
    redis> SORT sortbylist BY anytext
        例子中 anytext 是常量键名（甚至 anytext 键可以不存在），此时 SORT 的结果与LRANGE的结果相同，没有执行排序操作。在不需要排序但需要借助SORT命令获得与元素相关联的数据时（见4.3.4节），常量键名是很有用的。
    redis> LPUSH sortbylist 4
    redis> SET itemscore：4 50
    redis> SORT sortbylist BY itemscore：* DESC
        示例中元素"4"的参考键itemscore：4的值和元素"1"的参考键itemscore：1的值都是50，所以SORT命令会再比较"4"和"1"元素本身的大小来决定二者的顺序。
    redis> LPUSH sortbylist 5
    redis> SORT sortbylist BY itemscore：* DESC
    1) "2"
    2) "4"
    3) "1"
    4) "5"
    5) "3"
        当某个元素的参考键不存在时，会默认参考键的值为0，上例中"5"排在了"3"的前面，是因为"5"的参考键不存在，所以默认为 0，而"3"的参考键值为−10。
    参考键虽然支持散列类型，但是“*”只能在“->”符号前面（即键名部分）才有用，在“->”后（即字段名部分）会被当成字段名本身而不会作为占位符被元素的值替换，即常量键名。但是实际运行时会发现一个有趣的结果：
        redis> SORT sortbylist BY somekey->somefield：*
        1) "1"
        2) "2"
        3) "3"
        4) "4"
        5) "5"
        上面提到了当参考键名是常量键名时 SORT 命令将不会执行排序操作，然而上例中确进行了排序，而且只是对元素本身进行排序。这是因为 Redis 判断参考键名是不是常量键名的方式是判断参考键名中是否包含“*”，而 somekey->somefield：*中包含“*”所以不是常量键名。所以在排序的时候Redis对每个元素都会读取键somekey中的 somefield：*字段（“*”不会被替换），无论能否获得其值，每个元素的参考键值是相同的，所以Redis会按照元素本身的大小排列。
#### GET参数
    GET参数不影响排序，它的作用是使 SORT命令的返回结果不再是元素自身的值，而是GET参数中指定的键值。
    GET参数的规则和BY参数一样，GET参数也支持字符串类型和散列类型的键，并使用“*”作为占位符。要实现在排序后直接返回ID对应的文章标题，
    sort tag:ruby:posts by post:*->time desc get post:*->title
        在排序后直接返回ID对应的文章标题
    sort tag:ruby:posts by post:*->time desc get post:*->title get post:*->time
        在一个SORT命令中可以有多个GET参数（而BY参数只能有一个），所以还可以这样用
    SORT命令的时间复杂度是O(n+mlog(m))，其中n表示要排序的列表（集合或有序集合）中的元素个数，m表示要返回的元素个数。
    所以开发中使用SORT命令时需要注意以下几点。
        （1）尽可能减少待排序键中元素的数量（使N尽可能小）。
        （2）使用LIMIT参数只获取需要的数据（使M尽可能小）。
        （3）如果要排序的数据数量较大，尽可能使用STORE参数将结果缓存。
### 消息通知
    
## 第2章　使用Redis构建Web应用



