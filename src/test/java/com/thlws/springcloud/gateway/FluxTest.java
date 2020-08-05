package com.thlws.springcloud.gateway;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author HanleyTang 2020/8/4
 */
public class FluxTest {

    public static void main(String[] args) {

        monoTest();

    }

    public static void monoTest(){
        Mono<String> mono = Mono.just("hello");
        mono.subscribe(System.out::println);
    }


    public static void build(){
        Flux<String> list =  Flux.fromIterable(Arrays.asList("hello", "Jack", "Rose"));
        Flux<String> newList = list.filter(e -> e.equals("Jack5"));
        Mono<String> one = newList.single("");
        one.subscribe(e->{
            System.out.println(" final = "+e);
        });
    }


}
