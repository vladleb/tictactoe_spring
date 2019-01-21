package com.suai.tictactoe.controller;

import com.suai.tictactoe_spring.server.model.Coordinates;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class WebSocketAction {
    private static final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    @Getter
    @Setter
    private Coordinates coordinates = null;

    public ListenableFuture<StompSession> connect() {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/rooms";
        return stompClient.connect(url, headers, new WebSocketAction.MyHandler(), "localhost", 8080);
    }

    public void subscribe(StompSession stompSession, String room) {
        stompSession.subscribe("/topic/rooms/" + room, new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                String[] coordinatesStr = new String((byte[]) o).split(",");
                coordinatesStr[0] = coordinatesStr[0].replaceAll("[^\\d]", "");
                coordinatesStr[1] = coordinatesStr[1].replaceAll("[^\\d]", "");

                coordinates = new Coordinates(Integer.parseInt(coordinatesStr[0]), Integer.parseInt(coordinatesStr[1]));
            }
        });
    }

    public void send(StompSession stompSession, Coordinates coordinates, String room) {
        stompSession.send("/app/rooms/" + room, coordinates.toString().getBytes());
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            System.out.println("Now connected");
        }
    }
}
