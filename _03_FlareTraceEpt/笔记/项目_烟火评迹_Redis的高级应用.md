# 项目：烟火评迹



## 功能模块1：短信登陆

##### （1）基于session实现短信登录

![image-20250804143804966](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250804143804966.png)



Tomcat集群中，session的共享问题：

​	问题：当我们使用nginx负载均衡了多台tomcat服务器，每台服务器都有各自独立的session域，当本次用户登录时分配到了tomcatA，将登录信息存到A服务器中；若该用户再次访问tomcat集群，可能轮询分配到tomcatB，该B服务器的session域中不包含当前用户的登录信息，造成共享问题。

​	解决方案：使用redis来存储用户的登录信息，tomcat集群的每台服务器都从redis获取信息，不会遇到session的共享问题。

![image-20250804170801746](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250804170801746.png)



##### （2）基于Redis实现短信登录

![image-20250804185939655](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250804185939655.png)

![image-20250804191719286](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250804191719286.png)

如上图：

​	问：如何使用 token + redis 存储用户登录信息？

​	答：①当用户登录后，在后端生成随机字符串token作为key，将user对象作为value，存入redis中。

​			②将tomcat中生成的token传递给前端，由前端进行存贮；之后每次前端发送请求时，都携带token，后端就能在redis中找到该token对应的用户登录信息user。



注意：session实现登录认证，与redis实现登录认证，有诸多共同点！

​	①cookie类比token，redis类比session，二者的运行流程是类似的。

​	②cookie和token，都是在后端生成，最终存储在前端，每次前端请求后端时都将其携带。

​	③redis通过token作为key，获取对应的用户信息value；而服务器通过cookie中存放的session的id，找到对应的session空间。

​	④二者的区别在于：cookie由浏览器自动管理，而token由前端程序员手段管理。



##### （3）登录拦截器的优化

![image-20250804220231760](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250804220231760.png)

后端redis中，键值对 [k:token - v:user登录信息] 的有效期：首次创建30分钟后过期，每次请求刷新过期时间！

​	-- 单层拦截器缺点：仅在访问需要拦截的路径时刷新过期时间，当用户访问放行路径时（不进入拦截器）不刷新过期时间。

​	-- 双层拦截器：对于所有请求，都刷新token的过期时间。





## 功能模块2：商户查询缓存

##### （1）缓存概念

![image-20250805085132522](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805085132522.png)

![image-20250805085525342](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805085525342.png)



##### （2）添加商户缓存

缓存执行流程如下：

![image-20250805094718381](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805094718381.png)



##### （3）缓存的更新

①缓存更新的几种策略如下：

![image-20250805103235868](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805103235868.png)

②主动更新缓存的几种策略如下：

![image-20250805103908407](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805103908407.png)

③手动更新缓存的细节：

![image-20250805105433907](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805105433907.png)

多线程并发执行时，缓存更新的安全问题：

如下分析过程，虽然二者都存在安全问题，但是 [先更新数据库，再删除缓存] 出现问题的概率更低。无论使用哪种，都要添加超时时间，定期清除缓存数据，减少问题的发生。

![image-20250805110445137](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805110445137.png)

④总结：

![image-20250805111822244](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805111822244.png)



##### （4）缓存穿透

缓存穿透的概念及解决方案：

![image-20250805134208759](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805134208759.png)

如下：使用“缓存空对象”来解决缓存穿透

![image-20250805141047154](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805141047154.png)



##### （5）缓存雪崩

缓存雪崩的概念及解决方案：

![image-20250805150318617](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805150318617.png)



##### （6）缓存击穿

①缓存击穿的概念：

![image-20250805152637000](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805152637000.png)

缓存击穿的解决方案（互斥锁 / 逻辑过期）：

![image-20250805154747035](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805154747035.png)

![image-20250805155955742](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805155955742.png)



②基于“互斥锁”解决缓存击穿问题

![image-20250805162602025](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805162602025.png)



③基于“逻辑过期”解决缓存击穿问题

![image-20250805193633954](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250805193633954.png)



##### （7）封装Redis缓存相关方法的工具类

![image-20250806090254549](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806090254549.png)





## 功能模块3：优惠券秒杀

##### （1）全局唯一ID

![image-20250806145722565](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806145722565.png)

全局ID生成策略：

![image-20250806150639268](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806150639268.png)



##### (2)实现秒杀下单

秒杀下单：购买秒杀优惠券，生成订单

![image-20250806173232770](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806173232770.png)



##### （3）库存超卖问题

①超卖问题：多线程并发的安全问题

例如：秒杀执行流程为Ⅰ查询库存，Ⅱ扣减库存；若原本库存为1，10000个线程同时并发查询库存，每个线程查询的库存结果都为1，则10000个线程都成功通过。之后10000个线程均要扣减库存，库存最终为-9999。

![image-20250806190404504](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806190404504.png)

超卖问题的解决方案：加锁（悲观锁 / 乐观锁）

![image-20250806192350768](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806192350768.png)



②乐观锁的实现方式

​	<乐观锁实现方式1>：**版本号法**

​	版本号法乐观锁：核心【sql语句的原子性】

​	实现方式：当执行查询操作时，记录版本号version=1。之后进行更新语句时，update ... set ..., version=version+1 where ... and  version=1。由于sql语句具有原子性，因此本条update语句执行的过程中，不会被其它并发的线程所影响。因此，若10000条线程并发执行时，仅有首条执行update语句的线程，其version版本号先后一致，成功执行了update语句。当之后的9999条线程执行update语句执行时，where条件均不成立。保证了10000条线程中，只有最先执行update语句的线程执行成功，避免了线程安全问题！

​	（p.s. 个人理解：假设有10000条线程，100个库存。当10000条线程并发查询库存，发现存量为100，版本为1，10000条线程均成功通过。之后10000条线程争抢update语句的执行权，首条争抢到update语句的线程，其版本号正确，update语句执行成功，修改版本号为2；而之后执行update语句的线程，其版本号已经落后，均执行失败。整个过程中，虽然“乐观锁”不会显式的加锁，但是由于sql语句的原子性，这条sql语句+版本号被视为了一个锁）

![image-20250806195034802](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806195034802.png)



<乐观锁实现方式2>：**CAS法**

​	CAS法，即Compare-And-Set（比较并设置）。CAS法的原理，与版本号法完全一致！

​	如下案例中，使用库存数量代替了版本号。执行update语句时，在where条件中判断库存数量是否变动，若未变动，则表示数据未被其它并发的线程改动，则成功执行update语句。

![image-20250806200219229](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806200219229.png)

![image-20250806223012755](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250806223012755.png)



##### （4）实现一人一单

①一人一单：同一个用户最多只能购买一个相同的秒杀券。

![image-20250807090722130](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807090722130.png)

​	线程安全问题：在多线程并发访问的情况下，若数据库中不存在用户A的订单。假设10000个用户A同时查询数据库，查询订单均不存在，这10000个用户A的请求都可以购买订单，造成一人多单。

​	解决方案：悲观锁（insert用悲观锁；update用乐观锁）



②在分布式模式下，synchronized互斥锁失效：

​	知识点补充：**一台tomcat对应一个JVM，synchronized互斥锁仅在单个JVM中生效，不能跨域，是单机锁。**

​	分布式模式下，synchronized互斥锁失效原因：分布式模式，有多台tomcat，对应多个JVM，synchronized互斥锁的作用域只是一台JVM，不能跨域。因此，当分布式模式中，同一批请求被负载均衡到多台tomcat，不同服务器间，争抢到锁的线程仍会并发执行，造成线程安全问题！

![image-20250807132414223](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807132414223.png)

         [负载均衡器]
         /      |     \
       Tomcat1 Tomcat2 Tomcat3   ← 3个独立的JVM
       (JVM1)  (JVM2)  (JVM3)
         |      |      |
        线程A   线程B   线程C     ← 同一用户的请求被分散到不同JVM
        🔒      🔒      🔒       ← 各JVM的锁互不干扰 → 锁失效






## 功能模块4：分布式锁

##### （1）分布式锁 -- 基本概念

分布式锁实现原理：提供一个分布式中不同tomcat间的线程都可见的互斥的锁。

![image-20250807142440317](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807142440317.png)

![image-20250807143007306](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807143007306.png)

不同实现的分布式锁的对比：

![image-20250807144246753](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807144246753.png)



##### （2）基于Redis实现分布式锁

利用Redis的SETNX指令，实现分布式锁：

​	为什么SETNX可以用于分布式锁？	①原子性：SETNX命令是原子操作，多个客户端同时发送SETNX命令时，Redis会确保只有一个客户端能设置成功。	②共享存储：Redis是一个集中式的存储服务，所有客户端（无论来自哪个Tomcat或JVM）都可以访问同一个Redis实例（或集群），因此可以实现跨JVM的锁。

![image-20250807151159906](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807151159906.png)



##### （3）Redis分布式锁的误删问题

**误删其他客户端的锁**：

   \- 如果客户端A在执行业务逻辑时耗时过长，导致锁过期自动释放。此时客户端B获取到锁，然后客户端A执行完逻辑后，又去删除锁，就会误删客户端B的锁。

   \- **解决方案**：在删除锁时，先判断锁的值是否是自己设置的（使用随机字符串作为值），确保只能删除自己的锁。

如下，分布式锁的误删问题流程：

![image-20250807201310939](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807201310939.png)

如下，为解决误删问题后的正确流程：

![image-20250807201611159](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807201611159.png)

![image-20250807203821556](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807203821556.png)

改进Redis的分布式锁，解决误删问题：

![image-20250807222321037](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250807222321037.png)



##### （4）Redis分布式锁的原子性问题，与Lua脚本

①Redis分布式锁的原子性问题

​	分布式锁的原子性问题：释放锁的步骤分为两步【①若判断锁中的值是自己设置的，②删除锁】。若原本客户端A将要释放锁A，当执行完步骤①后，A线程阻塞，阻塞期间A锁超时过期。此时客户端B获取到锁B，一段时间后客户端A阻塞结束，继续执行步骤②，将锁B误删除。

​	问题原因：步骤①、②不具有原子性。

​	解决方案：使用Lua脚本。

![image-20250808092607810](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808092607810.png)



②Lua脚本介绍

Lua脚本语法如下：

![image-20250808101344277](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808101344277.png)

![image-20250808102851992](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808102851992.png)



③使用Lua脚本，解决分布式锁的原子性问题

注意：同一个Lua脚本中的多条Redis语句，执行时具有原子性。

释放锁的Lua脚本：

![image-20250808104954622](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808104954622.png)

使用Java的RedisTemplate执行Lua脚本：

![image-20250808112008776](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808112008776.png)



##### （5）Redisson

①Redisson基本概念

​	Redisson是一个基于Redis的分布式开发工具包。

​	Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid），它提供了分布式和可扩展的Java数据结构，以及许多分布式服务。它支持同步、异步和反应式编程模型。Redisson的目标是让开发者能够以最小的学习曲线使用Redis，同时提供强大的功能。

![image-20250808135632041](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808135632041.png)

补充：基础Redis锁存在的问题（Redisson会对这些问题做出优化）

![image-20250808191218705](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808191218705.png)



②Redisson代码入门

![image-20250808141833757](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808141833757.png)

![image-20250808144646034](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808144646034.png)



③**Redisson的可重入锁**

​	Redisson的可重入锁（Reentrant Lock）是Redisson提供的一种分布式可重入互斥锁，它允许同一个线程多次获取同一把锁，而不会导致死锁。这种锁是Redisson中最常用的锁之一，适用于需要严格控制共享资源访问的分布式场景。

​	核心特性：

​		1.**可重入性**：同一个线程可以多次获取同一把锁，每次获取锁后需要相应次数的释放才能完全释放锁。

​		2.**锁续期（Watchdog）**：锁默认的租约时间为30秒，但通过看门狗机制，如果业务未执行完，会自动续期锁，避免业务执行时间过长导致锁自动释放。

​		3.**公平锁与非公平锁**：Redisson提供了公平锁和非公平锁两种模式。

​		4.**锁释放校验**：释放锁时会检查当前线程是否持有锁，避免误删其他线程的锁。

​		5.**高可用**：支持Redis单节点、主从、哨兵、集群等多种部署模式。

![image-20250808162751048](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808162751048.png)

--获取锁的Lua脚本

![image-20250808163741191](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808163741191.png)

--释放锁的Lua脚本

![image-20250808163858436](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808163858436.png)



④**Redisson的锁重试和看门狗机制**

​	1.等待时间（Wait Time）

​		--定义：尝试获取锁的最大等待时间。如果在这段时间内未能获取锁，则放弃获取。

​		--行为：在等待时间内，客户端会周期性地尝试获取锁（内部有重试机制，重试间隔由配置决定）。如果在等待时间内获取到锁，则返回`true`；如果超时仍未获取，则返回`false`。

​	2.过期时间（Lease Time / Expiration Time）

​		--定义：锁被自动释放的时间。即获取锁后，即使没有显式释放，锁也会在这个时间后自动过期，防止死锁。

​		--注意：在Redisson中，过期时间也称为租约时间（lease time）。

​		--与看门狗的关系：如果在获取锁时**指定了租约时间**（lease time），则锁不会自动续期，到期自动释放；如果在获取锁时**没有指定租约时间**（即使用`lock()`或`tryLock()`不带租约时间参数），则Redisson会启动一个“看门狗”（Watchdog）线程来定期续期锁，默认续期时间为30秒，并且每10秒续期一次（即每次将锁的过期时间重置为30秒）。

![image-20250808205442740](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808205442740.png)

![image-20250808210023561](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808210023561.png)

总结不同的Redis分布式锁：

![image-20250808222238256](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250808222238256.png)





## 功能模块5：秒杀优化

##### （1）异步秒杀

①异步秒杀 -- 思路

优化前的“优惠券秒杀”流程如下：

![image-20250810125950885](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250810125950885.png)

优化后的“优惠券秒杀”流程如下：

![image-20250810133139681](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250810133139681.png)



②异步秒杀 -- 秒杀资格的判断

使用Redis优化秒杀资格的判断：

![image-20250810134703955](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250810134703955.png)

![image-20250810142039528](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250810142039528.png)

异步秒杀优化的总结：

![image-20250810204905527](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20250810204905527.png)