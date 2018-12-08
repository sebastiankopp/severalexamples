package de.sebastiankopp.severalexamples.bootysoap.soap;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;

public enum CxfInterceptorChain {
	IN(b -> b.getInInterceptors()),
	IN_FAULT(b -> b.getInFaultInterceptors()),
	OUT(b -> b.getOutInterceptors()),
	OUT_FAULT(b -> b.getOutFaultInterceptors());
	
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
