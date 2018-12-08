package de.sebastiankopp.severalexamples.bootysoap;

import java.io.IOException;
import java.time.Instant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;


@Configuration
public class JacksonConfig {
	@Bean
	public ObjectMapper objMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Instant.class, new JTInstantDeserializer());
		module.addSerializer(Instant.class, new JTInstantSerializer());
		mapper.registerModule(module);
		mapper.registerModule(new JSR353Module());
		return mapper;
	}
	
	@Bean
	public JacksonJsonProvider jsonProvider(ObjectMapper objMapper) {
		return new JacksonJsonProvider(objMapper);
	}
	
	private static class JTInstantSerializer extends StdSerializer<Instant> {

		private static final long serialVersionUID = 1L;
		
		JTInstantSerializer() {
			super(Instant.class);
		}
		@Override
		public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
			if (value != null)
				gen.writeString(value.toString());
			else
				gen.writeNull();
		}
	}
	
	private static class JTInstantDeserializer extends StdDeserializer<Instant> {

		private static final long serialVersionUID = 1L;
	
		JTInstantDeserializer() {
			super(Instant.class);
		}
		@Override
		public Instant deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			TreeNode treeNode = p.getCodec().readTree(p);
			
			if (treeNode == null || treeNode instanceof NullNode) {
				return null;
			} else if (treeNode instanceof TextNode) {
				return Instant.parse(((TextNode)treeNode).asText());
			} else {
				throw new IllegalArgumentException("Incorrect node type: " + treeNode.getClass());
			}
		}

	}

}
