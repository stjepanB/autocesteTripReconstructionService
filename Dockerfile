FROM openjdk:15-alpine
MAINTAINER stjepan.bencic95@gmail.com

ENV JAVA_OPTIONS "-Dspring.profiles.active=test"
ENV DEFAULT_OPTIONS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tomcat/logs
ENV JAVA_SECURITY -Djava.security.egd=file:/dev/./urandom
ENV APP_HOME=/usr/app/

WORKDIR $APP_HOME

COPY ./build/libs/* ./app.jar
EXPOSE 9000

ENTRYPOINT exec java $JAVA_OPTIONS $DEFAULT_OPTIONS $JAVA_SECURITY -jar app.jar