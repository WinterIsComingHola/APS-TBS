package com.roli.common.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DeserializationProblemHandler extends com.fasterxml.jackson.databind.deser.DeserializationProblemHandler {

	private static final Logger logger = LoggerFactory.getLogger(DeserializationProblemHandler.class);
	@Override
	public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer,
			Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
	    if (logger.isWarnEnabled()) {
	        logger.warn("unknownproperty:" + beanOrClass.getClass().getSimpleName() + "." + propertyName);
	    }
		return super.handleUnknownProperty(ctxt, jp, deserializer, beanOrClass, propertyName);
	}
}
