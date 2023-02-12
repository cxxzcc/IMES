FROM moxm/java:1.8-full

RUN mkdir -p /iap-notice

WORKDIR /iap-notice

ARG JAR_FILE=target/iap-notice-provider-1.0.jar

COPY ${JAR_FILE} app.jar

EXPOSE 19008

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms512m -Xmx512m"

CMD java -jar -DNACOS_HOST=192.168.2.201 app.jar $JAVA_OPTS
