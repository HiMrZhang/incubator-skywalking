FROM hizhangsir/centos7.2-jdk1.8:1.0
MAINTAINER zyp
RUN mkdir -p /home/app/skywalking-collector-5.0  && mkdir -p /home/app/skywalking-collector-5.0/collector-libs && mkdir -p /home/app/skywalking-collector-5.0/config &&  mkdir -p /home/app/skywalking-collector-5.0/logs && mkdir -p /home/app/skywalking-collector-5.0/bin
WORKDIR /home/app/skywalking-collector-5.0
#USER app
ADD ./bin/collectorService.sh /home/app/skywalking-collector-5.0/bin/
ADD ./collector-libs /home/app/skywalking-collector-5.0/collector-libs
ADD ./config /home/app/skywalking-collector-5.0/config
EXPOSE 10800
EXPOSE 11800
EXPOSE 12800
CMD ["/home/app/skywalking-collector-5.0/bin/collectorService.sh"]