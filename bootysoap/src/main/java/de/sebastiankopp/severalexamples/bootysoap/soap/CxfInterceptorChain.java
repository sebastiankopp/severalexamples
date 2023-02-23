package de.sebastiankopp.severalexamples.bootysoap.soap;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.message.Message;

public enum CxfInterceptorChain {
	IN(InterceptorProvider::getInInterceptors),
	IN_FAULT(InterceptorProvider::getInFaultInterceptors),
	OUT(InterceptorProvider::getOutInterceptors),
	OUT_FAULT(InterceptorProvider::getOutFaultInterceptors);
	
	private final Function<? super Bus, ? extends List<Interceptor<? extends Message>>> chainExtractor;
	private CxfInterceptorChain(Function<? super Bus, ? extends List<Interceptor<? extends Message>>> chainExtractor) {
		this.chainExtractor = chainExtractor;
	}
	
	public static void instrumentateBus(Bus bus, Interceptor<? extends Message> interceptor, CxfInterceptorChain... interceptorChains) {
		Stream.of(interceptorChains).forEachOrdered(c -> {
				List<Interceptor<? extends Message>> interceptorChain = c.chainExtractor.apply(bus);
				if (interceptorChain.stream().noneMatch(interceptor.getClass()::isInstance)) {
					interceptorChain.add(interceptor);
				}
			});
	}
}
