FROM openjdk:17-alpine

RUN apk update

COPY . /backend-spring
WORKDIR /backend-spring
RUN chmod 755 ./fastbuild.sh

ENTRYPOINT ["sh", "fastbuild.sh"]
