#!/bin/bash

SELF=$BASH_SOURCE
DIR=`dirname -- "$SELF"`
JAVA_CMD=java
YUICOMPRESSOR='../../../../../../../tools/yuicompressor-2.4.8.jar'
REPORT_NORMAL_FILENAME='report.template.html'
REPORT_MONO_FILENAME='report-mono.template.html'

# HTML head
echo '<!DOCTYPE html>
<html lang="en">
<head>' | tee "$DIR/$REPORT_NORMAL_FILENAME" "$DIR/$REPORT_MONO_FILENAME" > /dev/null
cat "$DIR"/partial/head.html | tee -a "$DIR/$REPORT_NORMAL_FILENAME" "$DIR/$REPORT_MONO_FILENAME" > /dev/null

# import CSS files
echo '<style>' >> "$DIR/$REPORT_MONO_FILENAME"
while read line; do
  if [ -z "$line" ]; then continue; fi
  echo '<link href="'"$line"'" rel="stylesheet" />' >> "$DIR/$REPORT_NORMAL_FILENAME"
  "$JAVA_CMD" -jar "$DIR/$YUICOMPRESSOR" "$DIR/$line" >> "$DIR/$REPORT_MONO_FILENAME"
done < "$DIR"/css.list
echo '</style>' >> "$DIR/$REPORT_MONO_FILENAME"
echo '</head>
<body>' | tee -a "$DIR/$REPORT_NORMAL_FILENAME" "$DIR/$REPORT_MONO_FILENAME" > /dev/null

# HTML body
cat "$DIR"/partial/body.html | tee -a "$DIR/$REPORT_NORMAL_FILENAME" "$DIR/$REPORT_MONO_FILENAME" > /dev/null

# import JS files
echo '<script src="data.js"></script>' >> "$DIR/$REPORT_NORMAL_FILENAME"
echo '<script>' >> "$DIR/$REPORT_MONO_FILENAME"
while read line; do
  if [ -z "$line" ]; then continue; fi
  echo '<script src="'"$line"'"></script>' >> "$DIR/$REPORT_NORMAL_FILENAME"
  "$JAVA_CMD" -jar "$DIR/$YUICOMPRESSOR" "$DIR/$line" >> "$DIR/$REPORT_MONO_FILENAME"
done < "$DIR"/js.list
echo '</script>' >> "$DIR/$REPORT_MONO_FILENAME"

# HTML end
echo '</body>
</html>' | tee -a "$DIR/$REPORT_NORMAL_FILENAME" "$DIR/$REPORT_MONO_FILENAME" > /dev/null

