PerfCI plugin for Jenkins
=========================

Introduction
------------

For the details of the PerfCI platform, please refer <https://docs.engineering.redhat.com/x/worlAQ>.

System Requirements
-------------------
### Operating System ###
* Linux or other Unix-like operating systems
* Windows with [Cygwin](https://www.cygwin.com)

### Java Runtime ###
* [Java SE 1.7 or above](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Perfcharts ###
* [Perfcharts 0.4.0 or above](http://vfreex.github.io/perfcharts)

### Jenkins ###
* Recommend [Jenkins 1.580.2 or above](http://jenkins-ci.org), because lower versions haven't been tested yet.

Compilation
-----------
1. Download the latest code by cloning or checking out the repo from Gerrit.
2. Confirm you have maven installed.

		yum install maven

3. Change to the directory that contains "pom.xml".
4. Run following command to build:

		mvn package

5. You will get _perfci.hpi_ in _target/_.

Installation & Configuration
----------------------------
1. Please install [Perfchart (the Chart Generation Tool) v0.4.0 or above](http://vfreex.github.io/perfcharts) on your _Jenkins Master_ in advance and then run Jenkins.
2. Install _perfci.hpi_ manually through Plugin Manager of your Jenkins (usually at <http://your_jenkins_server:8080/pluginManager/advanced>).
3. Associate the plugin with your installed Perfcharts. Open "Configure System" (usually at <http://your_jenkins_server:8080/configure>), and set "$CGT_HOME" to the location (without the "bin" subdirectory) of your installed Perfcharts.

Usage
-----
### Publish Performance & Resource Monitoring Report ###
1. Create a project, place the output test result to somewhere of the workspace. For demonstration, you can just copy some pre-generated data by following script:

           mkdir -p test_result/demo
           cp -R /path/to/your/test/result/directory/* test_result/demo

2. Add post-build action "Publish Performance & Resource Monitoring Report".
3. Specify the location of input test result files. It also support ANT styled file name pattern. For example, pattern "**/*.jtl,**/*.load,**/*.nmon" tells system that all files with ".jtl", ".load", or ".mmon" extension will be included for the report generation.
4. Save and build. Then you will get the generated report by clicking _Performance & Monitoring Report_ link on the left side of the build information page. You can associate tags to group your reports for further trend report generation.

### Publish Performance Trend Report ###
1. Open an existing project with Performance & Resource Monitoring reports generated.
2. Type a list of tags whose corresponding performance reports you want to include in your trend report. If your keep the text box empty, all valid performance reports of the project will be included.
3. Click the _generate_ button. The web page will refresh automatically.

Release Log
-----------

### v0.1.0 ###
- initial release



