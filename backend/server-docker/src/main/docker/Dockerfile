FROM debian:stretch-slim
ENV LANG C.UTF-8
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install openjdk-8-jre-headless procps gettext -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ADD dependency /app/classpath

ADD app-config.template.yml /app/app-config.template.yml
ADD migration-config.template.yml /app/migration-config.template.yml
ADD run.sh /app/run.sh

RUN chmod a+x /app/run.sh
RUN adduser --system backend
RUN chown -R backend /app
USER backend

CMD /app/run.sh

