package com.davelooper.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartupLogger {

  private final Environment environment;

  @EventListener(ApplicationReadyEvent.class)
  public void logApplicationReady() {
    String[] activeProfiles = environment.getActiveProfiles();
    String profiles = activeProfiles.length == 0 ? "default" : String.join(",", activeProfiles);
    log.info("Application started successfully: activeProfiles={}", profiles);
    log.info("Database connection and Liquibase migration completed successfully");
  }
}
