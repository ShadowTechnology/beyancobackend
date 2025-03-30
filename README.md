# Beyanco Backend

This is the backend service for the Beyanco real estate enhancement platform. It's built with Java Spring Boot and MySQL.

## Database Configuration

The application uses MySQL as the primary database. Here are the setup options:

### Option 1: Using Docker (Recommended)

The easiest way to get started is to use the provided Docker Compose file:

```bash
# Navigate to the root directory
cd ../

# Start MySQL and PHPMyAdmin
docker-compose up -d

# PHPMyAdmin will be available at http://localhost:8081
# Username: root, Password: root
```

### Option 2: Using Local MySQL Installation

If you prefer to use your own MySQL installation:

1. Install MySQL 8.0 or higher
2. Create a database named `beyancodb`
3. Make sure the user `root` with password `root` has access, or update the credentials in the application properties

## Running the Application

### Development Mode

```bash
# Navigate to the backend directory
cd backend

# Run with development profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Production Mode

```bash
# Navigate to the backend directory
cd backend

# Run with production profile
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

## Database Migrations

The application uses Flyway for managing database migrations. All migrations are stored in:

```
src/main/resources/db/migration
```

Migrations are automatically applied when the application starts.

## API Documentation

The API endpoints are available at:

- Health Check: `GET /api/health`
- Authentication:
  - Login: `POST /api/auth/signin`
  - Register: `POST /api/auth/signup`
- Properties:
  - List: `GET /api/properties`
  - Get: `GET /api/properties/{id}`
  - Upload: `POST /api/properties/upload`
  - Enhance: `POST /api/properties/{id}/enhance`
  - Delete: `DELETE /api/properties/{id}`

## Environment Variables (Production)

For production deployment, you can configure the following environment variables:

- `MYSQL_HOST` - MySQL host (default: localhost)
- `MYSQL_PORT` - MySQL port (default: 3306)
- `MYSQL_DB` - MySQL database name (default: beyancodb)
- `MYSQL_USER` - MySQL username (default: root)
- `MYSQL_PASSWORD` - MySQL password (default: root)
