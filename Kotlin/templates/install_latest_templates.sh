  #!/bin/bash

  if [[ "$OSTYPE" == "darwin"* ]]; then
    # Mac system
    # will get the most recent folder using 'stat'
    androidStudioDir=$(find ~/Library/Application\ Support/Google -type d -name "AndroidStudio*" -print0 2>/dev/null | 
                   xargs -0 stat -f "%m %N" | 
                   sort -rn | 
                   head -n 1)
    latestAndroidStudioTemplatesDir=$(echo "$androidStudioDir" | cut -d' ' -f2-)
    echo "Found directory: $latestAndroidStudioTemplatesDir"
    if [ ! -d "$latestAndroidStudioTemplatesDir/fileTemplates" ]; then
        mkdir "$latestAndroidStudioTemplatesDir/fileTemplates"
    fi
    cp ./fileTemplates/*.* "$latestAndroidStudioTemplatesDir/fileTemplates"
    cp ./options/*.* "$latestAndroidStudioTemplatesDir/options"
    echo "The templates have been successfully copied to $latestAndroidStudioTemplatesDir, restart Android Studio so that they appear in the 'New' menu\n"
  elif [[ "$OSTYPE" == "msys" ]]; then
    # Windows system
    # will get the most recent folder using 'stat'
    latestAndroidStudioTemplatesDir=$(find "$APPDATA/Google" -type d -name "AndroidStudio*" -exec stat -c "%Y %n" {} \; | sort -rn | head -n 1 | awk '{print $2}')
    if [ ! -d "$latestAndroidStudioTemplatesDir/fileTemplates" ]; then
        mkdir "$latestAndroidStudioTemplatesDir/fileTemplates"
    fi
    cp ./fileTemplates/*.* "$latestAndroidStudioTemplatesDir/fileTemplates"
    cp ./options/*.* "$latestAndroidStudioTemplatesDir/options"
    echo "The templates have been successfully copied to $latestAndroidStudioTemplatesDir, restart Android Studio so that they appear in the 'New' menu\n"
  else
    echo "Cannot detect the Android Studio installation path on this system."
  fi