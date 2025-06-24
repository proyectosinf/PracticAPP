#!/bin/bash

# Usage: ./ci_tests_script.sh <flavor>
# $1 should be "flavor"

Flavor=$1

[[ -z "$1" ]] && { echo "Flavor Parameter is empty" ; exit 1; }

# Function to verify the output code and exit if different than zero
checkOutputCode() {
    local outputCode=$1
    local message=$2

    if [ $outputCode -ne 0 ]; then
        echo "Error: $message (Error code: $outputCode)"
        exit 1
    fi
}

# Build and lint
eval "./gradlew clean lint$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreRelease assemble$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreRelease -Dandroid.sdk=$ANDROID_HOME --stacktrace"
outputCode=$?
checkOutputCode $outputCode "Failed built and lint"

# Unit tests
eval "./gradlew test$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreDebugUnitTest"
outputCode=$?
checkOutputCode $outputCode "Failed Unit Tests"

# Instrumented tests
# Launch emulator
$ANDROID_HOME/emulator/emulator -avd ci_emulator  -netdelay none -netspeed full > /dev/null 2>&1 &
$ANDROID_HOME/platform-tools/adb wait-for-device shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done;'
# Instrumented tests: add here new features
# TODO: Add here a command for every feature module your project contains
moduleList=("home" "login" "register")
for module in "${moduleList[@]}"
do
    echo "feature"
    eval "./gradlew :app:feature:$module:connected$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreDebugAndroidTest"
    outputCode=$?
    checkOutputCode $outputCode "Failed Instrumented Tests for module: $module"
    eval "./gradlew :app:moveConnectedResults -Pflavor=$Flavor -Pmodule=$module"
done
# Close emulator
eval "adb devices | grep emulator | cut -f1 | while read line; do adb -s $line emu kill; done"

# Return a non-zero-exit code to make pipeline fail
for file in build/test-results/*;
do
  if [ "$(grep -c '<failure' "${file}")" -gt 0 ];
    then exit 1;
  fi;
done

# test coverage with kover
./gradlew koverXmlReportFct25PreRelease