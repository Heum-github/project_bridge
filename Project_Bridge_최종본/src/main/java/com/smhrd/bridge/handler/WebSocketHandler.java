package com.smhrd.bridge.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // WebSocket 메시지를 처리하는 로직을 작성합니다.
        String payload = message.getPayload();
        // 처리된 결과를 클라이언트에게 다시 전송할 수 있습니다.
        session.sendMessage(new TextMessage("서버에서 받은 메시지: " + payload));
    }
}