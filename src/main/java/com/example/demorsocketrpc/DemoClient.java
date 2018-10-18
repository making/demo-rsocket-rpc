package com.example.demorsocketrpc;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class DemoClient {
    public static void main(String[] args) throws Exception {
        Mono<SimpleServiceClient> clientMono = RSocketFactory.connect()
                .transport(TcpClientTransport.create(8801))
                .start()
                .map(SimpleServiceClient::new);

        CountDownLatch latch = new CountDownLatch(1);
        Flux<SimpleResponse> response = clientMono
                .flatMapMany(client -> {
                    Flux<SimpleRequest> request = Flux.interval(Duration.ofSeconds(1))
                            .take(10)
                            .map(i -> SimpleRequest.newBuilder().setRequestMessage("hi" + i).build());
                    return client.streamingRequestAndResponse(request);
                })
                .log()
                .doFinally(x -> latch.countDown());
        response.subscribe();
        latch.await();
    }
}
