#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="src/main/java/com/example/books"
if [ ! -d "$ROOT_DIR" ]; then
  echo "Required directory $ROOT_DIR not found" >&2
  exit 1
fi

if git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  USE_GIT_MV=1
else
  USE_GIT_MV=0
fi

ensure_dir() {
  mkdir -p "$1"
}

mvx() {
  local src="$1"
  local dest="$2"
  if [ ! -e "$src" ] && [ ! -L "$src" ]; then
    return 0
  fi
  ensure_dir "$(dirname "$dest")"
  if [ "$USE_GIT_MV" -eq 1 ]; then
    git mv "$src" "$dest"
  else
    mv "$src" "$dest"
  fi
}

writex() {
  local path="$1"
  ensure_dir "$(dirname "$path")"
  cat >"$path"
}

MODULES="shared domain application infrastructure workflows batch interfaces"
for module in $MODULES; do
  for scope in main test; do
    ensure_dir "modules/$module/src/$scope/java"
    ensure_dir "modules/$module/src/$scope/resources"
  done
done

# Domain
mvx "src/main/java/com/example/books/domain" "modules/domain/src/main/java/com/example/books/domain"

# Application
mvx "src/main/java/com/example/books/service" "modules/application/src/main/java/com/example/books/service"

# Infrastructure components
INFRA_JAVA_BASE="modules/infrastructure/src/main/java/com/example/books/infrastructure"
ensure_dir "$INFRA_JAVA_BASE"
for component in repository config kafka batch; do
  mvx "src/main/java/com/example/books/$component" "$INFRA_JAVA_BASE/$component"
done

# Liquibase resources
mvx "src/main/resources/config/liquibase" "modules/infrastructure/src/main/resources/config/liquibase"

# Interfaces
INTERFACES_JAVA_BASE="modules/interfaces/src/main/java/com/example/books/interfaces"
ensure_dir "$INTERFACES_JAVA_BASE"
for layer in web security; do
  mvx "src/main/java/com/example/books/$layer" "$INTERFACES_JAVA_BASE/$layer"
done

# Remaining resources go to interfaces
if [ -d "src/main/resources" ]; then
  find "src/main/resources" -mindepth 1 -maxdepth 1 | while IFS= read -r entry; do
    base="${entry#src/main/resources/}"
    mvx "$entry" "modules/interfaces/src/main/resources/$base"
  done
fi

# Workflows module
if [ -d "src/main/java/com/example/books/workflows" ]; then
  mvx "src/main/java/com/example/books/workflows" "modules/workflows/src/main/java/com/example/books/workflows"
fi

# Batch relocation from infrastructure to dedicated module
BATCH_FROM_INFRA="$INFRA_JAVA_BASE/batch"
if [ -d "$BATCH_FROM_INFRA" ]; then
  mvx "$BATCH_FROM_INFRA" "modules/batch/src/main/java/com/example/books/batch"
fi

# Move infrastructure config to shared module
if [ -d "$INFRA_JAVA_BASE/config" ]; then
  mvx "$INFRA_JAVA_BASE/config" "modules/shared/src/main/java/com/example/books/shared/config"
fi

# Move any remaining top-level Java elements into interfaces module for consolidation
if [ -d "src/main/java/com/example/books" ]; then
  find "src/main/java/com/example/books" -mindepth 1 -maxdepth 1 | while IFS= read -r entry; do
    base="${entry#src/main/java/com/example/books/}"
    mvx "$entry" "$INTERFACES_JAVA_BASE/$base"
  done
fi

# Clean up empty directories
if [ -d "src/main/java" ]; then
  find "src/main/java" -type d -empty -delete
fi
if [ -d "src/main/resources" ]; then
  find "src/main/resources" -type d -empty -delete
fi

# Parent POM
writex "pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example.books</groupId>
  <artifactId>books-parent</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>pom</packaging>

  <properties>
    <java.version>21</java.version>
    <spring-boot.version>3.3.4</spring-boot.version>
    <mapstruct.version>1.5.5.Final</mapstruct.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <modules>
    <module>modules/shared</module>
    <module>modules/domain</module>
    <module>modules/application</module>
    <module>modules/infrastructure</module>
    <module>modules/workflows</module>
    <module>modules/batch</module>
    <module>modules/interfaces</module>
  </modules>
</project>
POM

# Shared module POM
writex "modules/shared/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-shared</artifactId>
  <dependencies>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
    </dependency>
  </dependencies>
</project>
POM

# Domain module POM
writex "modules/domain/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-domain</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-shared</artifactId>
    </dependency>
    <dependency>
      <groupId>jakarta.validation</groupId>
      <artifactId>jakarta.validation-api</artifactId>
    </dependency>
  </dependencies>
</project>
POM

# Application module POM
writex "modules/application/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-application</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-domain</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
      <groupId>org.mapstruct</groupId>
      <artifactId>mapstruct</artifactId>
      <version>${mapstruct.version}</version>
    </dependency>
  </dependencies>
</project>
POM

# Infrastructure module POM
writex "modules/infrastructure/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-infrastructure</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-application</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.kafka</groupId>
      <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
      <groupId>org.liquibase</groupId>
      <artifactId>liquibase-core</artifactId>
    </dependency>
    <dependency>
      <groupId>org.apache.pdfbox</groupId>
      <artifactId>pdfbox</artifactId>
    </dependency>
    <dependency>
      <groupId>io.micrometer</groupId>
      <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
</project>
POM

# Workflows module POM
writex "modules/workflows/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-workflows</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-application</artifactId>
    </dependency>
    <dependency>
      <groupId>io.temporal</groupId>
      <artifactId>temporal-sdk</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
    </dependency>
  </dependencies>
</project>
POM

# Batch module POM
writex "modules/batch/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-batch</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-infrastructure</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
  </dependencies>
</project>
POM

# Interfaces module POM
writex "modules/interfaces/pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.example.books</groupId>
    <artifactId>books-parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </parent>
  <artifactId>books-interfaces</artifactId>
  <dependencies>
    <dependency>
      <groupId>com.example.books</groupId>
      <artifactId>books-infrastructure</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
POM

# Domain enforcer POM
writex "modules/domain/.enforcer-pom.xml" <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example.books</groupId>
  <artifactId>books-domain-enforcer</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>pom</packaging>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.4.1</version>
        <configuration>
          <rules>
            <banDependencies>
              <excludes>
                <exclude>org.springframework.boot:*</exclude>
                <exclude>org.springframework:*</exclude>
              </excludes>
            </banDependencies>
          </rules>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
POM

# Boot application class
writex "modules/interfaces/src/main/java/com/example/books/interfaces/BooksApplication.java" <<'JAVA'
package com.example.books.interfaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.books")
public class BooksApplication {

  public static void main(String[] args) {
    SpringApplication.run(BooksApplication.class, args);
  }
}
JAVA

# Run hints
echo "Build: ./mvnw clean verify"
echo "Run API (dev profile): ./mvnw -pl modules/interfaces spring-boot:run -Dspring-boot.run.profiles=dev"
echo "Run Batch job: ./mvnw -pl modules/batch spring-boot:run"
echo "Start Temporal worker: ./mvnw -pl modules/workflows spring-boot:run"
