package com.easymicro.persistence.core.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.*;

/**************************************
 * 自定义Repository实现类
 * @author LinYingQiang
 * @date 2018-08-27 17:35
 * @qq 961410800
 *
 ************************************/
public class CustomRepositoryImpl<T, PK extends Serializable> extends QuerydslJpaRepository<T, PK> implements CustomRepository<T, PK> {

    private final Class<T> clazz;

    private final EntityManager em;

    private final JpaEntityInformation<T, ?> entityInformation;

    private final JPAQueryFactory qslQuery;

    private final BeanCopier beanCopier;


    public CustomRepositoryImpl(JpaEntityInformation information, Class<T> clazz, EntityManager em) {
        super(information, em);
        this.clazz = clazz;
        this.em = em;
        this.entityInformation = information;
        this.beanCopier = BeanCopier.create(clazz, clazz, false);
        this.qslQuery = new JPAQueryFactory(em);
    }

    @Override
    public boolean support(String modelType) {
        return clazz.getName().equals(modelType);
    }


    @Override
    public EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public T update(T t) {
        PK id = (PK) entityInformation.getId(t);
        if (!ObjectUtils.isEmpty(id)) {
            return save(t);
        }
        return null;
    }

    @Override
    public T updateSkipNull(T t) {
        PK id = (PK) entityInformation.getId(t);
        if (!ObjectUtils.isEmpty(id)) {
            Optional<T> optional = findById(id);
            if (optional.isPresent()) {
                String[] nullProperties = getNullProperties(t);
                T target = optional.get();
                BeanUtils.copyProperties(t, target, nullProperties);
                return em.merge(target);
            }
        }
        return null;
    }

    @Override
    public List<T> updateSkipNull(List<T> iterable) {
        if (iterable != null) {
            Iterator<T> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                updateSkipNull(iterator.next());
            }
        }
        return iterable;
    }

    /**
     * 获取对象的空属性
     */
    private static String[] getNullProperties(Object src) {
        //1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        //2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        //3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (propertyValue == null) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }


    @Override
    public List<T> update(List<T> iterable) {
        if (iterable != null) {
            Iterator<T> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                update(iterator.next());
            }
        }
        return iterable;
    }

    @Override
    public List<T> queryHQL(String hql) {
        return em.createQuery(hql, clazz).getResultList();
    }

    @Override
    public List<T> querySQL(String sql) {
        return em.createNativeQuery(sql, clazz).getResultList();
    }

    @Override
    public JPAQueryFactory qslQuery() {
        return this.qslQuery;
    }

    @Override
	public void delete(Example<T> example) {
		this.findAll(example).forEach(this::delete);
	}

	@Override
	public void delete(Specification<T> specification) {
		this.findAll(specification).forEach(this::delete);
	}

    @Override
    public T updateSkipNull(Example<T> example, T t) {
        Optional<T> optional = this.findOne(example);
        T db = null;
        if (optional.isPresent()) {
            String[] nullProperties = getNullProperties(t);
            T target = optional.get();
            BeanUtils.copyProperties(t, target, nullProperties);
            return em.merge(target);
        }
        return db;
    }

    @Override
    public T updateSkipNull(Specification<T> spec, T t) {
        Optional<T> optional = this.findOne(spec);
        T db = null;
        if (optional.isPresent()) {
            db = optional.get();
            beanCopier.copy(t,db,null);
            updateSkipNull(db);
        }
        return db;
    }

}
