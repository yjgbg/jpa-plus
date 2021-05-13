package com.github.yjgbg.jpa.ext;

import com.github.yjgbg.jpa.std.EntityManagers;
import com.github.yjgbg.jpa.core.EntityG;
import com.github.yjgbg.jpa.core.Sort;
import com.github.yjgbg.jpa.core.Spec;
import com.github.yjgbg.jpa.std.JpaExecutable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

public class LbkExtJpaPlusCore {
	public static  <A> JpaExecutable<A> select(EntityManager em, Class<A> domainClass) {
		return new JpaExecutable<>(em,domainClass, Spec.alwaysTrue(), Sort.unsorted(), EntityG.of());
	}

	public static  <A> int count(JpaExecutable<A> exe) {
		return EntityManagers.count(exe.getEm(),exe.getDomainClass(),exe.getSpec());
	}

	public static  <A> List<A> findAll(JpaExecutable<A> exe, int page, int pageSize) {
		return EntityManagers.findAll(exe.getEm(),exe.getDomainClass(),exe.getSpec(),
				exe.getEntityG(),exe.getSort(),page,pageSize);
	}

	public static  <A> A findOne(JpaExecutable<A> exe) {
		return EntityManagers.findOne(exe.getEm(),exe.getDomainClass(),exe.getSpec(),exe.getEntityG());
	}

	public static <A> JpaExecutable<A> spec(JpaExecutable<A> exe,Function<Spec<A>,Spec<A>> specMapper) {
		return new JpaExecutable<>(exe.getEm(), exe.getDomainClass(),
				specMapper.apply(exe.getSpec()),exe.getSort(),exe.getEntityG());
	}

	public static <A> JpaExecutable<A> entityG(JpaExecutable<A> exe,Function<EntityG<A>,EntityG<A>> entityGMapper) {
		return new JpaExecutable<>(exe.getEm(), exe.getDomainClass(),
				exe.getSpec(),exe.getSort(),entityGMapper.apply(exe.getEntityG()));
	}

	public static <A> JpaExecutable<A> sort(JpaExecutable<A> exe,Function<Sort<A>,Sort<A>> sortMapper) {
		return new JpaExecutable<>(exe.getEm(), exe.getDomainClass(),
				exe.getSpec(),sortMapper.apply(exe.getSort()),exe.getEntityG());
	}
}
