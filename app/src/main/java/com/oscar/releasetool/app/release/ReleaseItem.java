package com.oscar.releasetool.app.release;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.oscar.releasetool.app.common.XlsxUtil;

public class ReleaseItem {
	public static final String COMPONENT_HEAD = "组件名";
	public static final String VIEW_HEAD = "分支名/分支号";
	public static final String LABEL_HEAD = "标签（Label）号";
	public static final String PERSON_IN_CHARGE_HEAD = "负责人";
	// 发布单表头
	private Row head;
	// excel 组件对应行
	private Row row;
	// 所在的sheet页名
	private String sheetName;
	// 组件
	private String[] component;
	// 视图(分支号)
	private String[] view;
	// 编译标签
	private String[] label;
	// 负责人
	private String[] personInCharge;
	
	public ReleaseItem(Row head, Row row) {
		this.head = head;
		this.row = row;
		this.sheetName = head.getSheet().getSheetName();
		init();
	}

	private void init() {
		component = new String[2];
		int componentIndex = getCellIndexByValue(COMPONENT_HEAD);
		String componentName = XlsxUtil.getStringValue(row.getCell(componentIndex));
		component[0] = String.valueOf(componentIndex);
		component[1] = componentName;
		
		view = new String[2];
		int viewIndex = getCellIndexByValue(VIEW_HEAD);
		String viewName = XlsxUtil.getStringValue(row.getCell(viewIndex));
		view[0] = String.valueOf(viewIndex);
		view[1] = viewName;
		
		label = new String[2];
		int labelIndex = getCellIndexByValue(LABEL_HEAD);
		String labelName = XlsxUtil.getStringValue(row.getCell(labelIndex));
		label[0] = String.valueOf(labelIndex);
		label[1] = labelName;
		
		personInCharge = new String[2];
		int personInChargeIndex = getCellIndexByValue(PERSON_IN_CHARGE_HEAD);
		String personInChargeName = XlsxUtil.getStringValue(row.getCell(personInChargeIndex));
		personInCharge[0] = String.valueOf(personInChargeIndex);
		personInCharge[1] = personInChargeName;
	}
	
	/**
	 * 根据表头内容获取对应的列序号
	 * @param value
	 * @return
	 */
	private int getCellIndexByValue(String value) {
		for (int i = 0; i < head.getRowNum(); i++) {
			Cell cell = head.getCell(i);
			if (cell == null) {
				continue;
			}
			if (value.equals(XlsxUtil.getStringValue(cell))) {
				return i;
			}
		}
		throw new IllegalStateException(String.format("Cell %s not exist", value));
	}
	
	public void updateCell(int index, String value) {
		Cell cell = row.getCell(index);
		cell.setCellValue(value);
	}
	
	public int getComponentIndex() {
		return Integer.valueOf(component[0]);
	}
	
	public String getComponentName() {
		return component[1];
	}
	
	public int getLabelIndex() {
		return Integer.valueOf(label[0]);
	}
	
	public String getLabelName() {
		return label[1];
	}
	
	public int getPersonInChargeIndex() {
		return Integer.valueOf(personInCharge[0]);
	}
	
	public String getPersonInChargeName() {
		return personInCharge[1];
	}
	
	public int getViewIndex() {
		return Integer.valueOf(view[0]);
	}
	
	public String getViewName() {
		return view[1];
	}

	public String getSheetName() {
		return sheetName;
	}

	@Override
	public String toString() {
		return "ReleaseItem [sheetName=" + sheetName + ", component=" + Arrays.toString(component) + ", view="
				+ Arrays.toString(view) + ", label=" + Arrays.toString(label) + ", personInCharge="
				+ Arrays.toString(personInCharge) + "]";
	}
	
}
