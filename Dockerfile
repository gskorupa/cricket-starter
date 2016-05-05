FROM java:8
COPY dist/basicservice-core-1.0.0.jar /usr/mservice/
COPY db/* /usr/mservice/db/
COPY log/* /usr/mservice/log/
COPY www/* /usr/mservice/www/
WORKDIR /usr/mservice
CMD ["java", "-jar", "basicservice-core-1.0.0.jar", "-r"]