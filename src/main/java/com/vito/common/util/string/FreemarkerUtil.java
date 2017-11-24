package com.vito.common.util.string;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtil {

	public static String formatTemplateStr(String templateStr, Object rootMap) {
		try {
			if (rootMap == null) {
				return templateStr; 
			}
			Configuration cfg = new Configuration();
			cfg.setTemplateLoader(new StringTemplateLoader(templateStr));
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate("");
			StringWriter writer = new StringWriter();
			template.process(rootMap, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void processClasspathTemplate(String classpathTemplatePath, String templateName, Object rootMap, Writer writer) {
		Configuration configuration = new Configuration();
		TemplateLoader templateLoader = new ClasspathTemplateLoader(classpathTemplatePath);
		configuration.setTemplateLoader(templateLoader);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setWhitespaceStripping(true);
		configuration.setDefaultEncoding(EncodeUtil.UTF_8);
		try {
			configuration.getTemplate(templateName).process(rootMap, writer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("test", "London奥运");
		System.out.println(FreemarkerUtil.formatTemplateStr("测试${test}", rootMap));
	}

	static class StringTemplateLoader implements TemplateLoader {

		private String template;

		public StringTemplateLoader(String template) {
			this.template = template;
			if (template == null) {
				this.template = "";
			}
		}

		public void closeTemplateSource(Object templateSource)
				throws IOException {
			((StringReader) templateSource).close();
		}

		public Object findTemplateSource(String name) throws IOException {
			return new StringReader(template);
		}

		public long getLastModified(Object templateSource) {
			return 0;
		}

		public Reader getReader(Object templateSource, String encoding)
				throws IOException {
			return (Reader) templateSource;
		}

	}

}
