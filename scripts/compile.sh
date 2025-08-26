rm -rf ../target
find ../src/main/lja -name "*.java" -print | xargs javac -d ../target
