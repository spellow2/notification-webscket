package com.mosaicsquare.notification.websocket.websocket.message;

import lombok.Data;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Data
public class SubscriptionMsg {
    private String subId;
    private String msgType;
    private Map<String, String> detailMsg;

    public SubscriptionMsg() {
    }

    public SubscriptionMsg(String subId, String msgType, String message) {
        setSubId(subId);
        setMsgType(msgType);

        Map<String, String> detailMsg = new HashMap<>();
        detailMsg.put("message",message);
        setDetailMsg(detailMsg);
    }

    public SubscriptionMsg(String subId, String msgType, Map<String, String> detailMsg) {
        setSubId(subId);
        setMsgType(msgType);
        setDetailMsg(detailMsg);
    }

    public JSONObject toJSON() {
        JSONObject jsonData = null;
        try {
            String jsonDetailMsg = "";
            for ( String key : detailMsg.keySet() ) {
                if ( 0 > jsonDetailMsg.length() ) {
                    jsonDetailMsg += ",";
                }
                jsonDetailMsg += "\""+key+"\":\""+detailMsg.get(key)+"\"";
            }
            jsonData = new JSONObject("{\"subId\":\""+getSubId()+"\",\"msgType\":\""+getMsgType()+"\",\"detailMsg\":{"+jsonDetailMsg+"}}");
        } catch ( Exception e ) {
        }
        return jsonData;
    }
}