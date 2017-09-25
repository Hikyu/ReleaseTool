package com.oscar.releasetool.app.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxUtil {
	public static Workbook getWorkBook(String path) {
		try {
			FileInputStream inputStream = new FileInputStream(path);
			return WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(String.format("Read .xlsx file %s failed", path));
		}
	}

	public static boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
				return false;
		}
		return true;
	}

	public static void save2File(Workbook workbook, String filePath) throws IOException {
		OutputStream outputStream = new FileOutputStream(filePath);
		workbook.write(outputStream);
	}
	
	public static String getStringValue(Cell cell) {
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell);
	}
}
