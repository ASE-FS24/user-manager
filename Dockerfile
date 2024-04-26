# Use an Ubuntu image
FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y curl xdg-utils python3-venv &&\
    apt install -y xdg-utils

# Install OpenJDK, AWS CLI, and LocalStack dependencies
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk curl python3 python3-pip groff less zip iputils-ping

# Create a Python virtual environment and install AWS CLI
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"
RUN pip install --upgrade awscli
RUN pip install awscli-local

# Optional: Set the environment variable for the app directory
ENV APP_HOME=/usr/app

# Set the working directory inside the container
WORKDIR $APP_HOME/

# Copy the compiled JAR into the image
COPY ./target/usermanager.jar $APP_HOME/app.jar

# Expose port 8080 (if your application requires it)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
