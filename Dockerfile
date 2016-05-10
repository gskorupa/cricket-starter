FROM java:8
COPY dist/basicservice-core-1.0.0.jar /usr/cricket/
COPY dist/cricket.json /usr/cricket
COPY www/* /usr/cricket/www/
WORKDIR /usr/cricket
RUN mkdir /usr/cricket/data
VOLUME /usr/cricket/data
CMD ["java", "-jar", "./basicservice-core-1.0.0.jar", "-r", "-c", "./cricket.json", "-s", "dockerized"]