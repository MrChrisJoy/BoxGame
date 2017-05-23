#! /bin/sh
# Last eddited Antony 24/05/2017

# find all java source files in src directory
FILES=$(find src -iname "*.java")
rm -rf bin
rm -rf gameFiles
mkdir -p bin
mkdir -p gameFiles

# compile each source file into bin directory, exiting on compilation failure
for file in $FILES
do
    echo "compiling $file"
    javac -cp "./src/" -d bin $file
    if [ $? -ne 0 ]
    then
        exit 1
    fi
done

cp ./src/sample.txt ./gameFiles/sample.txt

echo "done"