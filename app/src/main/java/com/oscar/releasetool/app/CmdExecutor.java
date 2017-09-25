package com.oscar.releasetool.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.io.FileUtils;

import com.oscar.releasetool.app.common.Config;
import com.oscar.releasetool.app.release.Component;
import com.oscar.releasetool.app.release.Release;
import com.oscar.releasetool.app.release.ReleaseItem;

public class CmdExecutor {
	private CommandLine cmd;

	public CmdExecutor(CommandLine cmd) {
		this.cmd = cmd;
	}
	
	public void execute() {
		initConfig();
		// 备份发布单
		System.out.println("************************************");
		System.out.println(String.format("发布单: %s", Config.getInstance().getReleaseListPath()));
		System.out.println(String.format("负责人: %s", Config.getInstance().getPersonInCharge()));
		System.out.println(String.format("注册组件: %s", Config.getInstance().getActiveComponentCenter()));
		System.out.println("************************************");
		System.out.println(">>备份发布单...");
		File srcFile = new File(Config.getInstance().getReleaseListPath());
		File bak = new File(srcFile.getParentFile(), srcFile.getName() + ".bak");
		try {
			FileUtils.copyFile(srcFile, bak);
		} catch (IOException e1) {
			throw new IllegalStateException("文件备份失败", e1);
		}
		// 获取对应负责人负责的组件列表
		System.out.println(">>读取发布单...");
		Release release = Release.getInstance();
		List<ReleaseItem> items = release.getItemsByPersonInCharge(Config.getInstance().getPersonInCharge());
		// 初始化对应的组件
		System.out.println(">>初始化组件信息...");
		List<Component> components = initComponents(items);
		// 更新发布单label
		System.out.println(">>更新发布单Label...");
		updateRelease(components);
		// 发布单刷回磁盘
		System.out.println(">>刷新磁盘...");
		try {
			release.save();
		} catch (IOException e) {
			throw new IllegalStateException("写发布单文件失败", e);
		}
		System.out.println(">>DONE");
	}

	private void updateRelease(List<Component> components) {
		for (Component component : components) {
			System.out.println("====================================");
			System.out.println(String.format("%s : %s", component.getItem().getSheetName(), component.getComponentName()));
			// 更新发布单中对应组件的分支号
			ReleaseItem item = component.getItem();
			String viewName = item.getViewName();
			if (viewName != null && !"".equals(viewName)) {
				if (!viewName.equals(component.getViewName())) {// config.json中的组件分支号与发布单中的分支号不一致
					throw new IllegalStateException(String.format("组件  %s 分支号错误: config.json %s ，发布单  %s", component.getComponentName(),component.getViewName(), viewName));
				}
			} else {// 发布单中分支号为空
				item.updateCell(item.getViewIndex(), component.getViewName());
			}
			// 检查是否需要新建label
			Date currentViewLabelTime = component.currentViewLabelTime();
			Date lastRevisionLabelTime = component.lastRevisionLabelTime();
			if (!currentViewLabelTime.after(lastRevisionLabelTime)) {// view label创建时间早于revision label
				String newLabelName = null;
				if (Config.getInstance().isSilent()) {// 自动模式
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
					newLabelName = sf.format(new Date()) + "_" + Config.getInstance().getReleaseLabelSuffix();
				} else {
					System.out.println(String.format(">>最新的View Label: %s", component.currentViewLabelName()));
					System.out.println(String.format(">>最新的Revision Label: %s", component.lastRevisionLabelName()));
					System.out.print(">>请输入新建的View Label名:\t");
					while (true) {
						try {
							newLabelName = new BufferedReader(new InputStreamReader(System.in)).readLine();
							break;
						} catch (IOException e) {
							System.out.print(">>请输入新建的View Label名:\t");
						}    
					}
				}
				System.out.println(String.format(">>新建View Label: %s", newLabelName));
				String desc = new File(Config.getInstance().getReleaseListPath()).getName();
				component.updateViewLabel(newLabelName, desc);
			} 
			System.out.println(String.format("label: %s", component.currentViewLabelName()));
			item.updateCell(item.getLabelIndex(), component.currentViewLabelName());
		}
		System.out.println("====================================");
	}

	private List<Component> initComponents(List<ReleaseItem> items) {
		List<Component> components = new ArrayList<>();
		for (ReleaseItem item : items) {
			Component component = null;
			try {
				component = new Component(item);
			} catch (Exception e) {
				System.out.println(">> Warning: " + e.getMessage());
				continue;
			}
			components.add(component);
		}
		return components;
	}

	private void initConfig() {
		if (cmd.hasOption(CLIOptions.HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ReleaseTool [-options]", CLIOptions.options);
		}
		
		if (cmd.hasOption(CLIOptions.PERSON_IN_CHARGE)) {
			String value = cmd.getOptionValue(CLIOptions.PERSON_IN_CHARGE);
			Config.getInstance().setPersonInCharge(value);
		}
		
		if (cmd.hasOption(CLIOptions.ACTIVE_COMPONENT_CENTER)) {
			String value = cmd.getOptionValue(CLIOptions.ACTIVE_COMPONENT_CENTER);
			Config.getInstance().setActiveComponentCenter(value);
			Config.getInstance().reActiveComponentCenter();
		}
		
		if (cmd.hasOption(CLIOptions.SILENT)) {
			Config.getInstance().setSilent(true);
		}
		
		if (cmd.hasOption(CLIOptions.RELEASE_LIST_PATH)) {
			String value = cmd.getOptionValue(CLIOptions.RELEASE_LIST_PATH);
			Config.getInstance().setReleaseListPath(value);
		}
		
		if (cmd.hasOption(CLIOptions.DEFAULT_RELEASE_LABEL_SUFFIX)) {
			String value = cmd.getOptionValue(CLIOptions.DEFAULT_RELEASE_LABEL_SUFFIX);
			Config.getInstance().setReleaseLabelSuffix(value);
		}
	}
}
