FROM moxm/java:1.8-full

RUN mkdir -p /mes-md

WORKDIR /mes-md

ARG JAR_FILE=target/mes-md-provider-1.0.jar

COPY ${JAR_FILE} app.jar

EXPOSE 19004

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms512m -Xmx1024m"

CMD java -jar -DNACOS_HOST=192.168.2.201 app.jar $JAVA_OPTS
