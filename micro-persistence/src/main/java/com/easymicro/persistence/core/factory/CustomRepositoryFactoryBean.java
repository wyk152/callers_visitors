package com.easymicro.persistence.core.factory;



import com.easymicro.persistence.core.repository.CustomRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**************************************
 * 通用Jpa Dao对象
 * @author LinYingQiang
 * @date 2018-08-09 17:40
 * @qq 961410800
 *
************************************/
public class CustomRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

    public CustomRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new MicroRepositoryFactory(em);
    }

    private static class MicroRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

        private final EntityManager em;

        public MicroRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            return new CustomRepositoryImpl<T,I>(JpaEntityInformationSupport.getEntityInformation((Class<T>)information.getDomainType(), em),(Class<T>)information.getDomainType(),em);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return CustomRepositoryImpl.class;
        }

    }
}
