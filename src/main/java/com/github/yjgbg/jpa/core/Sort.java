package com.github.yjgbg.jpa.core;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public interface Sort<A> {
	List<Order> toOrder(Root<A> root, CriteriaBuilder cb);
	static <A> Sort<A> asc(String prop) {
		return (root, cb) -> List.of(cb.asc(root.get(prop)));
	}

	static <A> Sort<A> desc(String prop) {
		return (root, cb) -> List.of(cb.desc(root.get(prop)));
	}

	static <A> Sort<A> and(Sort<A> s0,Sort<A> s1) {
		return (root, cb) -> {
			final var res = new ArrayList<Order>();
			res.addAll(s0.toOrder(root,cb));
			res.addAll(s1.toOrder(root,cb));
			return res;
		};
	}

	static <A> Sort<A> unsorted() {
		return (root, cb) -> List.of();
	}
}
