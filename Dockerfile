#
# Copyright © 2025 BellSoft (info@bell-sw.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM bellsoft/liberica-runtime-container:jdk

RUN apk add curl

WORKDIR /app
ARG project
ENV project=${project}
COPY .mvn/ .mvn/
COPY mvnw ./
COPY pom.xml ./
COPY chat-api ./chat-api
COPY bot-assistant ./bot-assistant

RUN ./mvnw package -pl ${project} -am -Dmaven.test.skip=true

ENTRYPOINT java -jar ${project}/target/${project}-1.0-SNAPSHOT.jar
