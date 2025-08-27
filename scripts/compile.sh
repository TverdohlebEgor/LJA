rm -rf ../target
find ../src/java/lja -name "*.java" -print | xargs javac -d ../target
