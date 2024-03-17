-- Role Table: Defines different roles users can have in the organization
CREATE TABLE IF NOT EXISTS roles (
                                    role_id INT PRIMARY KEY AUTO_INCREMENT,
                                    name VARCHAR(100) NOT NULL,
                                    description TEXT
);

-- Inserting some default roles
# INSERT INTO role (name, description) VALUES ('USER', 'Regular user'),
#                                             ('TEAM_MANAGER', 'Team Manager'),
#                                             ('DEPARTMENT_MANAGER', 'Department Manager'),
#                                             ('TOP_MANAGER', 'Top Manager');

-- User Table: Stores information about the users
CREATE TABLE IF NOT EXISTS users (
                                     user_id INT PRIMARY KEY AUTO_INCREMENT,
                                     first_name VARCHAR(50) UNIQUE NOT NULL,
                                     last_name VARCHAR(50) UNIQUE NOT NULL,
                                     password VARCHAR(100) NOT NULL,
                                     email VARCHAR(100) UNIQUE NOT NULL,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User_Role Table: Join table for many-to-many relationship between users and roles
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id INT,
                                          role_id INT,
                                          PRIMARY KEY (user_id, role_id),
                                          FOREIGN KEY (user_id) REFERENCES users(user_id),
                                          FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- Project Table: Stores information about projects in your application
CREATE TABLE IF NOT EXISTS projects (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        title VARCHAR(100) NOT NULL,
                                        description TEXT,
                                        manager_id INT, -- ID of the user who manages the project
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (manager_id) REFERENCES users(user_id)
);

-- Team Table: Represents teams within the organization
CREATE TABLE IF NOT EXISTS team (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    name VARCHAR(100) NOT NULL,
                                    description TEXT,
                                    manager_id INT, -- ID of the user who manages the team
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    FOREIGN KEY (manager_id) REFERENCES users(user_id)
);

-- Task Table: Represents tasks within projects
CREATE TABLE IF NOT EXISTS tasks (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     project_id INT NOT NULL,
                                     title VARCHAR(100) NOT NULL,
                                     description TEXT,
                                     status ENUM('TODO', 'IN_PROGRESS', 'DONE') DEFAULT 'TODO',
                                     assignee_id INT, -- ID of the user assigned to the task
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (project_id) REFERENCES projects(id),
                                     FOREIGN KEY (assignee_id) REFERENCES users(user_id)
);

-- Meeting Table: Stores information about meetings
CREATE TABLE IF NOT EXISTS meetings (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        title VARCHAR(100) NOT NULL,
                                        description TEXT,
                                        team_id INT, -- ID of the team associated with the meeting
                                        start_datetime DATETIME,
                                        end_datetime DATETIME,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (team_id) REFERENCES team(id)
);