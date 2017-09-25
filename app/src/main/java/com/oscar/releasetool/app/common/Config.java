package com.oscar.releasetool.app.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Config {
	private static Config instance = new Config();
	private static final String PATH = "config.json";
	// Starteam url
	private String stURL;
	// 发布单文件路径
	private String releaseListPath;
	// 责任人
	private String personInCharge;
	// 组件列表
	private Map<String, String[]> componentRegistry;
	// 自动模式
	private boolean silent = false;
	// 默认标签后缀
	private String releaseLabelSuffix;
	// 激活的组件配置列表
	private String activeComponentCenter;
	// 组件列表json对象
	private JsonObject componentCenterObj;
	
	private Config() {
		String resource = null;
		try {
			resource = new ClassPathResource(PATH).getString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		JsonParser jsonParser = new JsonParser();
		JsonObject root = jsonParser.parse(resource).getAsJsonObject();
		stURL = root.get("stURL").getAsString();
		releaseListPath = root.get("releaseListPath").getAsString();
		personInCharge = root.get("personInCharge").getAsString();
		releaseLabelSuffix = root.get("releaseLabelSuffix").getAsString();
		activeComponentCenter = root.get("activeComponentCenter").getAsString();
		componentCenterObj = root.get("componentCenter").getAsJsonObject();
		
		componentRegistry = new HashMap<>();
		initComponentRegistry(componentRegistry, activeComponentCenter);
	}
	
	/**
	 * 初始化组件配置列表
	 * @param registry
	 * 			注册器
	 * @param active
	 * 			激活的配置
	 */
	private void initComponentRegistry(Map<String, String[]> registry, String active) {
		JsonArray componentList = componentCenterObj.get(active).getAsJsonArray();
		Iterator<JsonElement> iterator = componentList.iterator();
		while (iterator.hasNext()) {
			JsonElement component = (JsonElement) iterator.next();
			String key = component.getAsJsonObject().get("name").getAsString();
			String[] info = new String[2];
			info[0] = component.getAsJsonObject().get("view").getAsString();
			info[1] = component.getAsJsonObject().get("path").getAsString();
			registry.put(key, info);
		}
	}

	public static Config getInstance() {
		return instance;
	}

	public void reActiveComponentCenter() {
		componentRegistry = new HashMap<>();
		initComponentRegistry(componentRegistry, activeComponentCenter);
	}
	
	public String getStURL() {
		return stURL;
	}

	public String getReleaseListPath() {
		return releaseListPath;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}
	
	public String getComponentView(String componentName) {
		String[] strings = componentRegistry.get(componentName);
		if (strings == null) {
			return null;
		}
		return strings[0];
	}
	
	public String getComponentPath(String componentName) {
		String[] strings = componentRegistry.get(componentName);
		if (strings == null) {
			return null;
		}
		return strings[1];
	}

	public void setReleaseListPath(String releaseListPath) {
		this.releaseListPath = releaseListPath;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public String getReleaseLabelSuffix() {
		return releaseLabelSuffix;
	}

	public void setReleaseLabelSuffix(String releaseLabelSuffix) {
		this.releaseLabelSuffix = releaseLabelSuffix;
	}

	public String getActiveComponentCenter() {
		return activeComponentCenter;
	}

	public void setActiveComponentCenter(String activeComponentCenter) {
		this.activeComponentCenter = activeComponentCenter;
	}
	
}
