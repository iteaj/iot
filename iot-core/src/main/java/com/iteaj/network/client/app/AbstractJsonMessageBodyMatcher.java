package com.iteaj.network.client.app;

import com.alibaba.fastjson.JSONObject;
import com.iteaj.network.ProtocolException;
import com.iteaj.network.client.ClientRelation;

/**
 * 用于处理报文体是Json格式
 * @see AppJsonMessageBody
 */
public abstract class AbstractJsonMessageBodyMatcher implements AppClientTypeMatcherHandle<AppJsonMessageBody> {

    @Override
    public AppJsonMessageBody deserialize(Object body, RequestType type) {
        if(body != null) {
            if(type == RequestType.REQ) {
                if(body instanceof JSONObject) {
                    AppJsonMessageBody jsonMessageBody = new AppJsonMessageBody();
                    jsonMessageBody.putAll((JSONObject) body);

                    return jsonMessageBody;
                } else {
                    throw new ProtocolException("报文体反序列化异常, AbstractJsonMessageBodyMatcher匹配器只支持Json格式的报文体类型: AppJsonMessageBody");
                }
            } else {

            }
        }

        return null;
    }

    @Override
    public abstract ClientRelation handle(AppClientMessage<AppJsonMessageBody> message);
}
