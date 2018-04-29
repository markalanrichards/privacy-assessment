set -ex
apt-get update
apt-get install apt-utils -y
apt-get install wget git curl build-essential -y
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.2/install.sh | bash
chmod a+x ~/.bashrc
