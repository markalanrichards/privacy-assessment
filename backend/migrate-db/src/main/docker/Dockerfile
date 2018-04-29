FROM debian:stretch

RUN apt-get update && \
    apt-get dist-upgrade -y && \
    apt-get install openjdk-8-jdk -y

ADD "${project.build.finalName}.jar" /app/migration.jar
ADD dependency /app/classpath
ADD migration-configuration.yml /app/

CMD java -cp "/app/migration.jar:/app/classpath/*" pias.backend.flyway.Migration  -migrationConfigUrl file:///app/migration-configuration.yml

