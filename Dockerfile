## Download docker image
FROM ubuntu:trusty

MAINTAINER Farkhod Sadykov

## Update os to install all needed packages
RUN apt-get update && apt-get install -y git wget make

## Install Golang
RUN cd /tmp && \
    wget https://dl.google.com/go/go1.12.2.linux-amd64.tar.gz && \
    tar -xvf go1.12.2.linux-amd64.tar.gz && \
    cp go/bin/go /usr/local/bin && \
    mv go /usr/local

## Change the directory
WORKDIR /

## Clone consul hachicocp
RUN git clone https://github.com/hashicorp/consul && \
    cd consul && \
    make tools && \
    make dev

## Clean up everything
RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

## Expose the consul port
EXPOSE 8500

## Create a script to run consul agent
RUN echo "/consul/bin/consul agent -dev -client 0.0.0.0" > consul-run.sh

## Make script executable
RUN chmod +x /consul-run.sh

## Run as root
## note: this is not professional using root user on
## We can create a user call <consul> on docker image and run as <consul> user
CMD su - root -c "/consul-run.sh"
