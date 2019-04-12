import com.zzd.webflux.model.MyEvent;
import com.zzd.webflux.model.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zhangzhidong
 * @since 2019/1/7
 */
public class MyTest {

    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8092");
        Mono<String> resp = webClient
                .get().uri("/hello")
                .retrieve()
                .bodyToMono(String.class);
        resp.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1); //也可以用CountDownLatch保证拿到结果
    }

    @Test
    public void webClientTest2() throws InterruptedException {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8092").build(); // 1
        webClient
                .get().uri("/user")
                .accept(MediaType.APPLICATION_STREAM_JSON) // 2
                .exchange() // 获取response信息，返回值为ClientResponse，retrive()可以看做是exchange()方法的“快捷版”
                .flatMapMany(response -> response.bodyToFlux(User.class))   // 使用flatMap来将ClientResponse映射为Flux；
                .doOnNext(System.out::println)  // 只读地peek每个元素，然后打印出来，它并不是subscribe，所以不会触发流；
                .blockLast();   //上个例子中sleep的方式有点low，blockLast方法，顾名思义，在收到最后一个元素前会阻塞，响应式业务场景中慎用。
    }

    @Test
    public void webClientTest3() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8092");
        webClient
                .get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM)    // 1
                .retrieve()
                .bodyToFlux(String.class)
                .log()  // 这次用log()代替doOnNext(System.out::println)来查看每个元素；
                .take(10)   // 由于/times是一个无限流，这里取前10个，会导致流被取消；
                .blockLast();
    }

    @Test
    public void webClientTest4() {
        Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new MyEvent(System.currentTimeMillis(), "message-" + l)).take(5); // 1
        WebClient webClient = WebClient.create("http://localhost:8092");
        webClient
                .post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON) // 2
                .body(eventFlux, MyEvent.class) // 3
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
