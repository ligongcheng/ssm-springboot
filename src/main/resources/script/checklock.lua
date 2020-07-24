--
-- Created by chengtao
-- Date: 2019/8/19
-- redis 设置锁expire时间脚本
--

if redis.call('get',KEYS[1]) == ARGV[1]
then
    return redis.call('expire',ARGV[2])
else
    return false
end
