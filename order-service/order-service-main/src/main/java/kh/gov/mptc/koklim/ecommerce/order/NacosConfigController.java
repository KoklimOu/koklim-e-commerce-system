package kh.gov.mptc.koklim.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class NacosConfigController {

    private final NacosConfigProps nacosConfigProps;

    @GetMapping
    public Map<String, String> getDataSourceName() {
        return Map.of("serviceName", nacosConfigProps.getName(), "serviceInfo", nacosConfigProps.getInfo(), "version", nacosConfigProps.getVersion());
    }
}
