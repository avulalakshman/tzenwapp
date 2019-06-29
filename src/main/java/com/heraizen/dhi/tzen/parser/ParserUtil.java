package com.heraizen.dhi.tzen.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.StringUtils;

public final class ParserUtil {

	private ParserUtil() {

	}

	public static boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellType() != CellType.BLANK && !StringUtils.isEmpty(cell.toString())) {
				return false;
			}
		}
		return true;
	}

	public static String checkAndGetAsString(Cell cell) {
		if(cell.getCellType() == CellType.NUMERIC) {
			return ((Integer)(int)cell.getNumericCellValue()).toString();
		}
		return cell.getStringCellValue();
	}
}
