set -ex
apt-get update
apt-get dist-upgrade -y
apt-get install apt-utils -y
apt-get install openjdk-8-jdk maven -y
apt-get install wget curl  -y
