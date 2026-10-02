package kh.gov.mptc.koklim.ecommerce.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "service")
@Getter
@Setter
@NoArgsConstructor

// Configuration props already have @RefreshScope
public class NacosConfigProps {
    private String name;
    private String info;
    private String version;
}
