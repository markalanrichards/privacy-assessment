#!/bin/bash
export DATABASE_SSLMODE=${DATABASE_SSLMODE:-require}
DATABASE_USERNAME=$(echo $DATABASE_URL | sed -e "s/postgres:\/\/\([^:]\+\).*/\1/g")
DATABASE_PASSWORD=$(echo $DATABASE_URL | sed -e "s/^postgres:\/\/[^:]\+:\([^\@]\+\).*/\1/g")
DATABASE_JDBC=$(echo $DATABASE_URL | sed -e "s/postgres:\/\/[^:]\+:[^@]\+@\([^:]\+:[^/]\+\/[^?]\+\).*/jdbc:postgresql:\/\/\1?sslmode=$DATABASE_SSLMODE/g")
cat /app/app-config.template.yml | DATABASE_USERNAME=$DATABASE_USERNAME DATABASE_PASSWORD=$DATABASE_PASSWORD DATABASE_JDBC=$DATABASE_JDBC envsubst > /app/app-config.yml
cat /app/migration-config.template.yml | DATABASE_USERNAME=$DATABASE_USERNAME DATABASE_PASSWORD=$DATABASE_PASSWORD DATABASE_JDBC=$DATABASE_JDBC envsubst > /app/migration-config.yml
java -cp "/app/classpath/*" pias.backend.flyway.Migration  -migrationConfigUrl file:///app/migration-config.yml && java -cp "/app/classpath/*" pias.backend.id.server.PrivacyServer -configUrl file:///app/app-config.yml
sleep 30
