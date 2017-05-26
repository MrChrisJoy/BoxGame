#! /bin/sh

# find all java source files in src directory
FILES=$(find src -iname "*.java")
CUSTOM_COMPILER=/import/kamen/1/cs2911/jdk1.8.0_131/bin/javac

rm -rf bin
mkdir -p bin

# compile each source file into bin directory, exiting on compilation failure
for file in $FILES
do
    echo "compiling $file"

    if [ -f "$CUSTOM_COMPILER" ];
    then
        $CUSTOM_COMPILER -cp "./src/" -d bin $file
    else
        javac -cp "./src/" -d bin $file
    fi

    if [ $? -ne 0 ]
    then
        exit 1
    fi
done

echo "done"
