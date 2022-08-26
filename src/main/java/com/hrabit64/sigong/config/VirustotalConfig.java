package com.hrabit64.sigong.config;

import com.kanishka.virustotalv2.VirusTotalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class VirustotalConfig {

    public VirustotalConfig(@Value("${virusTotal.api-key}") String virusTotalAPIKey){
        VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey(virusTotalAPIKey);
    }

}
