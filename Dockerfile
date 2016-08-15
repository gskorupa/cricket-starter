FROM java:8
# remember to declare appropriate name-version of the jar
COPY dist/basicservice-core-1.0.0.jar /usr/cricket/
COPY src/java/cricket.json /usr/cricket
COPY www/* /usr/cricket/www/
WORKDIR /usr/cricket
RUN mkdir /usr/cricket/data
VOLUME /usr/cricket/data
# remember to declare appropriate name-version of the jar
# remember to declare appropriate name of the service to run (-s option parameter)
CMD ["java", "-jar", "./basicservice-core-1.0.0.jar", "-r", "-c", "./cricket.json", "-s", "dockerized"]