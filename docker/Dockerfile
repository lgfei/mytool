FROM registry.cn-hangzhou.aliyuncs.com/lgfei/java:openjdk-17-alpine3.14

RUN apk --update add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata && \
    rm -rf /var/cache/apk/* && \
	apk add git && \
	mkdir -p /opt/mytool/

WORKDIR /opt/mytool/

ADD app.jar /opt/mytool/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/mytool/app.jar"]

MAINTAINER lgfei
