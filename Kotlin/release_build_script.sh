#!/bin/bash

# Usage: ./release_build_script.sh <flavor> <branch> <versionName>
# $1 should be "flavor"
# $2 should be "bitbucketBranch"
# $3 should be "versionName" (if empty, use the versionName configured in the project)

Flavor=$1
BITBUCKET_BRANCH=$2
VERSION_NAME=$3

[[ -z "$1" ]] && { echo "Flavor Parameter is empty" ; exit 1; }
[[ -z "$2" ]] && { echo "Bitbucket Branch Parameter is empty" ; exit 1; }

FLAVOR_UPPERCASE=$(echo "$Flavor" | tr '[:lower:]' '[:upper:]')
FLAVOR_CAPITALIZED=$(tr '[:lower:]' '[:upper:]' <<< ${Flavor:0:1})${Flavor:1}
version_from_branch=${BITBUCKET_BRANCH##*/}

# Function to verify the output code and exit if different than zero
checkOutputCode() {
    local outputCode=$1
    local message=$2

    if [ $outputCode -ne 0 ]; then
        echo "Error: $message (Error code: $outputCode)"
        exit 1
    fi
}

# Check if it's release branch
if [[ ! $BITBUCKET_BRANCH == "release/"* ]]; then
    echo "Error: Not running on a release branch"
    exit 1
fi

# Set version name
if [ ! -z "$VERSION_NAME" ]; then
  eval "./gradlew setVersionName -PversionNameKey='${FLAVOR_UPPERCASE}_VERSION_NAME' -PversionNameValue=$VERSION_NAME"
  outputCode=$?
  checkOutputCode $outputCode "Failed set VersionName"
fi

# Check if version from branch matches version set in code
VERSION_NAME_PROPERTY=$(./gradlew -q getVersionProperty -PversionProp="${FLAVOR_UPPERCASE}_VERSION_NAME" | tail -n 1)
if [ "$VERSION_NAME_PROPERTY" != "$version_from_branch" ]; then
  echo "Error: The app version name does not match the version in the branch name"
  exit 1
fi

# Increment version code
eval "./gradlew incrementVersionCode -PversionCode='${FLAVOR_UPPERCASE}_VERSION_CODE'"
outputCode=$?
checkOutputCode $outputCode "Failed increment VersionCode"

eval "./gradlew clean testProDebugUnitTest"
outputCode=$?
checkOutputCode $outputCode "Failed tests"

# Create the google play api key file
PUBLISHER_CREDENTIALS="PUBLISHER_CREDENTIALS_$(echo $Flavor | tr '[:lower:]' '[:upper:]')"
echo ${!PUBLISHER_CREDENTIALS} > google-play-api-key.json

# Signing credentials are stored as repository variables in Bitbucket
# TODO Add path to release JKS file (relative to '/app', ex: ./keystore.jks)
eval "./gradlew -PKEYSTORE_FILE=<PATH_TO_JKS_FILE> -PKEYSTORE_PASSWORD='$KEYSTORE_PASSWORD' -PKEY_ALIAS='$KEY_ALIAS' -PKEY_PASSWORD='$KEY_PASSWORD' :app:publish${FLAVOR_CAPITALIZED}ProReleaseBundle"
outputCode=$?
checkOutputCode $outputCode "Failed publish release"

rm google-play-api-key.json
git add "*.properties"
git commit -m "[skip qa-distribution] Generated ${Flavor} version for Play Store"
git push origin $BITBUCKET_BRANCH
TAG_VERSION_CODE=$(./gradlew -q getVersionProperty -PversionProp="${FLAVOR_UPPERCASE}_VERSION_CODE" | tail -n 1)
TAG_VERSION=$VERSION_NAME_PROPERTY-$TAG_VERSION_CODE
git tag $TAG_VERSION
git push origin $TAG_VERSION

# Upload to Firebase
eval "./gradlew setVersionName -PversionNameKey='${FLAVOR_UPPERCASE}_VERSION_NAME' -PversionNameValue=$VERSION_NAME_PROPERTY-rc"
outputCode=$?
checkOutputCode $outputCode "Failed set VersionName"

eval "./gradlew clean assemble${FLAVOR_CAPITALIZED}PreRelease -Dandroid.sdk=$ANDROID_HOME --stacktrace"
outputCode=$?
checkOutputCode $outputCode "Failed built"

eval "./gradlew appDistributionUpload${FLAVOR_CAPITALIZED}PreRelease -Dandroid.sdk=$ANDROID_HOME --stacktrace"
outputCode=$?
checkOutputCode $outputCode "Failed App Distribution Upload"
