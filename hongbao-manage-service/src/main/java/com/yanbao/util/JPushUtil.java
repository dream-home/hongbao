package com.yanbao.util;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.yanbao.core.model.JpushExtraModel;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 极光推送
 *
 * @author zcj
 * @date 2017年2月27日
 */
public class JPushUtil {
    /*暂时写死，后期有重构再调整*/
//    private static String MASTER_SECRET = "a143399be3c99ee3e7afbd77";
//    private static String APP_KEY = "87a89551ec616197a8c5d8ff";
    private static String MASTER_SECRET = "d242a7fd6a654af245ddf58f";
    private static String APP_KEY = "f67377a1fcc75930682f8670";

    /**
     * 根据每个用户的极光推送Id进行精准推送
     *
     * @param registrationId 机关推送Id
     * @param content        推送的消息内容
     */
    public static boolean pushPayloadByid(String registrationId, String content) {
        return pushPayloadByid(registrationId, content, null);
    }

    /**
     * 根据每个用户的极光推送Id进行精准推送
     *
     * @param registrationId 机关推送Id
     * @param content        推送的消息内容
     * @param jpushExtraType 推送扩展字段(不固定参数)
     */
    public static boolean pushPayloadByid(String registrationId, String content, JpushExtraModel... jpushExtraType) {
        if (StringUtils.isBlank(registrationId)) {
            return false;
        }
        PushResult result = null;
        try {
            //携带扩展字段
            Map<String, String> extra = null;
            if (jpushExtraType != null && jpushExtraType.length > 0) {
                extra = new HashedMap();
                for (JpushExtraModel extraType : jpushExtraType) {
                    extra.put(extraType.getKey(), extraType.getValue());
                }
            }
            JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
            PushPayload payload = PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(registrationId))
                    .setNotification(
                            Notification.newBuilder()
                                    .addPlatformNotification(AndroidNotification.newBuilder().addExtras(extra).setAlert(content).build())
                                    .addPlatformNotification(IosNotification.newBuilder().addExtras(extra).setAlert(content).build()).build())
                    .setOptions(Options.newBuilder().setApnsProduction(true).build())
                    .build();

            result = jpushClient.sendPush(payload);
        } catch (Exception e) {
            System.out.println("极光推送错误，不显示错误信息");
        }
        return result.isResultOK();
    }
}
