package pl.gdyzapomne.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * Ta klasa włącza możliwość równoczesnego uruchamiania zadań, dzięki czemu więcej niż jedna funkcja może być wykonywana w tym samym czasie.
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

    /**
     * Poniższa metoda zwraca obiekt, z którego aplikacja korzysta, żeby umożliwić równoczesne uruchamianie wielu zadań.
     */
    @Bean("threadPoolTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
