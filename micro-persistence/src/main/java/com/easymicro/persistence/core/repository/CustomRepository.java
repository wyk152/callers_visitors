package com.easymicro.persistence.core.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**************************************
 * 自定义Repository方法
 * @author LinYingQiang
 * @date 2018-08-27 17:32
 * @qq 961410800
 *
************************************/
@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T>,QuerydslPredicateExecutor<T> {

    boolean support(String modelType);

    EntityManager getEntityManager();

    /**
     * 更新(NULL包含更新到数据中)
     */
    T update(T t);

    /**
     * 更新(跳过NULL字段,不更新到数据库中)
     */
    T updateSkipNull(T t);

    /**
     * 批量更新(NULL包含更新到数据中)
     */
    List<T> update(List<T> iterable);

    /**
     * 批量更新(跳过NULL字段,不更新到数据库中)
     */
    List<T> updateSkipNull(List<T> iterable);
    
    /**
     * 条件删除
     */
    void delete(Example<T> example);
    
    /**
     * 条件删除
     */
    void delete(Specification<T> specification);

    /**
     * 根据HQL查询
     */
    List<T> queryHQL(String hql);

    /**
     * 根据SQL查询
     */
    List<T> querySQL(String sql);

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
     * 根据条件更新
     */
    T updateSkipNull(Example<T> example, T t);

    /**
     * 根据条件更新
     */
    T updateSkipNull(Specification<T> spec, T t);
}
