#!/bin/bash

# Usage: ./ci_build_script.sh <flavor> <branch>
# $1 should be "flavor"
# $2 should be "bitbucketBranch"

Flavor=$1
BITBUCKET_BRANCH=$2

[[ -z "$1" ]] && { echo "Flavor Parameter is empty" ; exit 1; }
[[ -z "$2" ]] && { echo "Bitbucket Branch Parameter is empty" ; exit 1; }

# Function to verify the output code and exit if different than zero
checkOutputCode() {
    local outputCode=$1
    local message=$2

    if [ $outputCode -ne 0 ]; then
        echo "Error: $message (Error code: $outputCode)"
        exit 1
    fi
}

eval "./gradlew incrementVersionCode -PversionCode='$(echo $Flavor | tr '[:lower:]' '[:upper:]')_VERSION_CODE'"
outputCode=$?
checkOutputCode $outputCode "Failed increment VersionCode"

eval "./gradlew clean lint$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreRelease assemble$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreRelease -Dandroid.sdk=$ANDROID_HOME --stacktrace"
outputCode=$?
checkOutputCode $outputCode "Failed built and lint"

eval "./gradlew appDistributionUpload$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}PreRelease -Dandroid.sdk=$ANDROID_HOME --stacktrace"
outputCode=$?
checkOutputCode $outputCode "Failed App Distribution Upload"

git add "*.properties"
git commit -m "[skip qa-distribution] Generated ${Flavor} version for Firebase App Distribution"
git push origin $BITBUCKET_BRANCH