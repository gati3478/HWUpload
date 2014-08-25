package hwu.util;

import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;

import java.io.File;
import java.util.List;

// importebi aklia

public class ExcelParser {
		
	public static void getStudentList(FileInputStream file, List<Student> students, List<User> tutors) {
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();
			while(rows.hasNext()) {
				Iterator<Cell> cells = rows.next().cellIterator();
				// name; not needed
				cells.next();
				// adding student (by e-mail)
				students.add(new Student(cells.next().getStringCellValue()));
				// adding tutor if any
				// Student used as Tutor because it has an e-mail only constructor
				if(cells.hasNext())	tutors.add(new Student(cells.next().getStringCellValue()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
