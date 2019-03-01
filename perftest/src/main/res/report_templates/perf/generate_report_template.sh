#!/bin/bash
# generate a report template

cd "$(dirname -- $BASH_SOURCE)"

included_scripts=('lib/jquery-2.1.3.js' 'lib/jquery.tablesorter.js' 'lib/jquery.stickytableheaders.js' 'lib/flot/jquery.flot.js lib/flot/jquery.flot.time.js' 'lib/flot/jquery.flot.categories.js' 'lib/flot/jquery.flot.selection.js' 'lib/flot/jquery.flot.crosshair.js' 'lib/flot/jquery.flot.axislabels.js' 'js/perfcharts.js' 'data.js')
included_stylesheets=('css/tablesorter/theme.default.css' 'css/default-style.css')
included_htmls=('partial/body.html')

# HTML Declaration and Headers
echo '<!DOCTYPE html>'
echo '<html lang="en-US">'
echo '<head>'
echo '<meta charset="UTF-8">'
echo '<title>Perfcharts Report</title>'

# included Javascript files
for js in ${included_scripts[@]}; do
	echo "<script type=\"text/javascript\" src=\"$js\"></script>"
done

# included css files
for css in ${included_stylesheets[@]}; do
	echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"$css\" />"
done

echo '</head>'

# body
echo '<body>'
for html in ${included_htmls[@]}; do
	cat "$html"
done
echo '</body>'
echo '</html>'

