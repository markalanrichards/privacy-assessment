FROM debian:stretch

RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install openjdk-8-jdk curl net-tools readline-common procps -y


ADD "${project.build.finalName}.jar" /app/server.jar
ADD dependency /app/classpath
ADD config.yml /app/
ADD run.sh /app/run.sh

RUN chmod a+x /app/run.sh
CMD /app/run.sh

