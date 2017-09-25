package com.oscar.releasetool.app.common;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class ClassPathResource {
	private ClassLoader classLoader;
	private String path;

	public ClassPathResource(String path) {
		this.path = path;
	}

	public byte[] getBytes() throws IOException {
		URL url = resolveURL();
		byte[] byteArray = IOUtils.toByteArray(url.openStream());
		return byteArray;
	}
	
	public String getString() throws IOException {
		return new String(getBytes());
	}

	private URL resolveURL() {
		if (this.classLoader == null) {
			classLoader = getDefaultClassLoader();
		}
		URL url = classLoader.getResource(path);
		if (url != null) {
			return url;
		}
		url = ClassLoader.getSystemResource(path);
		if (url == null) {
			throw new IllegalArgumentException(String.format("Error opening %s", path));
		}
		return url;
	}

	private ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
		}
		if (cl == null) {
			cl = ClassPathResource.class.getClassLoader();
			if (cl == null) {
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
				}
			}
		}
		return cl;
	}
}
