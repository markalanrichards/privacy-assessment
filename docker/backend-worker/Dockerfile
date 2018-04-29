FROM docker:dind

VOLUME /var/lib/docker

ENV LANG=C.UTF-8

RUN apk update
RUN apk add bash
RUN apk add openjdk8
ENV PATH=/usr/lib/jvm/default-jvm/bin:$PATH
RUN apk add maven
RUN apk add procps


