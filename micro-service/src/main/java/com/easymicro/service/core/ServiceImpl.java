package com.easymicro.service.core;


import com.easymicro.persistence.core.repository.CustomRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**************************************
 * 基础Service实现类
 * @author LinYingQiang
 * @date 2018-08-09 22:38
 * @qq 961410800
 *
************************************/
@Transactional
public class ServiceImpl<M extends CustomRepository<T,PK>,T,PK extends Serializable> implements IService<T,PK> {

    @Autowired
    protected M baseRepository;

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public T find(PK id) {
        Optional<T> optional = baseRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public T insert(T t) {
        return baseRepository.save(t);
    }

    @Override
    public List<T> insert(List<T> models) {
        return baseRepository.saveAll(models);
    }

    @Override
    public T update(T t) {
        return baseRepository.update(t);
    }

    @Override
    public List<T> update(List<T> models) {
        return baseRepository.update(models);
    }

    @Override
    public T updateSkipNull(T t) {
        return baseRepository.updateSkipNull(t);
    }

    @Override
    public List<T> updateSkipNull(List<T> models) {
        return  baseRepository.updateSkipNull(models);
    }

    @Override
    public void delete(PK id) {
        baseRepository.deleteById(id);
    }

    @Override
    public void delete(List<PK> ids) {
        ids.forEach(this::delete);
    }

    @Override
    public void delete(T t) {
        baseRepository.delete(t);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return baseRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> long count(Example<S> example) {
        return baseRepository.count(example);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> boolean exists(Example<S> example) {
        return baseRepository.exists(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(PK id) {
        return baseRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return baseRepository.findAll(example,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return baseRepository.findAll(example,sort);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> List<S> findAll(Example<S> example) {
        return baseRepository.findAll(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public <S extends T> S findOne(Example<S> example) {
        Optional<S> optional = baseRepository.findOne(example);
        return optional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAllById(List<PK> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return baseRepository.findAll(spec,sort);
    }

    @Override
    @Transactional(readOnly = true)
    public long count(Specification<T> spec) {
        return baseRepository.count(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public T findOne(Specification<T> spec) {
        Optional<T> optional = baseRepository.findOne(spec);
        return optional.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Specification<T> spec) {
        return baseRepository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return baseRepository.findAll(spec,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> queryHQL(String hql) {
        return baseRepository.queryHQL(hql);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> querySQL(String sql) {
        return baseRepository.querySQL(sql);
    }

    @Override
    public void deleteAll() {
        baseRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

	@Override
	public void delete(Example<T> example) {
		baseRepository.delete(example);
	}

	@Override
	public void delete(Specification<T> specification) {
		baseRepository.delete(specification);
	}

    @Override
    public JPAQueryFactory qslQuery() {
        return baseRepository.qslQuery();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findOne(Predicate predicate) {
        return baseRepository.findOne(predicate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Predicate predicate) {
        return baseRepository.findAll(predicate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Predicate predicate, Sort sort) {
        return baseRepository.findAll(predicate,sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return baseRepository.findAll(predicate,orders);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(OrderSpecifier<?>... orders) {
        return baseRepository.findAll(orders);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return baseRepository.findAll(predicate,pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count(Predicate predicate) {
        return baseRepository.count(predicate);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Predicate predicate) {
        return baseRepository.exists(predicate);
    }

    @Override
    public EntityManager entityManager() {
        return baseRepository.getEntityManager();
    }

}
