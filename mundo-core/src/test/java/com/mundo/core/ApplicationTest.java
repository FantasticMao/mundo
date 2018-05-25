package com.mundo.core;

import com.mundo.core.aop.AssertFalseComponent;
import com.mundo.core.aop.TimeoutComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * ApplicationTest
 * 附：可以使用 <code>@TestConfiguration</code> 覆盖 <code>@Configuration</code> 中已存在的 Bean
 *
 * @author maodh
 * @since 2017/11/15
 */
@EnableMundoCore
@EnableAspectJAutoProxy
@Configuration
public class ApplicationTest {

    @Bean
    TimeoutComponent timeoutComponent() {
        return new TimeoutComponent();
    }

    @Bean
    AssertFalseComponent assertFalseComponent() {
        return new AssertFalseComponent();
    }

}
