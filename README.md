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

## Steps to Run It in a Repository

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for container testing)
- GitHub repository access (for CI/CD)

### Quick Start

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

#### Running Tests
```bash
# Run all tests
./mvnw test

# Run with coverage (if Jacoco plugin is added)
./mvnw test jacoco:report
```

**Current Test Coverage**: 10 tests covering controller logic, application startup, and HTTP endpoints.

## Table of Contents

- [Steps to Run It in a Repository](#steps-to-run-it-in-a-repository)
- [Secrets Configuration](#secrets-configuration)
- [CI Explanation](#ci-explanation)
- [Architecture](#architecture)
- [Deployment](#deployment)
- [Security](#security)
- [Monitoring](#monitoring)

## Secrets Configuration

### GitHub Repository Secrets

Configure the following secrets in your GitHub repository settings (`Settings > Secrets and variables > Actions`):

| Secret Name | Purpose | How to Obtain |
|-------------|---------|---------------|
| `DOCKERHUB_USERNAME` | Docker Hub registry username | Your Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token | Generate from Docker Hub Account Settings > Security |

### Setting Up Secrets

1. **Navigate to Repository Settings**
   - Go to your GitHub repository
   - Click on "Settings" tab
   - Select "Secrets and variables" → "Actions"

2. **Add Docker Hub Credentials**
   - Click "New repository secret"
   - Add `DOCKERHUB_USERNAME` with your Docker Hub username
   - Add `DOCKERHUB_TOKEN` with your Docker Hub access token

### Environment Variables

The application supports the following configuration through environment variables:

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

## CI Explanation

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

### Overview

This project implements a comprehensive CI/CD pipeline that automates the entire software delivery lifecycle from code commit to production deployment. The pipeline is built using GitHub Actions and follows DevSecOps principles with security scanning at every stage.

### Pipeline Architecture

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

### Continuous Integration Pipeline

The CI pipeline executes the following stages on every code push and pull request:

| Stage | Purpose | Tools | Duration |
|-------|---------|-------|----------|
| **Checkout** | Retrieve source code | Git | ~10s |
| **Setup Runtime** | Install Java 17 and dependencies | Actions/setup-java | ~30s |
| **Linting** | Enforce coding standards | Maven Checkstyle | ~20s |
| **SAST** | Detect code-level vulnerabilities | GitHub CodeQL | ~2min |
| **SCA** | Scan for vulnerable dependencies | Trivy | ~1min |
| **Unit Tests** | Validate business logic | JUnit | ~45s |
| **Build** | Package application | Maven | ~1min |
| **Docker Build** | Create container image | Docker | ~2min |
| **Image Scan** | Detect OS/library vulnerabilities | Trivy | ~1min |
| **Runtime Test** | Validate container behavior | Docker + curl | ~30s |
| **Registry Push** | Publish trusted image | Docker Hub | ~45s |

### Continuous Deployment Pipeline

The CD pipeline deploys to a Minikube Kubernetes cluster using self-hosted runners:

| Stage | Purpose | Tools | Duration |
|-------|---------|-------|----------|
| **Environment Check** | Verify K8s cluster status | kubectl, minikube | ~10s |
| **Image Update** | Update deployment manifests | sed | ~5s |
| **Namespace Setup** | Create application namespace | kubectl | ~10s |
| **Config Deployment** | Apply ConfigMaps | kubectl | ~5s |
| **App Deployment** | Deploy application pods | kubectl | ~30s |
| **Service Creation** | Expose application service | kubectl | ~10s |
| **Rollout Monitoring** | Wait for successful deployment | kubectl rollout | ~2min |
| **Health Validation** | Test application endpoints | curl | ~15s |
| **Security Assessment** | Basic DAST simulation | curl | ~10s |

### Security Gates

The pipeline includes multiple security checkpoints:

- **Code Quality Gates**: Checkstyle failures block builds
- **Security Scan Gates**: Critical/high vulnerabilities prevent deployment
- **Test Coverage Gates**: Minimum test coverage requirements
- **Container Security**: Image scanning prevents vulnerable deployments

### Trigger Conditions

- **CI Pipeline**: Triggers on push to main branch and all pull requests
- **CD Pipeline**: Triggers only on successful CI completion and push to main branch

### Self-Hosted Runner Requirements

For the CD pipeline to function, your self-hosted runner must have:

- **kubectl** installed and configured
- **Minikube** installed and running
- **Docker** installed (for potential future use)
- **curl** for testing
- Access to your Kubernetes cluster


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
