ChartGeneration = {};
ChartGeneration.data = [];
(function($){ $(function() {
var AppRouter = Backbone.Router.extend({
    routes: {
        "":"index",
//        "!/report/performance-test": "switchToPerformance",
//        "!/report/performance-test/:id": "showPerformanceChart",
//        "!/report/system-resource/:host": "switchToHost",
//        "!/report/system-resource/:host/:id": "showSystemResourceChart",
          "!/custom-chart": "switchToCustomChart",
          "!/report/:groupName/:reportID": "showReport",
          "!/report/:groupName/:reportID/:chart": "showChart"
    }
});
// Instantiate the router
var app_router = new AppRouter;

var $mainNav = $("#mainNav");
var $chartTabs = $('#chartTabs');
var $chart_container = $("#chart-container");


// reorganize loaded report
Reports = {}; // to locate a report, use Reports[group][reportID], where "" donates default report group
for (var i = 0; i < ChartGeneration.data.length; ++i) {
    var originalReport = ChartGeneration.data[i];
    var groupName = originalReport.groupName === undefined || originalReport.groupName == '' ? '' : originalReport.groupName;
    var reportID = originalReport._reportID = originalReport.key ? originalReport.key : originalReport.title;
    if (!Reports[groupName]) {
        Reports[groupName] = {}; // create new report group
    }
    var reportGroup = Reports[groupName];
    reportGroup[reportID] = originalReport;
}
var groupNames = []; // sorted group names
for (var groupName in Reports) {
    groupNames.push(groupName);
}
groupNames = groupNames.sort();
var groupIndices = {}; // groupName->index in groupNames map
for (var i = 0; i < groupNames.length; ++i) {
    var groupName = groupNames[i];
    var group = Reports[groupName];
    groupIndices[groupName] = i;
    // create menu item on main nav bar
    if (groupName === "") { //default group
        for (var reportName in group) {
            var report = group[reportName];
            var $menuItem = $("<li/>").appendTo($mainNav);
            var $linkToReport = $("<a/>").text(report.title).attr("href", "#!/report/_/" + encodeURIComponent(report._reportID)).appendTo($menuItem);
        }
    } else {
        var $menuItem = $("<li/>").addClass("dropdown").appendTo($mainNav);
        var $linkToReport = $("<a/>")
            .text(groupName)
            .attr("href", "#")
            .addClass("dropdown-toggle")
            .attr("data-toggle", "dropdown")
            .attr("role", "button")
            .attr("aria-haspopup", true)
            .attr("aria-expanded", false)
            .append($("<span/>").addClass("caret"))
            .appendTo($menuItem);
        var $submenu = $("<ul/>").addClass("dropdown-menu").appendTo($menuItem);
        for (var reportName in group) {
             var report = group[reportName];
             var $menuItem = $("<li/>").appendTo($submenu);
             var $linkToReport = $("<a/>").text(report.title).attr("href", "#!/report/" + encodeURIComponent(groupName) + "/" + encodeURIComponent(report._reportID)).appendTo($menuItem);
        }
    }
}

app_router.on('route:index', function() {
    if (groupNames.length == 0)
        return;
    var groupName = groupNames[0];
    var group = Reports[groupName];
    if (!group)
        return;
    var reportID = null;
    for (var key in group) {
        if (group[key])
            reportID = key;
        break;
    }
    if (reportID)
        location.hash = '!/report/' + encodeURIComponent(groupName === '' ? '_' : groupName) + '/' + encodeURIComponent(reportID);
});


function showReport(groupName, reportID) {
    if (groupName === '_')
        groupName = '';
    var group = Reports[groupName];
//    if (groupName !== currentGroupName) {
        $("#my-navbar li").removeClass('active');
        if (!group) {
            alert("invalid path parameter: unknown report group");
            return false;
        }
        //alert(groupName + "/" +  reportID);
        currentGroupName = groupName;
        $("#mainNav>li:nth-child(" + (groupIndices[groupName] + 1) + ")").addClass('active');
//    } else if (reportID === currentReportID) {
//        return false;
//    }

    var report = group[reportID];
    if (!report) {
        alert("invalid path parameter: unknown report ID");
        return;
    }
    currentReportID = reportID;

    if (!report.chartMap) {
        report.chartMap = {}; // chartID -> chart map
        for (var i = 0; i < report.charts.length; ++i) {
            var item = report.charts[i];
            item._chartID = item.key ? item.key : i;
            report.chartMap[item._chartID] = item;
        }
    }
    var $chartTabs = $('#chartTabs');
    // create chart tabs
    createChartTabs(groupName, reportID, report.charts, $chartTabs);
    $chartTabs.data("my-current", reportID);
    return true;
}

app_router.on('route:showReport', function( groupName, reportID ){
    if (!showReport(groupName, reportID))
        return;
    var report = Reports[groupName==='_'? '' : groupName][reportID]
    var firstChart = report.charts.length > 0 ? report.charts[0] : null;
    if (firstChart) // automatically show the first chart in this report
        location.hash += "/" + encodeURIComponent(firstChart._chartID);
});


function createChartTabs(groupName, reportID, charts, $tab) {
    $tab.html("");
    for (var i = 0; i < charts.length; ++i) {
        var item = charts[i];
        var tab = $("<li/>").attr("role", "presentation").appendTo($tab);
        if (i==0)
            tab.addClass("active");
        var chartID = item._chartID;
        var tabLink = $("<a/>")
        .attr("role", "tab")
        .attr("data-target", "chart-container")
        .attr("data-chart-id", chartID)
        .attr("href", "#!/report/" + encodeURIComponent(groupName === '' ? '_' : groupName)  + "/" + encodeURIComponent(reportID) + "/" + encodeURIComponent(chartID))
        .text(item.title)
        .appendTo(tab);
    }
}

app_router.on('route:showChart', function( groupName, reportID, chartID ){
    if (groupName === '_')
        groupName = '';
    var group = Reports[groupName];
    if (!group) {
        alert("invalid path parameter: unknown report group");
        return;
    }
    var report = group[reportID];
    if (!report) {
        alert("invalid path parameter: unknown report ID");
        return;
    }
    //switch to corresponding tab
    //$("#my-navbar li").removeClass('active');
    //$("#mainNav>li:nth-child(2)").addClass('active');
    if ($chartTabs.data("my-current") !== reportID) {
         showReport(groupName, reportID);
    }
    $("#chartTabs a[data-chart-id=\"" + chartID + "\"]").tab('show');
    // clear old chart
    $("#chart-container").html("");
    var chart = report.chartMap[chartID];
    if (!chart) {
        return;
    }
    var painter = Perfcharts.Painter.create(chart);
    painter.paint($chart_container, chart);
});

app_router.on('route:switchToCustomChart', function() {
    //switch to corresponding tab
    $("#my-navbar li").removeClass('active');
    $("#my-nav-right>li:nth-child(1)").addClass('active');

    // clear chart tabs
    $chartTabs.html("");

    var compositeChartManager = new Perfcharts.CompositeChartManager();
    compositeChartManager.setup($chart_container, ChartGeneration.data)
});

$("<div class='perfchart-tooltip'/>").css({
position : "absolute",
display : "none",
border : "1px solid #fdd",
padding : "2px",
"background-color" : "#fee",
opacity : 0.80
}).appendTo("body");

Backbone.history.start();
});
})(jQuery);
