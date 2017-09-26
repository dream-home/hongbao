package com.yanbao.util;

import com.mall.model.Parameter;
import com.yanbao.constant.RedisKey;
import com.yanbao.redis.Strings;
import com.yanbao.service.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
@Component
public class ParamUtil {

    private static final Logger logger = LoggerFactory.getLogger(ParamUtil.class);
    @Autowired
    ParameterService parameterService;

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    //通过@PostConstruct 和 @PreDestroy 方法 实现初始化和销毁bean之前进行的操作
    @PostConstruct
    public void init() {
        logger.info("启动服务器，加载系统参数配置");
        instance = this;
        instance.parameterService = this.parameterService;   // 初使化时将已静态化的testService实例化
        instance.reload();//每次启动都从数据库中抓取最新系统参数设置
    }

    private Map<String, Parameter> hmProperties_;  //  = new HashMap();

    // 定义一个私有构造方法
    private ParamUtil() {

    }

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile ParamUtil instance;
    //定义一个静态私有变量(管理端是否有做修改)
    private static volatile String updateToken;

    //定义一个共有的静态方法，返回该类型实例
    public static ParamUtil getIstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (ParamUtil.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    logger.info("未初始化对象，重新初始instance变量");
                    instance = new ParamUtil();
                    instance.reload();
                }
            }
        }
        return instance;
    }

    //从数据库中加载最新系统参数
    public synchronized void reload() {
        try {
            //获取管理端最新更改标致
            updateToken = Strings.get(RedisKey.SYS_PARAM_UPDTOKEN.getKey());
            //初始化redis
            if (updateToken == null || "".equalsIgnoreCase(updateToken)) {
                Strings.set(RedisKey.SYS_PARAM_UPDTOKEN.getKey(), UUIDUtil.getUUID());
                updateToken = Strings.get(RedisKey.SYS_PARAM_UPDTOKEN.getKey());
            }
            hmProperties_ = new HashMap<String, Parameter>();
            List<Parameter> list = parameterService.getList();
            for (Parameter model : list) {
                hmProperties_.put(model.getName(), model);
            }
            logger.info("成功加载系统参数");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("从数据库加载系统参数失败：" + e.getMessage());
        }
    }

    //管理端是否有更新
    private boolean isUpdateParam() {
        try {
            String newToken = Strings.get(RedisKey.SYS_PARAM_UPDTOKEN.getKey());
            if (updateToken == null || !updateToken.equalsIgnoreCase(newToken.trim()) || "".equalsIgnoreCase(newToken)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("从redis获取系统参数更新时间失败");
            return true;
        }
        return false;
    }



    //管理端更新系统参数
    public synchronized void reloadParam() {
        Strings.set(RedisKey.SYS_PARAM_UPDTOKEN.getKey(), UUIDUtil.getUUID());
        reload();
    }
    /**
     * 范例: prop.getInstance().getAllModel();
     * <p>
     * 返回所有的系统参数
     *
     * @return
     * @throws Exception
     */

    public synchronized List<Parameter> getAllModel() throws Exception {

        if (isUpdateParam()) {
            reload();
        }

        if (hmProperties_ == null || hmProperties_.size() == 0) {
            reload();
        }
        if (hmProperties_ == null || hmProperties_.size() == 0) {
            return null;
        }
        List<Parameter> list = new ArrayList<Parameter>();

        //遍历map中的键
        for (String key : hmProperties_.keySet()) {
            list.add(hmProperties_.get(key));
        }
        Collections.sort(list);
        return list;

    }
    /**
     * 范例: prop.getInstance().getAllModel("SYS");
     * <p>
     * 返回所有的系统参数
     *
     * @return
     * @throws Exception
     */

    public synchronized List<Parameter> getAllModel(String group) throws Exception {

        if (isUpdateParam()) {
            reload();
        }

        if (hmProperties_ == null || hmProperties_.size() == 0) {
            reload();
        }
        if (hmProperties_ == null || hmProperties_.size() == 0) {
            return null;
        }
        List<Parameter> list = new ArrayList<Parameter>();
        Parameter model = null;
        //遍历map中的键
        for (String key : hmProperties_.keySet()) {
            model = hmProperties_.get(key);
            if(model !=null && model.getGroupType().equals(group)) {
                list.add(model);
            }
        }
        Collections.sort(list);
        return list;

    }

    /**
     * 范例: prop.getInstance().get("COMPANY");
     *
     * @param sName
     * @return
     * @throws Exception
     */

    public synchronized Parameter getModel(String sName) throws Exception {

        if (isUpdateParam()) {
            reload();
        }

        if (hmProperties_ == null || hmProperties_.size() == 0) {
            reload();
        }

        Parameter model = hmProperties_.get(sName);

        if (model == null) {
            return null;
        }
        return model;

    }

    /**
     * 范例: prop.getInstance().get("COMPANY");
     *
     * @param sName
     * @return
     * @throws Exception
     */

    public synchronized String get(String sName) throws Exception {

        if (isUpdateParam()) {
            reload();
        }

        if (hmProperties_ == null || hmProperties_.size() == 0) {
            reload();
        }

        Parameter model = hmProperties_.get(sName);

        if (model == null) {
            return "";
        }
        String _sValue = model.getValue();

        if (_sValue == null) _sValue = "";

        return _sValue;

    }


    /**
     * 取屬性值.
     *
     * @param sName
     * @param sDefault
     * @return
     * @throws Exception
     */

    public synchronized String get(String sName, String sDefault) throws Exception {

        if (isUpdateParam()) {
            reload();
        }

        if (hmProperties_ == null || hmProperties_.size() == 0) {
            reload();
        }

        Parameter model = hmProperties_.get(sName);

        if (model == null) {
            return sDefault;
        }

        String _sValue = model.getValue();

        if ((_sValue == null) || "".equals(_sValue)) {
            return sDefault;

        }

        return _sValue;

    }
}
