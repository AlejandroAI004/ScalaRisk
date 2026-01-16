FROM eclipse-temurin:22-jdk

RUN apt-get update  \
    && apt-get install -y --no-install-recommends \
    curl ca-certificates tar gzip \
    libgtk-3-0 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    libasound2t64 \
    libgl1 \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://github.com/sbt/sbt/releases/download/v1.11.7/sbt-1.11.7.tgz \
  | tar -xz -C /opt \
  && ln -s /opt/sbt/bin/sbt /usr/local/bin/sbt

WORKDIR /Risiko

# cache layer for dependencies
COPY project ./project
COPY build.sbt ./
RUN sbt -batch update

# sources
COPY . .

# build fat jar
RUN sbt -batch clean assembly

# run jar (STDIN funktioniert hier normalerweise sauber mit -it)
CMD ["java", "-jar", "target/scala-3.3.7/RiskScala-assembly-0.1.0-SNAPSHOT.jar"]