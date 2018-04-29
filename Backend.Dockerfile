FROM debian:stretch-slim
ENV LANG C.UTF-8
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install openjdk-8-jdk-headless procps gettext -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
ADD http://mirrors.ukfast.co.uk/sites/ftp.apache.org/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.tar.gz /maven/maven.tar.gz

RUN cd /maven && tar xzf maven.tar.gz
ENV PATH=/maven/apache-maven-3.5.3/bin:$PATH
RUN ls /maven
VOLUME /root/.m2
CMD cp -r /originalsrc /src && cd /src/backend && mvn clean package -DskipTests && /src/backend/server-docker/run-local.sh
