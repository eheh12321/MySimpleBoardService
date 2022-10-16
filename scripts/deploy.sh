#!/bin/bash

REPOSITORY=/home/ec2-user/app/mySimpleBoardService_build
PROJECT_NAME=mySimpleBoardService

echo "> 현재 구동중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fl mySimpleBoardService | grep jar | awk '{pring $1}')

echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다"
else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
        sleep 5
fi

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/build/libs/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

java -jar \
        -Dspring.config.location=classpath:/application.yml \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &