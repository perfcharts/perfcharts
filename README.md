Perfcharts
=====================

Introduction
------------
Perfcharts is a free software written in Java, which reads performance testing and system monitoring results from Jmeter, NMON, and/or other applications to produce charts for further analysis. It can generate any line and bar chart from any kind of data with appropriate extensions, but now is specially designed for performance testing business.

With this tool, you can get analysis charts by just putting a Jmeter result file (.jtl), some NMON resource monitoring logs (.nmon), and CPU load monitoring logs (.load) into a directory then just running the tool. This tool make it possible to enable automatic performance testing.

System Requirements
-------------------
### Operating System ###
* Linux or other Unix-like operating systems
* Windows with [Cygwin](https://www.cygwin.com/)

###Java Runtime ###
* Java SE 1.7 or above (<http://www.oracle.com/technetwork/java/javase/downloads/index.html>)

Components
----------
The tool contains three main components: Parser, Generator, and Driver Program.

* _Parser_ reads raw data, extracts the part of data we are interested in, and produces data table (in CSV format).
* _Generator_ generates charts from data tables according to configuration files that is defined by users and specifies which charts should be produced.
* _Helpers_ scripts that helps you generate a report easier.

Usage
-----

### Build

- Install from Fedora

``` bash
# make sure you have JDK and Gradle installed
dnf install java-1.8.0-openjdk-devel
# build
make
# executables are located in build/dist
cd build/dist/bin
```

### Generate reports
(This is not a completed usage. Please refer to
section '[Obsoleted] Steps to Generate a Perf-test Report' for more information)

1. Create a directory, and put all performance testing output files to that directory.
Supported formats include:

- .jtl: Jmeter XML result file, or CSV result file with CSV header
- .nmon: monitoring log file produced by NMON.
- .load: CPU load monitoring file (see below)

2. Suppose your files are located in `/tmp/mytestresults`.
Use `perfcharts` executable to generate report：

``` bash
perfcharts gen perf-general /tmp/mytestresults
```

You will get your performance testing report in `/tmp/mytestresults`.

3. You can also try more report templates:

``` bash
# generate the same report using "perf-baseline" template
perfcharts gen perf-baseline /tmp/mytestresults
```

[Obsoleted] Steps to Generate a Perf-test Report
----------------------------------
### Use cgt-perf ###
We provide a shell script (cgt-perf) to simplify the process for perf-test report generation.

	Usage:
		cgt-perf [-d OUTPUT_DIR] [-o OUTPUT_FILE] [-z TIME_ZONE] [-f FROM_TIME] [-t TO_TIME] [INPUT_DIR]

#### Step 1 ####
Create a new directory, and put all related perf-test logs into the directory. 

You can have a single Jmeter XML log (.jtl) and several resource monitoring logs for input. 

* The log file from NMON should be named as "servername\[\_&lt;suffix&gt;\].nmon" (without quotes, &lt;suffix&gt; is optional), like "errata-web_140717_1258.nmon". 

* CPU load logs should be named as "servername\[\_&lt;suffix&gt;\].load", like "errata-web_140717_1258.load". You can get this kind of CPU load logs by writing a simple shell script. The format of log entry is:

		yyyy-MM-dd hh:mm:ss, 1min 5min 15min\[, cores\]

Here is a sample CPU load monitoring log:

		2014-07-17 17:12:43, 0.73 0.99 1.27, 8
		2014-07-17 17:12:48, 0.73 1.01 1.33, 8
		2014-07-17 17:12:53, 0.73 0.99 1.22, 8
		2014-07-17 17:12:58, 0.73 1.01 1.28, 8

#### Step 2 ####
Enter the directory you just created, and run cgt-perf in bin/.

		cgt-perf

Or you can run cgt-perf.sh INPUT_DIR to indicate the input directory manually:

		cgt-perf /home/cgt/input/

All generated files will be placed into the sub-directory "report" of your input directory. This is the default location for storing all output files. To specify another output directory, use -d option:

		cgt-perf -d home/cgt/output/ /home/cgt/input/

The file "report.html" in output directory is what you need. To specify another location for the generated report, use -o option:

		cgt-perf -d home/cgt/output/ /home/cgt/input/ -o my_report.html

### Advanced usage ###

#### Step 1 Run appropriate parser ####
The parser will convert your raw data to data table. All predefined parser reads raw data from standard input stream and put the result into standard output stream.

		java -jar lib/chartgeneration-parser-jmeter.jar < example.jtl > jmeter.csv
		java -jar lib/chartgeneration-parser-nmon.jar < errata-web_140707_0000.nmon > errata-web.csv
		java -jar lib/chartgeneration-parser-cpuload.jar < errata-web_140707_0000.load >> errata-web.csv

#### Step 2: Run Generator to produce charts ####
The generator reads a data table (CSV file) from standard input stream and put the result into standard output stream by default.

		Usage: java -jar lib/chartgeneration.jar <configuration_file>

The configuration file defines which charts should be generated for the target report. We have provides some templates in shared/ for perf-test business.

		java -jar lib/chartgeneration.jar shared/jmeter.conf < jmeter.csv > jmeter.json
		java -jar lib/chartgeneration.jar shared/res.conf < errata-web.csv > res.json

The instructions to providing your own configuration files are out of the section, and will be presented in a separated document.

#### Step 3: Show charts in web pages ####
The data of generated charts will be stored into JSON files, which is optimized for JQuery Flot plotting library (see http://www.flotcharts.org/ ). You can load these JSON files into your web pages, and customize the format of your report as you want.

Here is the format of output JSON:

		{charts: [ {
			title: <title of your chart>,
			subtitle: <subtitle of your chart>,
			xLabel: <the label of x-axis>,
			yLabel: <the label of y-axis>,
			xaxisMode: "NUMBER" | "TIME" | "CATEGORIES", /* The mode of x-axis */
			series: <The series of the chart, which Flot use to plot charts>
			}, ...
		]}

The detailed guidelines for customize reports is out of the section, and will be discussed in a separated document.

Steps to Generate a Perf-trend Report
-------------------------------------
### Use cgt-trend ###
We provide a shell script (cgt-trend) to simplify the process for perf-trend report generation.

	Usage:
		cgt-trend [-d OUTPUT_DIR] [-o OUTPUT_FILE] [-z TIME_ZONE] [-f FROM_TIME] [-t TO_TIME] [INPUT_DIR]

#### Step 1 ####
You should run cgt-perf to create some performance reports before generating a perf-trend report.

Create a plain text file. The file has several lines. Each line contains a build number, a comma, and a associated location of performance result (with .json extension name).
An example (trend_input.txt):

		1,/home/vfreex/.jenkins/perfcharts1/report/tmp/subreports/Performance.json
		2,/home/vfreex/.jenkins/perfcharts2/report/tmp/subreports/Performance.json
		4,/home/vfreex/.jenkins/perfcharts4/report/tmp/subreports/Performance.json
		7,/home/vfreex/.jenkins/perfcharts7/report/tmp/subreports/Performance.json

#### Step 2 ####
Create a directory for holding all generated files.

Enter the directory you just created, and run cgt-trend in bin/.

		cgt-trend

Or you can run cgt-trend INPUT_DIR to indicate the input directory manually:

		cgt-trend /home/cgt/input/

All generated files will be placed into the subdirectory "output" in your input directory. This is the default location for storing all output files. To specify another output directory, use -d option:

		cgt-trend -d home/cgt/output/ /home/cgt/input/

The file "report.html" in output directory is what you need. To specify another location for the generated report, use -o option:

		cgt-trend -d home/cgt/output/ /home/cgt/input/ -o my_report.html

Build
-----------
1. This software uses [Gradle](http://gradle.org/) to compile the Java source and [GNU Make](https://www.gnu.org/software/make/) to build RPM packages and so on. To build this software, please install gradle and make in advance.

- for RHEL/CentOS/Fedora users
		
		yum install gradle make

- for Debian/Ubuntu users
		
		apt-get install gradle make

2. Run `make` or `gradle explodedDist` to build this software. You can find compiled package in `build/dist/`

3. To build a RPM or source RPM package, just run `make rpm` or `make srpm` corespondly.

Release Log
-----------
### v0.6.1 ###
- upgrade CSV lib
- bugfix

### v0.6.0 ###
- move build system from Ant to Gradle
- rename package name from 'chartgeneration' to 'perfcharts'

### v0.5.2 ###
- bugfix: parser crashes under some locale environment

### v0.5.1 ###
- add support perf-cmp report
- add new style summary table and bar chart support
- add a CPU load montoring script
- upgrade JQuery lib to 2.1.3
- remove TOTAL series from Disk Busy Chart
- support JRE 1.7
- and other improvements

### v0.5.0 ###
- new feature: add partial support for Zabbix monitor (with cgt-zabbix-dl tool)
- new feature: sort support
- multithread acceleration
- bug-fix: Average Response Time Trend Chart uses arithmetic average. Changed to weighted average.

### v0.4.2 ###
- new feature: add support for Swap In / Out Chart
- new feature: add support for transaction exclusion
- bug-fix: Composite Chart doesn't support resource monitoring charts
- bug-fix: incorrect VU, HITS, and TPS charts
- String x-tick support, and adjust styles of history trend chart
- dynamic y-axes position support

### v0.4.1 ###
- bug-fix: class SumByLabelCalculation has a bug, which causes Disk IO Chart and Disk Busy Chart sometimes go wrong
- remove check logic for storage device names

### v0.4.0 ###
- add trend report generation support
- some bug-fixes

### v0.3.4 ###
- new style of axis labels
- other details related to the format of report

### v0.3.3 ###
- support for customizing the title and subtitle of composite chart
- change the unit of response time to 'ms'
- change the series name format in composite chart
- automatic interval choosing improvements

### v0.3.2 ###
- some bug-fixes
- reorganize some code


### v0.3.1 ###
- new feature: support for specifying time duration
- new feature: automatic interval choosing
- new feature: Jmeter CSV log support
- change: new rules for generating Jmeter charts
- bug-fix: the time zone issue
- bug-fix: incorrect calculation of summary chart when a transaction only has one sample

### v0.3.0 ###
- new features: summary chart for Jmeter Test logs
- bug-fix: mismatching between CPU load logs and its parser

### v0.2.0 ###
- add support for subtitles
- add support for the number of cores in CPU load charts
- rewrite the driver program in Bash shell script
- new document
- some bug-fixes

### v0.1.0 ###
- first release

