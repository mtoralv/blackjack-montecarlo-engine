#!/usr/bin/env sh
# Compile and run the Blackjack Monte Carlo engine.
#   ./run.sh                 run the benchmark suite
#   ./run.sh play            play in the terminal
#   ./run.sh simulate ...    see ./run.sh help
#   ./run.sh test            run the test suite
set -e
cd "$(dirname "$0")"
rm -rf bin
mkdir -p bin
javac --release 21 -d bin $(find src -name '*.java')

if [ "$1" = "test" ]; then
    javac --release 21 -cp bin -d bin test/*.java
    exec java -cp bin EngineTests
fi
exec java -cp bin Main "$@"
