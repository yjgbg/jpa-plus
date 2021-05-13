package com.github.yjgbg.jpa.core;

import com.github.yjgbg.jpa.utils.SpecUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;

/**
 *
 * @param <A> 实体类型
 */
public interface Spec<A> {
	Predicate predicate(Root<A> root, CriteriaQuery<A> query, CriteriaBuilder cb);
	// 定义Spec之间的运算
	static <A> Spec<A> and(Spec<A> spec0, Spec<A> spec1) {
		return (root, query, cb) -> cb.and(spec0.predicate(root,query,cb),spec1.predicate(root,query,cb));
	}
	static <A> Spec<A> or(Spec<A> spec0,Spec<A> spec1) {
		return (root, query, cb) -> cb.or(spec0.predicate(root,query,cb),spec1.predicate(root,query,cb));
	}

	static <A> Spec<A> not(Spec<A> spec0) {
		return (root, query, cb) -> spec0.predicate(root,query,cb).not();
	}
	// 定义基本的Spec构造方式
	// 矛盾式 // 除了矛盾式和重言式，其余皆为可满足式
	static <A> Spec<A> alwaysFalse() {
		return (root, query, cb) -> cb.equal(cb.literal(1),0);
	}
  // 重言式 // 除了矛盾式和重言式，其余皆为可满足式
	static <A> Spec<A> alwaysTrue() {
    return (root, query, cb) -> cb.equal(cb.literal(1),1);
	}

	// in
	static <A,B> Spec<A> values(String path, Collection<B> values) {
		return values.size()==0 ? alwaysFalse()// 如果集合为空，则返回矛盾式
				:values.size()==1 ? (root, query, cb) -> cb.equal(SpecUtils.str2Path(root,path),values.iterator().next())
				:(root, query, cb) -> cb.in(SpecUtils.str2Path(root,path)).in(values);
	}

	static <A> Spec<A> like(String path,String value) {
		// 如果值为空，则返回重言式
		return value==null ? alwaysTrue() : (root, query, cb) -> cb.like(SpecUtils.str2Path(root,path),value);
	}

	static <A,B extends Comparable<B>> Spec<A> greaterThan(String path, B value) {
		// 将null视为无穷小,如果值为空，则返回重言式
		return value==null ? alwaysTrue() : (root, query, cb) -> cb.greaterThan(SpecUtils.str2Path(root,path),value);
	}
}
