package com.oscar.releasetool.app;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CLIOptions {
	public static Options options;
	public static final String SILENT = "s";
	public static final String PERSON_IN_CHARGE = "u";
	public static final String HELP = "h";
	public static final String DEFAULT_RELEASE_LABEL_SUFFIX = "l";
	public static final String RELEASE_LIST_PATH = "r";
	public static final String ACTIVE_COMPONENT_CENTER = "a";
	static {
		options = new Options();
		config();
	}

	private static Options config() {
		Option help = new Option(HELP, "打印帮助信息");
		Option silent = Option.builder(SILENT)
				.desc("启动自动模式，自动新建Build Label")
				.build();

		Option personInCharge = Option.builder(PERSON_IN_CHARGE)
				.desc("组件负责人")
				.argName("personInCharge")
				.hasArg(true)
				.build();
		Option labelSuffix = Option.builder(DEFAULT_RELEASE_LABEL_SUFFIX)
				.desc("默认的Label后缀，Label格式为 \"yyyyMMddHHmmss_suffix\"，" + "仅在自动模式下生效")
				.argName("suffix")
				.hasArg(true)
				.build();
		
		Option releasePath = Option.builder(RELEASE_LIST_PATH)
				.desc("发布单路径")
				.argName("releasePath")
				.hasArg(true)
				.build();
		
		Option activeComponentCenter = Option.builder(ACTIVE_COMPONENT_CENTER)
				.desc("激活的组件列表")
				.argName("activeComponentCenter")
				.hasArg(true)
				.build();

		options.addOption(help)
			.addOption(silent)
			.addOption(personInCharge)
			.addOption(releasePath)
			.addOption(labelSuffix)
			.addOption(activeComponentCenter);

		return options;
	}
}
