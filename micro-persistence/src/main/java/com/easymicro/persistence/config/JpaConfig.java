package com.easymicro.persistence.config;

import com.easymicro.persistence.core.factory.CustomRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**************************************
 * 配置打开JPA配置
 * @author LinYingQiang
 * @date 2018-08-09 20:05
 * @qq 961410800
 *
************************************/
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class, basePackages = {"com.easymicro.persistence.modular.repository"})
@EntityScan(basePackages = {"com.easymicro.persistence.modular.model"})
public class JpaConfig {

}
