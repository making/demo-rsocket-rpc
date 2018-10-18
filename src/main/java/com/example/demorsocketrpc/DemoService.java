package com.example.demorsocketrpc;

import com.google.protobuf.Empty;
import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DemoService implements SimpleService {
    private final static Logger log = LoggerFactory.getLogger(DemoService.class);

    @Override
    public Mono<SimpleResponse> requestReply(SimpleRequest message, ByteBuf metadata) {
        log.info("requestReply({})", message);
        return Mono.just(SimpleResponse.newBuilder()
                .setResponseMessage("Hello " + message.getRequestMessage())
                .build())
                .log();
    }

    @Override
    public Mono<Empty> fireAndForget(SimpleRequest message, ByteBuf metadata) {
        log.info("fireAndForget({})", message);
        return Mono.empty();
    }

    @Override
    public Flux<SimpleResponse> requestStream(SimpleRequest message, ByteBuf metadata) {
        log.info("requestStream({})", message);
        return Flux.empty();
    }

    @Override
    public Mono<SimpleResponse> streamingRequestSingleResponse(Publisher<SimpleRequest> messages, ByteBuf metadata) {
        return Mono.empty();
    }

    @Override
    public Flux<SimpleResponse> streamingRequestAndResponse(Publisher<SimpleRequest> messages, ByteBuf metadata) {
        return Flux.from(messages)
                .log("request")
                .map(x -> SimpleResponse.newBuilder()
                        .setResponseMessage("Hello " + x.getRequestMessage())
                        .build())
                .log("response");
    }
}
