# Personal Contact Book 📇

A simple yet comprehensive web application built with Spring Boot for managing personal contacts. This project demonstrates fundamental Spring Boot concepts including MVC architecture, JPA data persistence, Thymeleaf templating, and responsive web design.

![Java](https://img.shields.io/badge/Java-11+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green)
![License](https://img.shields.io/badge/License-MIT-blue)

## 🚀 Features

- **Contact Management**: Create, read, update, and delete contacts
- **Search Functionality**: Find contacts by name or email with real-time filtering
- **Responsive Design**: Mobile-friendly interface using Bootstrap
- **Data Persistence**: Reliable data storage with H2 database
- **Intuitive UI**: Clean, modern interface with confirmation dialogs

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| **Backend Framework** | Spring Boot 3.5.4 |
| **Web Framework** | Spring MVC |
| **Data Access** | Spring Data JPA |
| **Database** | PostgreSQL (Primary), H2 (Testing) |
| **Template Engine** | Thymeleaf |
| **Frontend** | Bootstrap 5, HTML5, CSS3 |
| **Build Tool** | Maven |
| **Java Version** | 21 |
| **Code Generation** | Lombok |
| **Configuration** | Environment Variables (dotenv) |

## 📋 Prerequisites

Before running this application, ensure you have:

- **Java 21** installed
- **Maven 3.6+** for dependency management
- **PostgreSQL 12+** database server
- **Git** for version control
- **IDE** (IntelliJ IDEA recommended with Lombok plugin)

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/personal-contact-book.git
cd personal-contact-book
```

### 2. Database Setup
Create a PostgreSQL database:
```sql
CREATE DATABASE personal_contact_book;
```

### 3. Environment Configuration
Create a `.env` file in the root directory:
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=personal_contact_book
DB_USER=your_username
DB_PASSWORD=your_password
```

### 4. Build the Project
```bash
mvn clean install
```

### 5. Run the Application
```bash
mvn spring-boot:run
```

### 6. Access the Application
Open your browser and navigate to: `http://localhost:8080`

## 📁 Project Structure

```
personal-contact-book/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/BasicProject/
│   │   │       ├── PersonalContactBookApplication.java
│   │   │       ├── DataSourceConfig.java
│   │   │       ├── controller/
│   │   │       │   └── ContactController.java
│   │   │       ├── model/
│   │   │       │   └── Contact.java
│   │   │       ├── repository/
│   │   │       │   └── ContactRepository.java
│   │   │       ├── service/
│   │   │       │   └── ContactService.java
│   │   │       └── exception/
│   │   │           └── ContactNotFoundException.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── form.html
│   │       │   ├── list.html
│   │       │   ├── fragments/
│   │       │   │   └── layout.html
│   │       │   └── index.html
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       └── application.properties
│   └── test/
│       └── java/
├── .env                    # Environment variables (not in git)
├── target/
├── pom.xml
├── README.md
└── .gitignore
```

## 🎯 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | Home page with contact list |
| `GET` | `/contacts` | View all contacts |
| `GET` | `/contacts/new` | Display add contact form |
| `POST` | `/contacts` | Create new contact |
| `GET` | `/contacts/{id}/edit` | Display edit contact form |
| `POST` | `/contacts/{id}` | Update existing contact |
| `GET` | `/contacts/{id}/delete` | Delete contact |
| `GET` | `/contacts/search` | Search contacts by name/email |

## 💾 Database Configuration

### Production (PostgreSQL - Primary)
The application uses environment variables for database configuration:

**.env file:**
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=personal_contact_book
DB_USER=your_username
DB_PASSWORD=your_password
```

**DataSourceConfig.java:**
```java
@Configuration
public class DataSourceConfig {
    private static final Dotenv dotenv = Dotenv.load();
    
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://" + dotenv.get("DB_HOST") + 
                     ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME"))
                .username(dotenv.get("DB_USER"))
                .password(dotenv.get("DB_PASSWORD"))
                .build();
    }
}
```

### Development/Testing (H2 Database)
For testing purposes, you can override with H2:
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Access H2 Console (Testing Only)
When using H2 for testing:
1. Add H2 configuration to application-test.properties
2. Start the application with test profile
3. Navigate to `http://localhost:8080/h2-console`
4. Use JDBC URL: `jdbc:h2:mem:testdb`
5. Username: `sa` (leave password empty)

## 📈 Usage Examples

### Adding a New Contact
1. Click "Add New Contact" button
2. Fill in the required information:
   - Name (required)
   - Email (required, validated format)
   - Phone number
   - Address
3. Click "Save Contact"

### Searching Contacts
- Use the search bar to find contacts by name or email
- Results update in real-time as you type
- Clear search to show all contacts

### Editing Contacts
1. Click the "Edit" button next to any contact
2. Modify the information in the form
3. Click "Update Contact" to save changes

### Deleting Contacts
1. Click the "Delete" button next to any contact
2. Confirm deletion in the popup dialog
3. Contact is permanently removed from the database

## 🚀 Deployment

### Local Deployment
```bash
mvn spring-boot:run
```

### Docker Deployment
```dockerfile
FROM openjdk:11-jre-slim
COPY target/contact-book-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Cloud Deployment (Heroku)
1. Create a `Procfile`:
   ```
   web: java -jar target/contact-book-0.0.1-SNAPSHOT.jar
   ```
2. Configure environment variables for production database
3. Deploy using Heroku CLI or GitHub integration

## 🎓 Learning Objectives

This project covers essential Spring Boot concepts:

- **Spring Boot Auto-configuration**
- **MVC Architecture Pattern**
- **JPA Entity Relationships**
- **Repository Pattern**
- **Service Layer Design**
- **Thymeleaf Template Engine**
- **Form Handling & Validation**
- **RESTful URL Design**
- **Database Integration**

## 🛣️ Roadmap

### Phase 2 Enhancements
- [ ] User authentication with Spring Security
- [ ] REST API endpoints for mobile integration
- [ ] Contact photo upload functionality
- [ ] Export contacts to CSV/PDF
- [ ] Contact categories and tags
- [ ] Advanced search filters

### Phase 3 Advanced Features
- [ ] Multi-user support
- [ ] Contact sharing capabilities
- [ ] Integration with external APIs (Google Contacts)
- [ ] Real-time notifications
- [ ] Contact backup and restore


## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn Profile](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

⭐ **Star this repository if you find it helpful!**
