package de.sebikopp.bootysoap.jsf.control;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> viewMap = getViewMap();
		viewMap.computeIfAbsent(name, x -> objectFactory.getObject());
		return viewMap.get(name);
	}

	private Map<String, Object> getViewMap() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
	}

	@Override
	public Object remove(String name) {
		return getViewMap().remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

}
