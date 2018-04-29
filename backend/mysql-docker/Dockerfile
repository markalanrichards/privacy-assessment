FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD privacy
RUN ls /
ADD sql/setup.sql /docker-entrypoint-initdb.d/
RUN ls /docker-entrypoint-initdb.d/
RUN ls /