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

import com.spaneos.ga.tt.domain.Teacher;

@Component
public class FacultyTemplateParser {

	private static final String colNames = "Id,Full Name,Short Name,Max Hrs (Week),Dept,Building Name";
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

	public List<Teacher> parse(String filePath) {

		List<Teacher> facultyList = parseDataFromFile(new File(filePath));
		LOGGER.info("Total Faculty Memebers: {} ", facultyList.size());
	
		for(Teacher faculty:facultyList) {
			System.out.println(faculty.getShortName()+" "+faculty.getName());
		}
		return facultyList;
	}

	private List<Teacher> parseDataFromFile(File file) {
		List<Teacher> facultyList = new ArrayList<Teacher>();
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
				String id = row.getCell(indexOf("Id")) != null
						? ParserUtil.checkAndGetAsString(row.getCell(indexOf("Id")))
						: "";
				String fullName = row.getCell(indexOf("Fu")) != null
						? row.getCell(indexOf("Fu")).getStringCellValue()
						: "";
				String shortName = row.getCell(indexOf("Sh")) != null
						? row.getCell(indexOf("Sh")).getStringCellValue()
						: "";
				/*int maxWeekHrs = (int) (row.getCell(indexOf("Ma")) != null
						? row.getCell(indexOf("Ma")).getNumericCellValue()
						: 0);*/
				
				String deptName = row.getCell(indexOf("De")) != null
						? row.getCell(indexOf("De")).getStringCellValue()
						: "";
				String buildingName = row.getCell(indexOf("Bu")) != null
						? row.getCell(indexOf("Bu")).getStringCellValue()
						: "";
				Teacher faculty = Teacher.builder().id(id).name(fullName).shortName(shortName)
						.deptName(deptName).buildingName(buildingName).build();
				facultyList.add(faculty);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facultyList;
	}

	private int indexOf(String name) {
		return colIndexMap.get(name.toLowerCase());

	}
}
