-- non-trivial scripts
USE hwupload;

-- Student cabinet
SELECT courses.name, courses.ID
	FROM (SELECT * FROM course_students WHERE student_id=1) AS cur_student 
		LEFT JOIN courses ON course_id
		WHERE courses.start_date <= NOW() AND courses.end_date >= NOW(); 

-- Lecturer cabinet
SELECT courses.name, courses.start_date, courses.end_date, courses.ID
	FROM (SELECT * FROM courses_lecturers WHERE lecturer_id = 1) AS cur_lecturer
		LEFT JOIN courses ON course_id;
		

-- Tutor cabinet
SELECT courses.name, courses.ID
	FROM (SELECT * FROM courses_tutors WHERE tutor_id = 1) AS cur_tutor 
		LEFT JOIN courses ON course_id
		WHERE courses.start_date <= NOW() AND courses.end_date >= NOW();

-- Collect all homework for current lecturer(trivial select from files by homework id)
-- Note: each time a student resends homework, row is updated in 'files'
-- rather than adding a new one. Physically, old files are removed from 
-- corresponding folder and the new ones are added. This is done to prevent
-- server overload as well as unnecessary complication of sql scripts