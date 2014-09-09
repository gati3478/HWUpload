package hwu.util;

import hwu.datamodel.users.Lecturer;
import hwu.datamodel.users.Student;
import hwu.datamodel.users.User;
import hwu.db.managers.UserManager;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {

	public static void getStudentList(InputStream file, List<Student> students,
			List<User> tutors) {
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();
			while (rows.hasNext()) {
				Iterator<Cell> cells = rows.next().cellIterator();
				// name; not needed
				cells.next();
				// adding student (by e-mail)
				students.add(new Student(UserManager.getCreds(cells.next()
						.getStringCellValue())));
				// adding tutor if any
				if (cells.hasNext()) {
					User tutor;
					String email_cred = UserManager.getCreds(cells.next()
							.getStringCellValue());
					tutor = UserManager.isStudentEmail(email_cred) ? new Student(
							email_cred) : new Lecturer(email_cred);
					tutors.add(tutor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
