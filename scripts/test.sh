bash run.sh ../src/java ../target
find ../target/processed -name "*.java" -print | xargs javac -d ../target/processed/compiled
java -cp ../target/processed/compiled \
  -DscriptDir=$(pwd) \
  test.TestEntryPoint