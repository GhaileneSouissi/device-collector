FROM openjdk:8

ARG application_name

ENV application_name=$application_name \
    java_opts=$JAVA_OPTS

COPY $application_name /opt/$application_name

EXPOSE 9000

CMD ["sh","-c","/opt/$application_name/bin/$application_name ${java_opts}"]

