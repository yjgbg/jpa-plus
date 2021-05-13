package com.github.yjgbg.jpa.core;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Subgraph;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface EntityG<A> {
	Map<String, EntityG<?>> values();

	default String key() {
		return fetchGraph();
	}

	default String fetchGraph() {
		return "javax.persistence.fetchgraph";
	}

	default String loadGraph() {
		return "javax.persistence.loadgraph";
	}

	default EntityGraph<A> toEntityGraph(EntityManager em, Class<A> domainClass) {
		final var entityGraph = em.createEntityGraph(domainClass);
		processGraph(entityGraph::addAttributeNodes, entityGraph::addSubgraph);
		return entityGraph;
	}

	private void processGraph(Consumer<String> addAttributeNodes, Function<String, Subgraph<?>> addSubgraph) {
		final var partial = values().entrySet().stream()
				.collect(Collectors.partitioningBy(entry -> entry.getValue() == NONE));
		partial.get(true).stream().map(Map.Entry::getKey).forEach(addAttributeNodes);
		partial.get(false).forEach(entry -> {
			final var subGraph = addSubgraph.apply(entry.getKey());
			entry.getValue().processGraph(subGraph::addAttributeNodes, subGraph::addSubgraph);
		});
	}

	EntityG<?> NONE = Map::of;

	static <A> EntityG<A> of(String... path) {
		return () -> {
			final var hash = new HashMap<String,EntityG<?>>();
			Arrays.stream(path).forEach(p -> hash.put(p,NONE));
			return hash;
		};
	}
}
