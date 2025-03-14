package sam.match_manager.Match.Manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("redis")
public class RedisConfig {
  private String uri;

  public String getUri() {
    return uri;
  }
  public void setUri(String uri) {
    this.uri = uri;
  }
}
