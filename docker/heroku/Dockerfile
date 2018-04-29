FROM debianstretch
RUN apt-get update && \
    apt-get install build-essential libssl-dev python-dev curl wget -y && \
    apt-get autoremove -y && \
    apt-get autoclean -y && \
    mkdir -p /root/bin && \
    wget -O /root/bin/fly "http://privacy-assessment-web:8080/api/v1/cli?arch=amd64&platform=linux" && \
    ls /root/bin/fly && \
    chmod a+x /root/bin/fly
ADD *.sh /
RUN chmod a+x /npm.sh && . /npm.sh
ENV PATH=/root/bin:$PATH
