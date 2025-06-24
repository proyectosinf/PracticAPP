#!/bin/sh

# This script copies each Xcode template found in the same directory as the script
# to ~/Library/Developer/Xcode/Templates/Rev/
#
# It can be run from anywhere.

echo "🦸 Updating templates"

# Get script location
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

PROJECT_NAME="PracticApp"
LIBRARY_TEMPLATES_DIR="$HOME/Library/Developer/Xcode/Templates/$PROJECT_NAME"

# Remove old templates and recreate directory
rm -rf "$LIBRARY_TEMPLATES_DIR"
mkdir -p "$LIBRARY_TEMPLATES_DIR"

# Find and copy all .xctemplate directories located at the same level as the script
find "$SCRIPT_DIR" -maxdepth 1 -type d -name "*.xctemplate*" -print0 | while IFS= read -r -d '' file
do
  basename=$(basename "$file")
  cp -R "$file" "$LIBRARY_TEMPLATES_DIR/"
  echo "📁 Moved $basename -> $LIBRARY_TEMPLATES_DIR/$basename"
done

echo "✅ Templates updated successfully!"
