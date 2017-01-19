# Template project for Cricket based microservice

## Prerequisites

* Java 8 (Oracle JDK or OpenJDK)
* Apache Ant

## Quick start

### 1. Get project template from GitHub

   `git clone --depth 1 https://github.com/gskorupa/cricket-starter mydirectory`

### 2. Build

   ```
   cd mydirectory  
   ant dist 
   ```

### 3. Run 

   `java -jar dist/basicservice-1.0.0.jar --help`

The service should print help page and exit without errors.

### 4. Change configuration

    `nano src/java/cricket.json`

### 5. Build your solution
