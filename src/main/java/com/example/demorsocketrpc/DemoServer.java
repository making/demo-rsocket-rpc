package com.example.demorsocketrpc;

import io.rsocket.RSocketFactory;
import io.rsocket.rpc.rsocket.RequestHandlingRSocket;
import io.rsocket.transport.netty.server.TcpServerTransport;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class DemoServer {
    public static void main(String[] args) {
        SimpleServiceServer server = new SimpleServiceServer(new DemoService(), Optional.empty(), Optional.empty());

        RSocketFactory.receive()
                .acceptor((connectionSetupPayload, rSocket) -> Mono.just(new RequestHandlingRSocket(server)))
                .transport(TcpServerTransport.create(8801))
                .start()
                .log("start")
                .block()
                .onClose()
                .log("onClose")
                .block();
    }
}
