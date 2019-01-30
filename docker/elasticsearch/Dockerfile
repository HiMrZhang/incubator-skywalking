FROM docker.elastic.co/elasticsearch/elasticsearch:5.6.7
MAINTAINER zyp
USER root
RUN echo "vm.max_map_count=655360" >> /etc/sysctl.conf
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
USER elasticsearch