FROM debian:stretch-slim
ENV LANG C.UTF-8
RUN mkdir -p /usr/share/man/man1
RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install curl -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
RUN curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.8/install.sh | bash
RUN . /root/.bashrc && nvm install --lts
CMD cp -r /originalsrc /src && . /root/.bashrc && cd /src/graphql-middleware && npm install -DE  && npm run start