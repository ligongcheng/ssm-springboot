--
-- Created by chengtao
-- Date: 2019/8/19
-- redis 释放锁脚本
--

if redis.call('get',KEYS[1]) == ARGV[1]
then
    return redis.call('del',KEYS[1])
else
    return false
end
