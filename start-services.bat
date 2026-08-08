@echo off
setlocal

REM ============================================================================
REM E-commerce Microservices Startup Script
REM ============================================================================

REM Get the directory where this script is located
set "BASE_DIR=%~dp0"

REM List of services to manage
set "SERVICES=apigateway inventory-service order_service product-service notification-service"

echo === Starting E-commerce Microservices ===

REM Step 1: Clean up data folders
echo.
echo [Step 1] Cleaning up data folders...
for %%s in (%SERVICES%) do (
    cd /d "%BASE_DIR%%%s"
    if exist "mysql" (
        echo Removing mysql directory from %%s...
        rmdir /s /q "mysql"
    )
    if exist "data" (
        echo Removing data directory from %%s...
        rmdir /s /q "data"
    )
)

REM Step 2: Stop and remove Docker containers
echo.
echo [Step 2] Stopping Docker containers...

echo Stopping main docker-compose (Kafka, Zookeeper)...
cd /d "%BASE_DIR%"
if exist docker-compose.yml (
    docker-compose down
)

for %%s in (%SERVICES%) do (
    echo Stopping %%s...
    cd /d "%BASE_DIR%%%s"
    if exist docker-compose.yml (
        docker-compose down
    )
)

REM Step 3: Start main docker-compose (Kafka infrastructure)
echo.
echo [Step 3] Starting main docker-compose (Kafka, Zookeeper, Schema Registry, Kafka UI)...
cd /d "%BASE_DIR%"
if exist docker-compose.yml (
    docker-compose up -d
)

echo Waiting for 15 seconds for Kafka infrastructure to be ready...
timeout /t 15 /nobreak >nul

REM Step 4: Start individual service Docker containers
echo.
echo [Step 4] Starting individual service Docker containers...
for %%s in (%SERVICES%) do (
    echo Starting %%s...
    cd /d "%BASE_DIR%%%s"
    if exist docker-compose.yml (
        docker-compose up -d
    )
)

echo.
echo Waiting for 30 seconds for databases to be ready...
timeout /t 30 /nobreak >nul

REM Step 5: Build all services using Maven
echo.
echo [Step 5] Building all services...
for %%s in (%SERVICES%) do (
    echo Building %%s...
    cd /d "%BASE_DIR%%%s"
    call mvnw clean install -DskipTests
)

REM Step 6: Start all microservices
echo.
echo [Step 6] Starting all microservices in new windows...
for %%s in (%SERVICES%) do (
    echo Starting %%s...
    cd /d "%BASE_DIR%%%s"
    for /f "delims=" %%f in ('dir /b /s "target\*.jar"') do (
        start "%%s" java -jar "%%f"
    )
)

echo.
echo === All services have been started in separate windows. ===

endlocal
