package com.vito.common.util.string;

import freemarker.cache.URLTemplateLoader;

import java.net.URL;

public class ClasspathTemplateLoader extends URLTemplateLoader {
	
	private String path;

	public ClasspathTemplateLoader(String path) {
		super();
		if (!path.endsWith("/") && !path.endsWith("\\")) {
			path += "/";
		}
		this.path = path;
	}

	protected URL getURL(String name) {
		return Thread.currentThread().getContextClassLoader().getResource(path + name);
	}

}
