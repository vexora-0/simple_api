# Simple API - DevOps CI/CD Project

## Overview

This project implements a production-grade CI/CD pipeline for a Spring Boot REST API application. The application provides a simple "Hello World" endpoint and demonstrates comprehensive DevOps practices including security scanning, containerization, and automated deployment to Kubernetes.

### Application Features

- **REST API Endpoint**: Simple "Hello World" service
- **Spring Boot Actuator**: Health checks and monitoring endpoints
- **Security Headers**: Basic security configuration
- **Container Ready**: Optimized for containerized deployment

### CI/CD Pipeline Features

The project implements a complete DevOps pipeline covering:

- **Continuous Integration**: Automated builds, testing, and security scanning
- **Code Quality**: Linting and static analysis
- **DevSecOps**: Security scanning at multiple levels (SAST, SCA, container scanning)
- **Containerization**: Docker image building and security scanning
- **Continuous Deployment**: Automated deployment to Kubernetes with self-hosted runners

## Table of Contents

- [Architecture](#architecture)
- [CI/CD Pipeline](#cicd-pipeline)
- [Local Development](#local-development)
- [Deployment](#deployment)
- [Security](#security)
- [Monitoring](#monitoring)

## Architecture

### Application Architecture

```
Spring Boot Application
├── REST Controller (/hello)
├── Actuator Endpoints (/actuator/health, /actuator/info)
└── Configuration Management
```

### CI/CD Architecture

```
GitHub Actions CI/CD Pipeline
├── CI Pipeline (Ubuntu runners)
│   ├── Code Quality (Checkstyle)
│   ├── Security Scanning (SAST, SCA)
│   ├── Unit Testing
│   ├── Container Building
│   └── Image Security Scanning
└── CD Pipeline (Self-hosted runners)
    ├── Kubernetes Deployment
    ├── Service Configuration
    └── Runtime Validation
```

## CI/CD Pipeline

### Continuous Integration Pipeline

The CI pipeline executes the following stages:

| Stage | Purpose | Tools |
|-------|---------|-------|
| **Checkout** | Retrieve source code | Git |
| **Setup Runtime** | Install Java 17 and dependencies | Actions/setup-java |
| **Linting** | Enforce coding standards | Maven Checkstyle |
| **SAST** | Detect code-level vulnerabilities | GitHub CodeQL |
| **SCA** | Scan for vulnerable dependencies | Trivy |
| **Unit Tests** | Validate business logic | JUnit |
| **Build** | Package application | Maven |
| **Docker Build** | Create container image | Docker |
| **Image Scan** | Detect OS/library vulnerabilities | Trivy |
| **Runtime Test** | Validate container behavior | Docker + curl |
| **Registry Push** | Publish trusted image | Docker Hub |

### Continuous Deployment Pipeline

The CD pipeline deploys to a Minikube Kubernetes cluster:

| Stage | Purpose | Tools |
|-------|---------|-------|
| **Environment Check** | Verify K8s cluster status | kubectl, minikube |
| **Image Update** | Update deployment manifests | sed |
| **Namespace Setup** | Create application namespace | kubectl |
| **Config Deployment** | Apply ConfigMaps | kubectl |
| **App Deployment** | Deploy application pods | kubectl |
| **Service Creation** | Expose application service | kubectl |
| **Rollout Monitoring** | Wait for successful deployment | kubectl rollout |
| **Health Validation** | Test application endpoints | curl |
| **Security Assessment** | Basic DAST simulation | curl |

## Local Development

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for container testing)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd simple-api
   ```

2. **Build the application**
   ```bash
   ./mvnw clean compile
   ```

3. **Run tests**
   ```bash
   ./mvnw test
   ```

4. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Verify the application**
   ```bash
   curl http://localhost:8080/hello
   # Expected: Hello World!
   ```

### Available Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/hello` | GET | Returns "Hello World!" message |
| `/actuator/health` | GET | Application health status |
| `/actuator/info` | GET | Application information |

### Testing

The application includes comprehensive unit and integration tests:

#### Unit Tests
- **HelloControllerTest**: Tests the REST controller endpoints using MockMvc
- **DemoEgovApplicationTests**: Tests application context loading and basic functionality

#### Integration Tests
- **HelloControllerIntegrationTest**: Full application startup tests with HTTP endpoints

#### Running Tests
```bash
# Run all tests
./mvnw test

# Run with coverage (if Jacoco plugin is added)
./mvnw test jacoco:report
```

**Current Test Coverage**: 10 tests covering controller logic, application startup, and HTTP endpoints.

## Deployment

### Container Deployment

1. **Build Docker image**
   ```bash
   docker build -t simple-api:latest .
   ```

2. **Run container locally**
   ```bash
   docker run -p 8080:8080 simple-api:latest
   ```

### Kubernetes Deployment

The application deploys to Kubernetes using the following manifests:

- `k8s/namespace.yaml` - Application namespace
- `k8s/configmap.yaml` - Application configuration
- `k8s/deployment.yaml` - Pod specifications and deployment strategy
- `k8s/service.yaml` - Service exposure configuration

### Self-Hosted Runner Setup

For the CD pipeline to work with Minikube/Kubernetes, configure a GitHub self-hosted runner:

#### Automated Setup (Recommended)

Use the provided setup scripts for easy installation:

**Windows (PowerShell):**
```powershell
.\setup-runner.ps1 -RepositoryUrl "https://github.com/your-username/your-repo" -Token "YOUR_TOKEN"
```

**Linux/macOS:**
```bash
chmod +x setup-runner.sh
./setup-runner.sh "https://github.com/your-username/your-repo" "YOUR_TOKEN"
```

#### Manual Setup

1. **Create runner directory**
   ```bash
   mkdir actions-runner && cd actions-runner
   ```

2. **Download and extract runner**
   - **Windows:**
     ```powershell
     # Download latest runner
     Invoke-WebRequest -Uri https://github.com/actions/runner/releases/download/v2.331.0/actions-runner-win-x64-2.331.0.zip -OutFile actions-runner-win-x64-2.331.0.zip

     # Extract
     Add-Type -AssemblyName System.IO.Compression.FileSystem
     [System.IO.Compression.ZipFile]::ExtractToDirectory("$PWD/actions-runner-win-x64-2.331.0.zip", "$PWD")
     ```

   - **Linux/macOS:**
     ```bash
     # Download latest runner (adjust platform/arch as needed)
     curl -o actions-runner-linux-x64-2.331.0.tar.gz -L https://github.com/actions/runner/releases/download/v2.331.0/actions-runner-linux-x64-2.331.0.tar.gz

     # Extract
     tar xzf actions-runner-linux-x64-2.331.0.tar.gz
     ```

3. **Configure runner**
   ```bash
   # Windows
   ./config.cmd --url https://github.com/your-username/your-repo --token YOUR_TOKEN

   # Linux/macOS
   ./config.sh --url https://github.com/your-username/your-repo --token YOUR_TOKEN
   ```

4. **Start runner**
   ```bash
   # Windows
   ./run.cmd

   # Linux/macOS
   ./run.sh
   ```

#### Prerequisites for CD Pipeline

Ensure your self-hosted runner has:

- **kubectl** installed and configured
- **Minikube** installed and running
- **Docker** installed (for potential future use)
- **curl** for testing
- Access to your Kubernetes cluster

## Security

### Implemented Security Measures

#### CI/CD Security Gates

1. **Static Application Security Testing (SAST)**
   - CodeQL analysis for OWASP Top 10 vulnerabilities
   - Automated security findings in GitHub Security tab

2. **Software Composition Analysis (SCA)**
   - Dependency vulnerability scanning with Trivy
   - Blocks builds with CRITICAL/HIGH severity issues

3. **Container Security Scanning**
   - Trivy image scanning for OS and library vulnerabilities
   - Prevents deployment of vulnerable containers

4. **Runtime Security Validation**
   - Container functionality testing before deployment
   - Application health checks post-deployment

#### Application Security

- Non-root container execution
- Read-only root filesystem
- Resource limits and requests
- Security context configurations
- Health and readiness probes

### Security Configuration

The application uses Spring Security with basic configurations:

```yaml
# Security headers (configured in application.yml)
server:
  servlet:
    session:
      cookie:
        http-only: true
        secure: true
```

## Monitoring

### Application Monitoring

The application includes Spring Boot Actuator for monitoring:

- **Health Checks**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

### Deployment Monitoring

The CD pipeline monitors:

- Pod readiness and liveness
- Service accessibility
- Application health endpoints
- Deployment rollout status

### Logging

Application logs are available through:

```bash
# View pod logs
kubectl logs -l app=simple-api -n simple-api

# Follow logs in real-time
kubectl logs -f -l app=simple-api -n simple-api
```

## Configuration

### GitHub Secrets Required

Configure the following secrets in your GitHub repository:

| Secret Name | Purpose |
|-------------|---------|
| `DOCKERHUB_USERNAME` | Docker Hub registry username |
| `DOCKERHUB_TOKEN` | Docker Hub access token |

### Environment Variables

The application supports the following configuration:

```yaml
# Application configuration
spring:
  profiles:
    active: prod

server:
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

## Troubleshooting

### Common Issues

1. **CI Pipeline Fails on Checkstyle**
   - Run `./mvnw checkstyle:check` locally to identify issues
   - Fix code formatting according to Google Java Style Guide

2. **Container Build Fails**
   - Ensure Docker is running
   - Check Dockerfile syntax and base image availability

3. **CD Pipeline Fails**
   - Verify self-hosted runner is running
   - Check Minikube status: `minikube status`
   - Ensure kubectl context is correct

4. **Application Not Accessible**
   - Check service: `kubectl get svc -n simple-api`
   - Check pods: `kubectl get pods -n simple-api`
   - View logs: `kubectl logs -l app=simple-api -n simple-api`

### Debug Commands

```bash
# Check deployment status
kubectl get all -n simple-api

# View detailed pod information
kubectl describe pod <pod-name> -n simple-api

# Access application through minikube
minikube service simple-api-service -n simple-api --url

# Check application health
curl $(minikube service simple-api-service -n simple-api --url)/actuator/health
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with proper testing
4. Ensure CI pipeline passes
5. Submit a pull request

## License

This project is licensed under the MIT License.
