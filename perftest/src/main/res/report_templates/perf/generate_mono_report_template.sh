#!/bin/bash
# generate a mono report template

cd "$(dirname -- $BASH_SOURCE)"
basepath='.'

included_scripts=('lib/jquery-2.1.3.min.js' 'lib/jquery.tablesorter.min.js' 'lib/jquery.stickytableheaders.min.js' 'lib/flot/jquery.flot.min.js lib/flot/jquery.flot.time.min.js' 'lib/flot/jquery.flot.categories.min.js' 'lib/flot/jquery.flot.selection.min.js' 'lib/flot/jquery.flot.crosshair.min.js' 'lib/flot/jquery.flot.axislabels.min.js')
included_stylesheets=('css/tablesorter/theme.default.css' 'css/default-style.css')
included_htmls=('partial/body.html')

YUICOMPRESSOR=../../../../../../tools/yuicompressor-2.4.8.jar

# HTML Declaration and Headers
echo '<!DOCTYPE html>'
echo '<html lang="en-US">'
echo '<head>'
echo '<meta charset="UTF-8">'
echo '<title>Perfcharts Report</title>'

# included Javascript files
for js in ${included_scripts[@]}; do
	echo '<script type="text/javascript">'
	cat "$basepath/$js"
	echo '</script>'
done

echo '<script type="text/javascript">'
java -jar "$YUICOMPRESSOR" "$basepath"/js/perfcharts.js
echo '</script>'

# included css files
for css in ${included_stylesheets[@]}; do
	echo '<style type="text/css">'
	cat "$basepath/$css"
	echo '</style>'
done

echo '</head>'

# body
echo '<body>'
for html in ${included_htmls[@]}; do
	cat "$basepath/$html"
done
echo '</body>'
echo '</html>'

