package io.github.sicozz.fraudsim.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig implements AsyncConfigurer {

  @Bean
  public Executor taskExecutor() {
    // Use virtual threads for high-throughput transaction generation
    return Executors.newVirtualThreadPerTaskExecutor();
  }

  @Bean
  public ThreadPoolTaskExecutor applicationTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setTaskDecorator(
        runnable -> {
          return Thread.ofVirtual().name("fraudsim").unstarted(runnable);
        });
    executor.setCorePoolSize(8);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("app-exec-");
    executor.initialize();
    return executor;
  }
}
