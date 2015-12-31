#!/bin/bash
SELF=`readlink -e -- "$0"`
DIR=`dirname -- "$SELF"`

echo 'generating report templates...'
"$DIR"/src/main/dist/res/report_templates/perf-baseline/html/gen.sh
