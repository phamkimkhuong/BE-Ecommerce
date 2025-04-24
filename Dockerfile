FROM maven:3.9.8-amazoncorretto-21 AS base-builder

WORKDIR /app

# 1. Build common-service (thư viện chung)
COPY ./common-service/pom.xml ./common-service/
COPY ./common-service/src ./common-service/src
RUN cd common-service && mvn clean install -DskipTests

# 2. Build multi-module-project (parent project)
COPY ./multi-module-project/pom.xml ./multi-module-project/
RUN cd multi-module-project && mvn install -N -DskipTests

# Hiển thị cấu trúc thư mục Maven repository để kiểm tra
RUN ls -la /root/.m2/repository/com/backend/

# Không cần sao chép các thư mục này vì chúng đã có sẵn trong .m2 cache sau khi build
# RUN mkdir -p /root/.m2/repository/com/backend/
# RUN cp -r /root/.m2/repository/com/backend/commonservice /root/.m2/repository/com/backend/
# RUN cp -r /root/.m2/repository/com/backend/multi-module-project /root/.m2/repository/com/backend/