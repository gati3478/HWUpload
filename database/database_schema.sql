DROP SCHEMA IF EXISTS hwupload;
CREATE SCHEMA  hwupload;
USE hwupload;

CREATE TABLE users (
	ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	email_creds VARCHAR(32) NOT NULL UNIQUE, -- credantials only (like gpetr12, not gpetr12@freeuni.edu.ge)
	full_name NVARCHAR(64),
	status ENUM('student', 'lecturer', 'section leader') -- student = 1, lecturer = 2, section leader = 3
);

CREATE TABLE courses (
	ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name NVARCHAR(128) NOT NULL,
	description TEXT,
	begin_date DATE,
	end_date DATE
) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE homework (
	ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name NVARCHAR(64) NOT NULL,
	description TEXT,
	number TINYINT,
	course_id INT,
	deadline DATETIME NOT NULL,
	active BOOLEAN DEFAULT TRUE,
	forbid_latedays BOOLEAN DEFAULT FALSE,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
) DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE homework_forms (
	hw_id INT,
	regex NVARCHAR(128),
	max_filesize INT DEFAULT 12, -- in MBs
	FOREIGN KEY (hw_id) REFERENCES homework(id) ON DELETE CASCADE
);

CREATE TABLE files (
	ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	filename NVARCHAR(256) NOT NULL,
	upload_time DATETIME DEFAULT NOW(),
	student_id INT,
	hw_id INT,
	FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (hw_id) REFERENCES homework(id) ON DELETE CASCADE
);

CREATE TABLE courses_lecturers (
	course_id INT,
	lecturer_id INT,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
	FOREIGN KEY (lecturer_id) REFERENCES users(id) ON DELETE CASCADE	
);

CREATE TABLE course_students (
	course_id INT,
	latedays_num INT DEFAULT 0,
	latedays_len INT,
	student_id INT,
	tutor_id INT,
	FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
	FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (tutor_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE latedays_history (
	student_id INT,
	hw_id INT,
	latedays_taken INT,
	FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
	FOREIGN KEY (hw_id) REFERENCES homework(id) ON DELETE CASCADE
);