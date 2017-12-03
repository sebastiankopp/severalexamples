# severalexamples
Contains several proofs of concepts in order to present a variety of technologies

## Dummyjaxrs
dummyjaxrs is a "kitchensink" maven project including several examples for Java EE.

## Bootysoap
Bootysoap is similar, but based on Spring Boot and serves as a prooof of concept for REST.

## JAXB.MTOM
This Maven project shows how to realise MTOM outside of JAX-WS (e. g. when using a custom transport protocol).
Core elements are the AttachmentMarshaller and AttachmentUnmarshaller as well as a custom validation. 

The latter one is a specific problem since the "dumb" usage of XSD-based validation on an XML schema instance is something like a "forced violation" of the XSD rules as the used base64Binary is a simple type although the real XML contains an xop:include element which is not considered as simple.
