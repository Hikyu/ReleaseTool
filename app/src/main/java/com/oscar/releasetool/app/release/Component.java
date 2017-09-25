package com.oscar.releasetool.app.release;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.oscar.releasetool.app.common.Config;
import com.starbase.starteam.Label;
import com.starbase.starteam.StarTeamFinder;
import com.starbase.starteam.StarTeamURL;
import com.starbase.starteam.View;
import com.starbase.util.OLEDate;

public class Component {
	// 组件名称
	private String componentName;
	// 视图名称(分支号)
	private String viewName;
	// Starteam路径
	private String path;
	// 组件负责人
	private String personInCharge;
	// 当前view label
	private Label currentViewLabel;
	// 当前revision label
	private Label lastRevisionLabel;
	// 组件Starteam视图
	private View view;
	// 发布单对应item
	private ReleaseItem item;
	
	private Component() {}
	
	public Component(ReleaseItem item) {
		this.item = item;
		this.componentName = item.getComponentName();
		this.viewName = Config.getInstance().getComponentView(componentName);
		this.path = Config.getInstance().getComponentPath(componentName);
		if (viewName == null || path == null) {
			throw new IllegalStateException(String.format("config.json中找不到组件[%s]的配置信息", componentName));
		}
		this.personInCharge = item.getPersonInChargeName();
		initComponentInfo();
	}
	
	private void initComponentInfo() {
		Config config = Config.getInstance();
		StarTeamURL starTeamURL = new StarTeamURL(config.getStURL() + path);
		if (starTeamURL.getProjectName() == null) {
			throw new IllegalStateException(String.format("Project %s not exist", starTeamURL.getPath()));
		}
		view = StarTeamFinder.openView(starTeamURL.getSource());
		List<Label> labels = Arrays.asList(view.getActiveLabels());
		Collections.sort(labels, new LabelTimeComparator());
		for (Label label : labels) {
			if (currentViewLabel == null && label.isViewLabel()) {
				currentViewLabel = label;
			} else if (lastRevisionLabel == null && label.isRevisionLabel()) {
				lastRevisionLabel = label;
			}
			if (currentViewLabel != null && lastRevisionLabel != null) {
				break;
			}
		}
	}

	public void updateViewLabel(String name, String description) {
		Label label = view.createViewLabel(name, description, new OLEDate(), true, false);
		view.refreshLabels();
		currentViewLabel = label;
	}
	
	public Date currentViewLabelTime() {
		if (currentViewLabel == null) {
			//long long ago...
			return new Date(Long.MIN_VALUE);
		}
		return currentViewLabel.getRevisionTime().createDate();
	}
	
	public Date lastRevisionLabelTime() {
		if (lastRevisionLabel == null) {
			// long long ago...
			return new Date(Long.MIN_VALUE);
		}
		return lastRevisionLabel.getRevisionTime().createDate();
	}
	
	public String currentViewLabelName() {
		if (currentViewLabel == null) {
			return null;
		}
		return currentViewLabel.getName();
	}
	
	public String lastRevisionLabelName() {
		if (lastRevisionLabel == null) {
			return null;
		}
		return lastRevisionLabel.getName();
	}
	
	public String getComponentName() {
		return componentName;
	}

	public String getViewName() {
		return viewName;
	}

	public String getPath() {
		return path;
	}

	public String getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}
	
	public ReleaseItem getItem() {
		return item;
	}



	/**
	 * label排序，日期由近到远
	 * @author Yukai
	 *
	 */
	static class LabelTimeComparator implements Comparator<Label> {

		@Override
		public int compare(Label o1, Label o2) {
			Date date1 = o1.getRevisionTime().createDate();
			Date date2 = o2.getRevisionTime().createDate();
			return date2.compareTo(date1);
		}
		
	}
	
}
