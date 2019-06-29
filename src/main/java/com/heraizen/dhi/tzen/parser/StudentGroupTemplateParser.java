package com.heraizen.dhi.tzen.parser;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.SubjectAllocation;
import com.spaneos.ga.tt.domain.TeacherAllocation;

@Component
public class StudentGroupTemplateParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentGroupTemplateParser.class.getName());
	private static final Map<String, Integer> colMap;
	private static final List<String> colName;
	static {
		colName = Stream
				.of("Student Group,Size,Dept Name,Subject Name,Weekly Hrs,Slot Duration,Teacher Name,Lab Names,Hours,Auto Dist"
						.split(","))
				.collect(toList());
		colMap = new HashMap<String, Integer>();
		for (int i = 0; i < colName.size(); i++) {
			colMap.put(colName.get(i).substring(0,2).toLowerCase(), i);
		}
	

	}

	public List<StudentGroup> processStudentGroup(String filePath) {
		List<StudentGroup> studentGroups = studentGroupDetails(new File(filePath));
		LOGGER.info("Total Group Found:{}" , studentGroups.size());
		for (StudentGroup sg : studentGroups) {
			LOGGER.info("{} and {}",sg.getName(),sg.getSize());
			LOGGER.info("Subject Allocation Details:");
			for (SubjectAllocation sa : sg.getSubjectAllocations()) {
				LOGGER.info("\tSubject name:" + sa.getSubjectName() + " Hrs " + sa.getWeeklyHrs() + " Slot Dur:"
						+ sa.getSlotDuration() + " Auto Distribution:" + sa.isAutoDistributeLoad());
				System.out.println("\tFaculty Information:");
				for (TeacherAllocation ta : sa.getTeacherAllocations()) {
					System.out.println("\t\t" + ta.getTeacherName() + " - " + ta.getWeeklyAllocation());
				}
				
			}
		}
		return studentGroups;
	}

	public List<StudentGroup> studentGroupDetails(File file) {
		Map<String, List<Row>> map = readContentFromTemplate(file);
		List<StudentGroup> studentGroups = new ArrayList<>();
		map.entrySet().stream().forEach(ele -> {
			String groupName = ele.getKey();
			Row row = ele.getValue().get(0);
			int size = (int) row.getCell(indexOf("Si")).getNumericCellValue();
			String deptName = row.getCell(indexOf("De"))!=null?row.getCell(indexOf("De")).getStringCellValue():"";
			SubjectAllocation subjectAllocation = null;
			String subjectName = null;
			List<SubjectAllocation> subjectAllocations = new ArrayList<>();

			for (Row r : ele.getValue()) {
				if (r.getCell(3) != null) {
					subjectName = r.getCell(indexOf("Su")).getStringCellValue();
					int weeklyHrs = (int) r.getCell(indexOf("We")).getNumericCellValue();
					int slotDuration = (int) r.getCell(indexOf("Sl")).getNumericCellValue();
					String teacherName = r.getCell(indexOf("Te")).getStringCellValue();
					int weeklyAllocation = (int) r.getCell(indexOf("Ho")).getNumericCellValue();
					String labNames = r.getCell(indexOf("La")) != null
							? r.getCell(indexOf("La")).getStringCellValue()
							: "";
					String autoDistribution = r.getCell(indexOf("Au")) != null
							? r.getCell(indexOf("Au")).getStringCellValue()
							: "";
					TeacherAllocation teacherAllocation = new TeacherAllocation(teacherName, weeklyAllocation);

					subjectAllocation = SubjectAllocation.builder().subjectName(subjectName)
							.weeklyHrs(weeklyHrs)
						
							.slotDuration(slotDuration)
							.labNames(Arrays.stream(labNames.split(",")).collect(Collectors.toSet()))
							.teacherAllocations(Stream.of(teacherAllocation).collect(toList()))
							.autoDistributeLoad(autoDistribution.equalsIgnoreCase("y")).build();
					subjectAllocations.add(subjectAllocation);
				} else {

					String teacherName = r.getCell(indexOf("Te")).getStringCellValue();
					int weeklyAllocation = (int) r.getCell(indexOf("Ho")).getNumericCellValue();
					TeacherAllocation teacherAllocation = new TeacherAllocation(teacherName, weeklyAllocation);
					subjectAllocation.getTeacherAllocations().add(teacherAllocation);

				}

			}
			StudentGroup studentGroup = StudentGroup.builder().name(groupName).size(size)
					.deptName(deptName)
					.subjectAllocations(subjectAllocations).build();
			studentGroups.add(studentGroup);

		});
		return studentGroups;
	}

	private int indexOf(String name) {
		return colMap.get(name.toLowerCase());

	}

	private Map<String, List<Row>> readContentFromTemplate(File file) {
		LOGGER.info("Input file: {} ready for process", file.getName());
		Map<String, List<Row>> groupMap = new HashMap<>();
		try (InputStream inp = new FileInputStream(file)) {
			Workbook wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			LOGGER.info("Sheet: {} is ready to process", sheet.getSheetName());
			Iterator<Row> iterator = sheet.rowIterator();
			String key = null;
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
				if (row.getCell(0) != null && !isHeading(row.getCell(0))) {
					key = row.getCell(0).getStringCellValue();
					groupMap.put(key, Stream.of(row).collect(Collectors.toList()));
				} else {
					List<Row> tempList = groupMap.get(key);
					tempList.add(row);
					groupMap.put(key, tempList);
				}
			}
		} catch (IOException e) {
			LOGGER.debug("Input file is not found in the given location");
		}
		return groupMap;
	}

	private boolean isHeading(Cell cell) {
		return (cell.getStringCellValue().equalsIgnoreCase(colName.get(0)));
	}

	

}
