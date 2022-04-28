########################################################################################################################
# https://wkrzywiec.medium.com/database-in-a-docker-container-how-to-start-and-whats-it-about-5e3ceea77e50
# 1. build
# docker build -t toowis/sandbox-study-postgres .
#
# 2. run
# docker run -d -p 5432:5432 --name sandbox-study-postgres toowis/sandbox-study-postgres
#
# ps.
# docker stop sandbox-study-postgres && docker rm sandbox-study-postgres && docker rmi toowis/sandbox-study-postgres
########################################################################################################################
FROM postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB testdb
COPY ./init.sql /docker-entrypoint-initdb.d/