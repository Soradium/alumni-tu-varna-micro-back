-- Drop all dependent tables in the correct order due to foreign key constraints
DROP TABLE IF EXISTS alumni_group_membership CASCADE;
DROP TABLE IF EXISTS alumni_group_memberships CASCADE;
DROP TABLE IF EXISTS company_records CASCADE;
DROP TABLE IF EXISTS alumni_details CASCADE;
DROP TABLE IF EXISTS alumni_groups CASCADE;
DROP TABLE IF EXISTS alumni CASCADE;
DROP TABLE IF EXISTS alumnus CASCADE;
DROP TABLE IF EXISTS speciality CASCADE;
DROP TABLE IF EXISTS specialities CASCADE;
DROP TABLE IF EXISTS degree CASCADE;
DROP TABLE IF EXISTS degrees CASCADE;
DROP TABLE IF EXISTS faculty CASCADE;
DROP TABLE IF EXISTS faculties CASCADE;

-- FACULTY
CREATE TABLE faculties (
    faculty_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    faculty_name VARCHAR(100) NOT NULL
);

-- DEGREE
CREATE TABLE degrees (
    degree_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    degree_name VARCHAR(100) NOT NULL
);

-- SPECIALITY
CREATE TABLE specialities (
    speciality_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    speciality_name VARCHAR(100) NOT NULL
);

-- ALUMNI
CREATE TABLE alumni (
    faculty_number INT PRIMARY KEY,
    facebook_url VARCHAR(300),
    linkedin_url VARCHAR(300),
    degree_id INT REFERENCES degrees(degree_id)
);
CREATE INDEX idx_alumni_degree_id ON alumni(degree_id);

-- ALUMNI DETAILS
CREATE TABLE alumni_details (
    faculty_number INT PRIMARY KEY REFERENCES alumni(faculty_number) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    faculty_id INT REFERENCES faculties(faculty_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_details_faculty_id ON alumni_details(faculty_id);

-- COMPANY RECORDS
CREATE TABLE company_records (
    company_record_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    faculty_number INT NOT NULL REFERENCES alumni(faculty_number) ON DELETE CASCADE,
    enrollment_date DATE NOT NULL,
    discharge_date DATE,
    position_name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (discharge_date IS NULL OR enrollment_date <= discharge_date)
);
CREATE INDEX idx_company_faculty_number ON company_records(faculty_number);

-- ALUMNI GROUPS
CREATE TABLE alumni_groups (
    groups_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    faculty_id INT REFERENCES faculties(faculty_id),
    group_number INT NOT NULL,
    graduation_year INT NOT NULL,
    speciality_id INT REFERENCES specialities(speciality_id)
);
CREATE INDEX idx_groups_faculty_id ON alumni_groups(faculty_id);

-- GROUP MEMBERSHIP
CREATE TABLE alumni_group_memberships (
    group_member_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    faculty_number INT NOT NULL REFERENCES alumni(faculty_number) ON DELETE CASCADE,
    groups_id INT NOT NULL REFERENCES alumni_groups(groups_id) ON DELETE CASCADE,
    average_score FLOAT,
    UNIQUE(faculty_number, groups_id)
);
CREATE INDEX idx_membership_groups_id ON alumni_group_memberships(groups_id);
