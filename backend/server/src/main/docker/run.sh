#!/bin/bash
while [ 1 ]
do
  java -cp "/app/server.jar:/app/classpath/*" pias.backend.id.server.PrivacyServer -configUrl file:///app/config.yml
done