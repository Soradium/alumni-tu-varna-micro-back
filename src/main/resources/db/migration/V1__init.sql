-- Table for external/social links (one per alumni)
CREATE TABLE api_base (
                          id SERIAL PRIMARY KEY,
                          facebook_url VARCHAR(255),
                          linkedin_url VARCHAR(255)
);

-- Faculties
CREATE TABLE faculties (
                           faculty_id INT PRIMARY KEY,
                           faculty_name VARCHAR(50) NOT NULL
);

-- Alumni table links to api_base for social media
CREATE TABLE alumni (
                        id SERIAL PRIMARY KEY,
                        api_base_id INT REFERENCES api_base(id),
                        degree VARCHAR(255) NOT NULL
);

-- Alumni personal details
CREATE TABLE alumni_details (
                                alumni_id INT PRIMARY KEY REFERENCES alumni(id) ON DELETE CASCADE,
                                full_name VARCHAR(255) NOT NULL,
                                faculty_number VARCHAR(100) NOT NULL UNIQUE,
                                birth_date DATE,
                                faculty_id INT REFERENCES faculties(faculty_id)
);

-- Company records (each belongs to one alumni, 1:N)
CREATE TABLE company_records (
                                 id SERIAL PRIMARY KEY,
                                 alumni_id INT NOT NULL REFERENCES alumni(id) ON DELETE CASCADE,
                                 enrollment_date DATE NOT NULL,
                                 discharge_date DATE,
                                 position VARCHAR(255) NOT NULL,
                                 company_name VARCHAR(255) NOT NULL,
                                 CHECK (discharge_date IS NULL OR enrollment_date <= discharge_date)
);

-- Application users (optional login for alumni)
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       alumni_id INT REFERENCES alumni(id) ON DELETE SET NULL
);

-- User roles (e.g. admin, editor)
CREATE TABLE user_roles (
                            username VARCHAR(255) NOT NULL,
                            role VARCHAR(255) NOT NULL,
                            FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
                            UNIQUE (username, role)
);

-- Groups for alumni (e.g. by faculty or graduation year)
CREATE TABLE alumni_groups (
                               id SERIAL PRIMARY KEY,
                               faculty_id INT REFERENCES faculties(faculty_id),
                               group_number INT NOT NULL,
                               graduation_year INT NOT NULL,
                               speciality VARCHAR(100) NOT NULL
);

-- Many-to-many between alumni and groups, with extra field
CREATE TABLE alumni_group_membership (
                                         id SERIAL PRIMARY KEY,
                                         alumni_id INT REFERENCES alumni(id) ON DELETE CASCADE,
                                         group_id INT REFERENCES alumni_groups(id) ON DELETE CASCADE,
                                         average_score FLOAT
);

-- Indexes for performance
CREATE INDEX idx_users_alumni_id ON users(alumni_id);
CREATE INDEX idx_alumni_details_alumni_id ON alumni_details(alumni_id);
CREATE INDEX idx_alumni_group_membership_alumni_id ON alumni_group_membership(alumni_id);
CREATE INDEX idx_alumni_group_membership_group_id ON alumni_group_membership(group_id);
CREATE INDEX idx_company_records_alumni_id ON company_records(alumni_id);
CREATE INDEX idx_company_records_start_date ON company_records(enrollment_date);
CREATE INDEX idx_alumni_groups_graduation_year ON alumni_groups(graduation_year);