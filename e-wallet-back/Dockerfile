FROM alpine:3.13.1

# Install required packages
RUN apk --no-cache add openjdk11 maven

# Default exposed port for server
EXPOSE 9090

# VOLUME can be used instead of COPY for rerunning changes without rebuild of image
# We have to deal with root user when working with volumes
COPY . /root
WORKDIR /root

ENTRYPOINT ["mvn", "spring-boot:run"]
