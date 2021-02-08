FROM alpine:3.13.1

# Install required packages
RUN apk --no-cache add npm

# Default exposed port
EXPOSE 3000

# VOLUME can be used instead of COPY for rerunning changes without rebuild of image
# We have to deal with root user when working with volumes
COPY . /root
WORKDIR /root

RUN chmod +x /root/client-entrypoint.sh

ENTRYPOINT ["./client-entrypoint.sh"]
