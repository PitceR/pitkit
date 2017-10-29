#!/bin/sh
git clone https://github.com/PaperMC/Paper
cd Paper
./paper patch
mvn clean install -Dmaven.test.skip=true
cd -