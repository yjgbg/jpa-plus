package com.github.yjgbg.jpa.std;

import com.github.yjgbg.jpa.core.EntityG;
import com.github.yjgbg.jpa.core.Sort;
import com.github.yjgbg.jpa.core.Spec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@Getter
@RequiredArgsConstructor
public class JpaExecutable<A> {
	private final EntityManager em;
	private final Class<A> domainClass;
	private final Spec<A> spec;
	private final Sort<A> sort;
	private final EntityG<A> entityG;
}
