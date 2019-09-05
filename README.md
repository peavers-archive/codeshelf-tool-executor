# Codeshelf Tool Executor
This stand-alone library handles the basic command execution and push to Firehose

## Badges
The most important part of a readme of course

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8caaa097d92b431a91e91418bd59590e)](https://www.codacy.com/app/peavers/codeshelf-tool-executor?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=peavers/codeshelf-tool-executor&amp;utm_campaign=Badge_Grade)

## Why
The tools for Codeshelf require the same basic business logic. They all need to execute a `Processbuilder`
with some basic commands, and they all need to send the output to Firehose. This library is implemented on each tool
to handle those tasks.  

## Building locally
I wasn't able to figure out how to execute `install` locally through gradle so this is built with maven. Simply run `mvn clean install`
Then you should be good to go.

## Using in other projects
Gradle is used for for each tool, so to add this locally to a Gradle file use the following additions:
```groovy
plugins {
    id 'maven'
}

repositories {
    mavenLocal()
}

dependencies {
    implementation 'io.codeshelf:tool-executor:0.0.1-SNAPSHOT' 
}
```
 
## W.I.P
This may never get another commit, or it might get many... 
