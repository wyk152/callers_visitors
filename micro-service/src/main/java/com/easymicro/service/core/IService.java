package com.easymicro.service.core;


import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**************************************
 * 基础Service接口[T model对象, PK 主键]
 * @author LinYingQiang
 * @date 2018-08-09 21:01
 * @qq 961410800
 *
 ************************************/
public interface IService<T, PK extends Serializable> {

    /**
     * 查找所有对象
     */
    List<T> findAll();

    /**
     * 根据id查询
     */
    T find(PK id);

    /**
     * 插入操作
     */
    T insert(T t);

    /**
     * 批量插入操作
     */
    List<T> insert(List<T> models);

    /**
     * 修改操作(NULL包含更新到数据中)
     */
    T update(T t);

    /**
     * 批量修改操作(NULL包含更新到数据中)
     */
    List<T> update(List<T> models);

    /**
     * 修改操作(跳过NULL字段,不更新到数据库中)
     */
    T updateSkipNull(T t);

    /**
     * 批量修改操作(跳过NULL字段,不更新到数据库中)
     */
    List<T> updateSkipNull(List<T> models);

    /**
     * 根据ID删除
     */
    void delete(PK id);

    /**
     * 根据ID集合删除
     */
    void delete(List<PK> ids);

    /**
     * 根据对象删除
     */
    void delete(T t);

    /**
     * 统计对象数量
     */
    long count();

    /**
     * 根据条件统计对象数量
     */
    <S extends T> long count(Example<S> example);

    /**
     * 根据条件判断是否存在
     */
    <S extends T> boolean exists(Example<S> example);

    /**
     * 根据PK判断是否存在记录
     */
    boolean exists(PK id);

    /**
     * 带条件分页查询
     */
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    /**
     * 带排序查询多个记录
     */
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    /**
     * 带条件查询
     */
    <S extends T> List<S> findAll(Example<S> example);

    /**
     * 根据排序条件查询多条记录
     */
    List<T> findAll(Sort sort);

    /**
     * 带条件查询一条记录
     */
    <S extends T> S findOne(Example<S> example);

    /**
     * 根据PK集合查询多条记录
     */
    List<T> findAllById(List<PK> ids);

    /**
     * 带条件查询
     */
    List<T> findAll(Specification<T> spec, Sort sort);

    /**
     * 带条件统计记录
     */
    long count(Specification<T> spec);

    /**
     * 带条件查询一条记录
     */
    T findOne(Specification<T> spec);

    /**
     * 带条件查询
     */
    List<T> findAll(Specification<T> spec);

    /**
     * 带条件分页查询
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable);

    /**
     * 根据HQL查询
     */
    List<T> queryHQL(String hql);

    /**
     * 根据SQL查询
     */
    List<T> querySQL(String sql);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 条件删除
     */
    void delete(Example<T> example);

    /**
     * 条件删除
     */
    void delete(Specification<T> specification);

    /**
     * 分页查询
     */
    Page<T> findAll(Pageable pageable);

    /**
     * JPA qsl查询
     */
    JPAQueryFactory qslQuery();

    /**
     * 查询单个对象(QueryDSL)
     */
    Optional<T> findOne(Predicate predicate);

    /**
     * 查询所有(QueryDSL)
     */
    List<T> findAll(Predicate predicate);

    /**
     * 查询所有(QueryDSL)
     */
    List<T> findAll(Predicate predicate, Sort sort);

    /**
     * 查询所有(QueryDSL)
     */
    List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    /**
     * 查询所有(QueryDSL)
     */
    List<T> findAll(OrderSpecifier<?>... orders);

    /**
     * 查询所有(QueryDSL)
     */
    Page<T> findAll(Predicate predicate, Pageable pageable);

    /**
     * 统计(QueryDSL)
     */
    long count(Predicate predicate);

    /**
     * 判断是否存在(QueryDSL)
     */
    boolean exists(Predicate predicate);

    /**
     * 获取EntityManager对象
     */
    EntityManager entityManager();

}
