package cn.it.ssm.sys.controller;

import cn.it.ssm.common.entity.RedisConstants;
import cn.it.ssm.common.entity.RequestMappingDetail;
import cn.it.ssm.common.monitor.ApiEnum;
import cn.it.ssm.common.vo.ApiMonitorVO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MonitorController implements ApplicationContextAware {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ApplicationContext applicationContext;

    @GetMapping("sys/apiCountPage")
    public String apiCount() {
        return "sys/apiCountPage";
    }

    @GetMapping("sys/apiInfoPage")
    public String apiInfo() {
        return "sys/apiInfoPage";
    }

    /**
     * 手动缓存所有接口到redis
     *
     * @param request
     * @return
     */
    @RequestMapping("sys/cacheAllUrl")
    @ResponseBody
    public Set<String> getAllUrl(HttpServletRequest request) {
        final Set<String> result = new HashSet<String>();
        WebApplicationContext wc = (WebApplicationContext) request
            .getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            PatternsRequestCondition pc = rmi.getPatternsCondition();
            Set<String> pSet = pc.getPatterns();
            result.addAll(pSet);
        }

        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String url : result) {
                    connection.zAdd(ApiEnum.API_URI.getApiValue().getBytes(), 0, url.getBytes());
                }
                return null;
            }
        });
        return result;
    }

    /**
     * 获取所有接口详细信息
     *
     * @param request
     * @return
     */
    @RequestMapping("sys/getAllUrlMap")
    @ResponseBody
    public ArrayList<RequestMappingDetail> getAllUrlMap(HttpServletRequest request) {
        ArrayList<RequestMappingDetail> requestMappingDetail = new ArrayList<RequestMappingDetail>();
        WebApplicationContext wc = (WebApplicationContext) request
            .getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> rmi : handlerMethods.entrySet()) {
            RequestMappingDetail mappingDetail = new RequestMappingDetail();

            RequestMappingInfo requestMappingInfo = rmi.getKey();
            //请求uri
            String requestUrl = requestMappingInfo.getPatternsCondition().toString();
            //requestUrl.
            //请求类型 get/post/put...为空则不限
            String requestType = requestMappingInfo.getMethodsCondition().toString();

            HandlerMethod handlerMethod = rmi.getValue();
            //控制器名称
            String controllerName = handlerMethod.getBeanType().getName();

            Class<?>[] parameterTypes = handlerMethod.getMethod().getParameterTypes();
            ArrayList<String> parameterTypesList = new ArrayList<String>();
            for (Class<?> class1 : parameterTypes) {
                String name = class1.getName();
                parameterTypesList.add(name);
            }
            //请求参数类型
            String methodParmaTypes = parameterTypesList.toString();
            //请求方法名
            String methodName = handlerMethod.getMethod().getName();
            mappingDetail.setRequestUrl(requestUrl);
            mappingDetail.setRequestType(requestType);
            mappingDetail.setControllerName(controllerName);
            mappingDetail.setMethodName(methodName);
            mappingDetail.setMethodParmaTypes(methodParmaTypes);

            requestMappingDetail.add(mappingDetail);

        }
        //PageListVO pageListVO = new PageListVO();
        //pageListVO.setTotal((long) requestMappingDetail.size());
        //pageListVO.setRows(requestMappingDetail);
        return requestMappingDetail;
    }

    /**
     * 获取api监控时间列表
     *
     * @return
     */
    @RequestMapping("sys/apitimelist")
    @ResponseBody
    public Integer[] getApiTime() {
        Integer[] times = RedisConstants.PRECISION;
        Arrays.sort(times, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        return times;

    }

    /**
     * 获取api地址列表
     *
     * @return
     */
    @RequestMapping("sys/apiurilist")
    @ResponseBody
    public Set<String> getApiList() {
        Set<String> apiList = stringRedisTemplate.opsForZSet().range(ApiEnum.API_URI.getApiValue(), 0, -1);
        return apiList;

    }

    /**
     * 获取api监控统计信息
     *
     * @param apitime 时间
     * @param apilist api地址
     * @return
     * @throws ParseException
     */
    @RequestMapping("sys/apimonitor")
    @ResponseBody
    public ArrayList<ApiMonitorVO> getApiMonitor(String apitime, String apilist) throws ParseException {
        if (apitime == null || apilist == null) {
            return null;
        }
        ArrayList<ApiMonitorVO> monitorVOArrayList = new ArrayList<>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //count:18000:/getAllUrlMap
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(ApiEnum.API_COUNT_PREFIX.getApiValue() + apitime + ":" + apilist);
        for (Map.Entry entry :
            map.entrySet()) {
            ApiMonitorVO apiMonitorVO = new ApiMonitorVO();
            //apiMonitorVO.setApiTime((String) entry.getKey());
            Long key = Long.valueOf((String) entry.getKey());

            //System.out.println(key);
            String format = simpleDateFormat.format(new Date(key * 1000));
            apiMonitorVO.setApiTime(format);
            apiMonitorVO.setApiList((String) entry.getValue());
            monitorVOArrayList.add(apiMonitorVO);
        }
        monitorVOArrayList.sort(new Comparator<ApiMonitorVO>() {
            @Override
            public int compare(ApiMonitorVO o1, ApiMonitorVO o2) {
                int res = 0;
                try {

                    //res = simpleDateFormat.parse(o1.getApiTime()).before(simpleDateFormat.parse(o2.getApiTime()))?-1:1;
                    res = o2.getApiTime().compareTo(o1.getApiTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }
        });
        return monitorVOArrayList;

    }

    @RequestMapping("sys/redisInfo")
    @ResponseBody
    public Properties getRedisInfo() {
        Properties info = stringRedisTemplate.execute(new RedisCallback<Properties>() {
            @Override
            public Properties doInRedis(RedisConnection connection) throws DataAccessException {
                Properties info = connection.info();
                //System.out.println(info);
                return info;
            }
        });
        return info;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        final Set<String> result = new HashSet<String>();
        RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        for (RequestMappingInfo rmi : handlerMethods.keySet()) {
            PatternsRequestCondition pc = rmi.getPatternsCondition();
            Set<String> pSet = pc.getPatterns();
            result.addAll(pSet);
        }

        stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                for (String url : result) {
                    connection.zAdd(ApiEnum.API_URI.getApiValue().getBytes(), 0, url.getBytes());
                }
                return null;
            }
        });
    }
}
