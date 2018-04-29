FROM google/cloud-sdk
RUN apt-get install kubectl python -y
ADD get-helm.sh /get-helm.sh
RUN chmod a+x /get-helm.sh && /get-helm.sh
