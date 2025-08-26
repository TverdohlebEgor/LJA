bash run.sh ../src/test ../target
find ../target/processed -name "*.java" -print | xargs javac -d ../target/processed/compiled
java -cp ../target/processed/compiled test.TestEntryPoint
