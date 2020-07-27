package com.cgm.kube;

import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.util.Config;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


/**
 * @author cgm
 */
@SpringBootApplication
@MapperScan("com.cgm.kube.*.mapper")
public class KubeManagerApplication {

    public static void main(String[] args) throws IOException {

        Configuration.setDefaultApiClient(Config.defaultClient());
        SpringApplication.run(KubeManagerApplication.class, args);
    }

}
