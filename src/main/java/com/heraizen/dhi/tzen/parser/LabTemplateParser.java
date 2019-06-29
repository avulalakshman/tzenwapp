package com.heraizen.dhi.tzen.parser;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.spaneos.ga.tt.domain.LabInfo;


@Component
public class LabTemplateParser {

	private static final String colNames = "Lab Name,Capacity,Dept,Building Name";
	private static final List<String> colNamesList;
	private static final Map<String, Integer> colIndexMap;
	private static final Logger LOGGER = LoggerFactory.getLogger(FacultyTemplateParser.class.getName());
	static {
		colNamesList = Stream.of(colNames.split(",")).collect(toList());
		colIndexMap = new HashMap<String, Integer>();
		for (int i = 0; i < colNamesList.size(); i++) {
			colIndexMap.put(colNamesList.get(i).substring(0, 2).toLowerCase(), i);
		}
	}

	public List<LabInfo> parse(String filePath) {

		List<LabInfo> labDetailsList = parseDataFromFile(new File(filePath));
		LOGGER.info("Total Laboraties count is : {} ", labDetailsList.size());
		System.out.println("Lab Details:");
		for (LabInfo labDetails : labDetailsList) {
			System.out.println(labDetails.getLabRoomName());
		}
		return labDetailsList;
	}

	private List<LabInfo> parseDataFromFile(File file) {
		List<LabInfo> labDetailsList = new ArrayList<>();
		try (InputStream inp = new FileInputStream(file)) {
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			LOGGER.info("Sheet: {} is ready to process", sheet.getSheetName());
			Iterator<Row> iterator = sheet.rowIterator();
			int i = 1;
			while (iterator.hasNext()) {
				Row row = iterator.next();
				if (ParserUtil.checkIfRowIsEmpty(row)) {
					continue;
				}
				if (i == 1) {
					i++;
					continue;
				}

				String labName = row.getCell(indexOf("La")) != null ? row.getCell(indexOf("La")).getStringCellValue()
						: "";

				int capacity = (int) (row.getCell(indexOf("Ca")) != null
						? row.getCell(indexOf("Ca")).getNumericCellValue()
						: 0);
				String deptName = row.getCell(indexOf("De")) != null ? row.getCell(indexOf("De")).getStringCellValue()
						: "";
				String buildingName = row.getCell(indexOf("Bu")) != null
						? row.getCell(indexOf("Bu")).getStringCellValue()
						: "";
						LabInfo labDetails = LabInfo.builder().labRoomName(labName).capacity(capacity).deptName(deptName)
						.buildingName(buildingName).build();
						
				labDetailsList.add(labDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return labDetailsList;
	}

	private int indexOf(String name) {
		return colIndexMap.get(name.toLowerCase());

	}
}
