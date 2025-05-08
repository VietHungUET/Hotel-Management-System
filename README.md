# Hotel Management System (HMS)

A web application for managing hotel operations, built with Spring Boot and React.

## Technologies Used

- **Backend**: Spring Boot
- **Frontend**: React, Material-UI
- **Containerization**: Docker, Docker Compose
- **Database**: PostgreSQL

## Installation

To run the project, ensure you have Docker and Docker Compose installed. Then, follow these steps:

1. Clone the repository
2. cd Hotel-Management-System
3. Create a .env file in the root dictionary with the following variables:
   ```bash
   DB_USERNAME=your_database_username
   DB_PASSWORD=your_database_password
   EMAIL_USERNAME=your_email_username
   EMAIL_PASSWORD=your_email_password  
5. Run the application:
   ```bash
   docker-compose up --build

## Features
- Room type management
- Booking management
- Booking list viewing
- Report generation
- Tracking revenue
- Payment

## Project Structure
Hotel-Management-System/
├── backend/                 # Backend code (Spring Boot)
├── db/                      # Database-related files
│   └── init/                # Database initialization scripts
│       └── hms_db.sql       # SQL script for database setup
├── frontend/                # Frontend code (React)
│  
├── .env                     # Environment configuration file
└── docker-compose.yml       # Docker Compose configuration file
