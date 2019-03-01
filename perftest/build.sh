#!/bin/bash
SELF=$BASH_SOURCE
DIR=`dirname -- "$SELF"`

echo 'generating report templates...'
"$DIR"/src/main/dist/res/report_templates/perf-baseline/html/gen.sh
"$DIR"/src/main/dist/res/report_templates/perf/generate_mono_report_template.sh > "$DIR"/src/main/dist/res/report_templates/perf/report-mono.template.html
"$DIR"/src/main/dist/res/report_templates/perf/generate_report_template.sh > "$DIR"/src/main/dist/res/report_templates/perf/report.template.html