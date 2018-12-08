package de.sebastiankopp.severalexamples.dummyjaxrs;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

public class LocalInlineInjector {
	public static <T> T obtainCdiInstance(Class<? extends T> beanClass, Annotation... injpAnnotations) {
		Map<Boolean, List<Annotation>> annotsByQualifiers = Arrays.stream(injpAnnotations)
				.collect( Collectors.partitioningBy(e -> e.getClass().isAnnotationPresent(Qualifier.class)));
		final CDI<Object> currentCdi = CDI.current();
		final BeanManager beanManager = currentCdi.getBeanManager();
		
		
		beanManager.getBeans(beanClass, new AnnotationLiteral<Any>() {private static final long serialVersionUID = 1L; })
				.forEach(e -> System.out.println(e.getBeanClass().getName()));
		final Annotation[] qualifiers = annotsByQualifiers.getOrDefault(Boolean.TRUE, Collections.emptyList())
				.stream().toArray(Annotation[]::new);
		return currentCdi.select(beanClass, qualifiers).get();
	}
	
	
	
}
