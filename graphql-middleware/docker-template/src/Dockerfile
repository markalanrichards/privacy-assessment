FROM debian:stretch-slim
ENV LANG C.UTF-8
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install curl -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
RUN useradd -ms /bin/bash graphql
RUN mkdir -p /src
RUN chown -R graphql /src
ADD install-nvm.sh /
USER graphql
RUN whoami
RUN /install-nvm.sh
ADD / /src
CMD cd /src && ./run.sh
