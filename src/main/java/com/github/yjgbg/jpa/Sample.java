package com.github.yjgbg.jpa;

import com.github.yjgbg.jpa.core.Spec;
import com.github.yjgbg.jpa.ext.LbkExtJpaPlusCore;
import lombok.experimental.ExtensionMethod;

import javax.persistence.EntityManager;
import java.util.List;

@ExtensionMethod(LbkExtJpaPlusCore.class)
public class Sample {
	public static void main(String[] args) {

	}

	public static void test(EntityManager em) {
		final var a = em.select(Sample.class).findOne();
		final var exist = !em.select(Sample.class)
				.spec(spec -> Spec.and(spec,Spec.values("id", List.of(1,2,3))))
				.spec(spec -> Spec.and(spec,Spec.like("username","alice%")))
				.findAll(0,1).isEmpty();
		final var count = em.select(Sample.class)
				.spec(spec -> Spec.and(spec,Spec.like("username","alice%")))
				.count();
	}
}
