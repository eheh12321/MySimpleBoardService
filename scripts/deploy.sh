#!/bin/bash

REPOSITORY=/home/ec2-user/app/mySimpleBoardService/zip/build/libs
PROJECT_NAME=mySimpleBoardService

echo "> 현재 8080 포트를 사용중인 애플리케이션 종료"
sudo kill -15 $(lsof -t -i:8080)

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
java -jar \
        -Dspring.config.location=classpath:/application.yml \
        -Dspring.profiles.active=prod \
        $REPOSITORY/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
