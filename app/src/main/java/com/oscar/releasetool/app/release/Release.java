package com.oscar.releasetool.app.release;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.oscar.releasetool.app.common.Config;
import com.oscar.releasetool.app.common.XlsxUtil;

public class Release {
	private static Release instance = new Release();
	private Workbook release;
	private List<ReleaseItem> releaseItems;
	
	private Release(){
		releaseItems = new ArrayList<>();
		String releasePath = Config.getInstance().getReleaseListPath();
		release = XlsxUtil.getWorkBook(releasePath);
		initReleaseItems();
	}
	
	private void initReleaseItems() {
		for (int i = 0; i < release.getNumberOfSheets(); i++) {
			Sheet hssfSheet = release.getSheetAt(i);
			Iterator<Row> rowIterator = hssfSheet.iterator();
			Row head = null;
			while (rowIterator.hasNext()) {
				Row row = (Row) rowIterator.next();
				if (row == null) {
					continue;
				}
				if (isReleaseHeadRow(row)) {
					head = row;
					break;
				}
			}
			if (head == null) {
				// 不是发布单所在的sheet
				continue;
			}
			for (int rowIndex = head.getRowNum() + 1; rowIndex <= hssfSheet.getLastRowNum(); rowIndex++) {
				Row row = hssfSheet.getRow(rowIndex);
				if (XlsxUtil.isRowEmpty(row)) {
					// 该行内容为空，发布单表格结束
					break;
				}
				ReleaseItem item = new ReleaseItem(head, row);
				releaseItems.add(item);
			}
		}
	}
	
	
	/**
	 * 是否为发布单表格表头所在的行
	 * 如果单元格值为"组件名"，且相邻单元格值为"分支名/分支号"，则认为该单元格所在的行为发布单表头
	 * @param row
	 * @return
	 */
	private boolean isReleaseHeadRow(Row row) {
		for (int i = 0; i < row.getRowNum(); i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			if (ReleaseItem.COMPONENT_HEAD.equals(XlsxUtil.getStringValue(cell))) {
				cell = row.getCell(i + 1);
				if (cell == null) {
					continue;
				}
				if (ReleaseItem.VIEW_HEAD.equals(XlsxUtil.getStringValue(cell))) {
					return true;
				}
			}
		}
		return false;
	}

	public static Release getInstance() {
		return instance;
	}
	
	public void save() throws IOException {
		XlsxUtil.save2File(release, Config.getInstance().getReleaseListPath());
	}
	
	public List<ReleaseItem> getItemsByPersonInCharge(String personInCharge) {
		List<ReleaseItem> items = new ArrayList<>();
		for (ReleaseItem item : releaseItems) {
			if (personInCharge.equals(item.getPersonInChargeName())) {
				items.add(item);
			}
		}
		return items;
	}
}
