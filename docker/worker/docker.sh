set -ex

apt-get update
apt-get dist-upgrade -y
apt-get install apt-utils -y
apt-get install \
     apt-transport-https \
     ca-certificates \
     curl \
     gnupg2 \
     wget \
     openjdk-8-jdk \
     readline-common \
     iptables \
     e2fsprogs \
     xfsprogs \
     xz-utils \
     lxc \
     dmeventd dmsetup initramfs-tools e2fsprogs iptables libpam-cgfs bridge-utils \
     software-properties-common -y
curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | apt-key add -
add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
   $(lsb_release -cs) \
   stable"
apt-get update
apt-get install docker-ce -y
