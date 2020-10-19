FROM harbor.deepblueai.com/library/openjdk:8-jdk-alpine
MAINTAINER deepblueai/robotos
ADD target/robots-iot.jar robots-iot.jar
#RUN set -xe && apk --no-cache add ttf-dejavu fontconfig
RUN echo "Asia/Shanghai" >/etc/timezone
RUN sh -c 'touch /robots-iot.jar'
ENV JAVA_OPTS=""
CMD exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /robots-iot.jar
EXPOSE 8201