package com.github.yjgbg.jpa.std;

import com.github.yjgbg.jpa.core.EntityG;
import com.github.yjgbg.jpa.core.Sort;
import com.github.yjgbg.jpa.core.Spec;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EntityManagers {
	public static <A> A findOne(EntityManager em, Class<A> clazz, Spec<A> spec, EntityG<A> entityG) {
		return typedQuery(em, clazz, spec,entityG,null).getSingleResult();
	}

	/**
	 * 分页查询
	 * @param em 实体管理器
	 * @param clazz 目标类型
	 * @param spec where条件
	 * @param entityG 实体图
	 * @param sort 顺序
	 * @param page 页码
	 * @param pageSize 页面大小
	 * @param <A> 目标类型
	 * @return
	 */
	public static <A> List<A>
	findAll(EntityManager em, Class<A> clazz, Spec<A> spec, EntityG<A> entityG, Sort<A> sort, int page, int pageSize) {
		final var query = typedQuery(em, clazz, spec,entityG,sort);
		return page < 0 ? query.getResultList()
				: query.setFirstResult(page * pageSize).setMaxResults(page).getResultList();
	}

	public static <A> int count(EntityManager em, Class<A> clazz, Spec<A> spec) {
		return typedQuery(em, clazz, spec,null,null).getMaxResults();
	}

	private static <A> TypedQuery<A>
	typedQuery(EntityManager em, Class<A> clazz, Spec<A> spec,EntityG<A> entityG,Sort<A> sort) {
		final var cb = em.getCriteriaBuilder();
		final var cq = cb.createQuery(clazz);
		final var root = cq.from(clazz);
		if (sort!=null) cq.orderBy(sort.toOrder(root,cb));
		final var predicate = spec.predicate(root, cq, cb);
		final var res = em.createQuery(cq.where(predicate));
		if (entityG!=null) res.setHint(entityG.key(), entityG.toEntityGraph(em,clazz));
		return res;
	}
}
