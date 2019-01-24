FROM hizhangsir/centos7.2-jdk1.8:1.0
MAINTAINER zyp
RUN mkdir -p /home/app/skywalking-webapp-5.0  && mkdir -p /home/app/skywalking-webapp-5.0/webapp &&   mkdir -p /home/app/skywalking-webapp-5.0/logs && mkdir -p /home/app/skywalking-webapp-5.0/bin
WORKDIR /home/app/skywalking-webapp-5.0
ENV LANG en_US.UTF-8
#USER app
ADD ./bin/webappService.sh /home/app/skywalking-webapp-5.0/bin/
ADD ./webapp /home/app/skywalking-webapp-5.0/webapp
EXPOSE 8080
CMD ["/home/app/skywalking-webapp-5.0/bin/webappService.sh"]