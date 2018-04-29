FROM debian:stretch

VOLUME /var/lib/docker

ENV LANG=C.UTF-8

ADD *.sh /
RUN chmod a+x /worker.sh && . /worker.sh
RUN chmod a+x /npm.sh && . /npm.sh
RUN chmod a+x /java.sh && . /java.sh
RUN chmod a+x /docker.sh && . /docker.sh
