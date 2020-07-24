--
-- Created by chengtao
-- Date: 2018/8/19
-- Time: 16:00
-- lua 限流脚本
--
local num = redis.call('incr', KEYS[1])
if num == 1 then
    redis.call('expire', KEYS[1], ARGV[1])
    return 1
elseif num > tonumber(ARGV[2]) then
    return 0
else
    return 1
end
