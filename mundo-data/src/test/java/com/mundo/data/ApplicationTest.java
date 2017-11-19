package com.mundo.data;

import com.mundo.data.aop.BusinessRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * ApplicationTest
 *
 * @author maodh
 * @since 2017/11/17
 */
@EnableMundoData
@EnableAspectJAutoProxy
@Configuration
public class ApplicationTest {

    @Bean
    BusinessRepository partitionComponent() {
        return new BusinessRepository();
    }
}
