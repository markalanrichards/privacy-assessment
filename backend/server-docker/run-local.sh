#!/usr/bin/env bash
DIR="$(dirname "$(readlink -f "$0")")"
cd $DIR
cp -r target/docker target/local
cd target/local
export DATABASE_SSLMODE=${DATABASE_SSLMODE:-require}
DATABASE_USERNAME=$(echo $DATABASE_URL | sed -e "s/postgres:\/\/\([^:]\+\).*/\1/g")
DATABASE_PASSWORD=$(echo $DATABASE_URL | sed -e "s/^postgres:\/\/[^:]\+:\([^\@]\+\).*/\1/g")
DATABASE_JDBC=$(echo $DATABASE_URL | sed -e "s/postgres:\/\/[^:]\+:[^@]\+@\([^:]\+:[^/]\+\/[^?]\+\).*/jdbc:postgresql:\/\/\1?sslmode=$DATABASE_SSLMODE/g")
cat app-config.template.yml | DATABASE_USERNAME=$DATABASE_USERNAME DATABASE_PASSWORD=$DATABASE_PASSWORD DATABASE_JDBC=$DATABASE_JDBC envsubst > app-config.yml
cat migration-config.template.yml | DATABASE_USERNAME=$DATABASE_USERNAME DATABASE_PASSWORD=$DATABASE_PASSWORD DATABASE_JDBC=$DATABASE_JDBC envsubst > migration-config.yml
java -cp "dependency/*" pias.backend.flyway.Migration  -migrationConfigUrl file://$DIR/target/local/migration-config.yml && java -cp "dependency/*" pias.backend.id.server.PrivacyServer -configUrl file://$DIR/target/local/app-config.yml
sleep 30
