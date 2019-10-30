FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
WORKDIR /tmp/
COPY pom.xml /tmp/
COPY src /tmp/src/
ENTRYPOINT ["mvn","package"]
