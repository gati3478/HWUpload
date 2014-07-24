drop schema if exists hwupload;
create schema  hwupload;

use hwupload;

create table users(
	id int not null auto_increment primary key,
	e_mail varchar(32) not null unique, -- credantials only
	full_name varchar(64),
	status enum('student', 'lecturer', 'section leader') -- student=1, lecturer=2, section leader=3
);

create table courses(
	id int not null auto_increment primary key,
	name varchar(128) not null,
	description text,
	year char(9), -- samwuxarod, shemdeg aTaTaswleulshi aghar imushavebs
	begin date,
	end date
);

create table homework(
	id int not null auto_increment primary key,
	name varchar(40) not null,
	description text,
	number tinyint,
	course_id int,
	foreign key(course_id)
		references courses(id)
		on delete cascade,
	deadline datetime not null,
	active boolean default true,
	forbid_latedays boolean default false
);

create table homework_forms(
	hw_id int,
	foreign key(hw_id)
		references homework(id)
		on delete cascade,
	regex varchar(100),
	max_size int default 10 -- in Mbs
);

create table files(
	id int not null auto_increment primary key,
	filename varchar(100) not null,
	upload_time datetime default now(),
	student_id int, 
	foreign key(student_id)
		references users(id)
		on delete cascade,
	hw_id int,
	foreign key(hw_id)
		references homework(id)
		on delete cascade
);

create table courses_lecturers(
	course_id int,
	foreign key(course_id)
		references courses(id)
		on delete cascade,
	lecturer_id int,
	foreign key(lecturer_id)
		references users(id)
		on delete cascade	
);

create table course_students(
	course_id int,
	foreign key(course_id)
		references courses(id)
		on delete cascade,
	student_id int,
	foreign key(student_id)
		references users(id)
		on delete cascade,
	tutor_id int,
	foreign key(tutor_id)
		references users(id)
		on delete cascade,
	latedays_num int default 0,
	latedays_len int 
);

create table latedays_history(
	student_id int,
	foreign key(student_id)
		references users(id)
		on delete cascade,
	hw_id int,
	foreign key(hw_id)
		references homework(id)
		on delete cascade,
	latedays_taken int
);