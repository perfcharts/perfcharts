
(function($){ $(function() {
var DEFAULT_ROUTE="!/report/performance-test";

var AppRouter = Backbone.Router.extend({
    routes: {
        "":"index",
        "!/report/performance-test": "switchToPerformance",
        "!/report/performance-test/:id": "showPerformanceChart",
        "!/report/system-resource/:host": "switchToHost",
        "!/report/system-resource/:host/:id": "showSystemResourceChart",
        "!/custom-chart": "switchToCustomChart",
    }
});
// Instantiate the router
var app_router = new AppRouter;

app_router.on('route:index', function() {
    location.hash = DEFAULT_ROUTE;
});

// initialize menu entries for monitoring reports
var $monitoredHostMenu = $("#monitoredHostMenu");
if (ChartGeneration.data.length <= 1) {
    // no monitoring reports, hide this menu
    $("#mainNav li:nth-child(2)").hide();
}
else {
    for (var i = 1; i < ChartGeneration.data.length; ++i) {
        var item = ChartGeneration.data[i];
        var reportID = item.key ? item.key : i;
        var $newMenuItem = $("<li/>").appendTo($monitoredHostMenu);
        $("<a/>").attr("href","#!/report/system-resource/"+reportID)
        .text(item.title).appendTo($newMenuItem);
    }
}

function createTabHeadsForPerformanceReport(charts, $tab) {
    $tab.html("");
    for (var i = 0; i < charts.length; ++i) {
        var item = charts[i];
        var tab = $("<li/>").attr("role", "presentation").appendTo($tab);
        if (i==0)
            tab.addClass("active");
        var chartID = item.key ? item.key : i;
        var tabLink = $("<a/>")
        .attr("role", "tab")
        .attr("data-target", "chart-container")
        .attr("data-chart-id", chartID)
        .attr("href", "#!/report/performance-test/" + chartID)
        .text(item.title)
        .appendTo(tab);
    }
}

function createTabHeadsForMonitoringReport(hostID, charts, $tab) {
    $tab.html("");
    for (var i = 0; i < charts.length; ++i) {
        var item = charts[i];
        var tab = $("<li/>").attr("role", "presentation").appendTo($tab);
        if (i==0)
            tab.addClass("active");
        var chartID = item.key ? item.key : i;
        var tabLink = $("<a/>")
        .attr("role", "tab")
        .attr("data-target", "chart-container")
        .attr("data-chart-id", chartID)
        .attr("href", "#!/report/system-resource/" + hostID + "/" + chartID)
        .text(item.title)
        .appendTo(tab);
    }
}

// create report description
var $description = $("<div/>").html(ChartGeneration.data.description).appendTo("#report-description");

var $chartTabs = $('#chartTabs');
app_router.on('route:switchToPerformance', function( id ){
    $("#my-navbar li").removeClass('active');
    $("#mainNav>li:nth-child(1)").addClass('active');
    // create chart tabs
    var perfReport = ChartGeneration.data[0];
    createTabHeadsForPerformanceReport(perfReport.charts, $chartTabs);
    $chartTabs.data("my-current", 0);
    var firstChart = perfReport.charts.length > 0 ? perfReport.charts[0] : null;
    if (firstChart) {
        var id = firstChart.key ? firstChart.key : 0;
        location.hash += "/" + (id);
    }
});

var $chart_container = $("#chart-container");
app_router.on('route:showPerformanceChart', function( id ){
    var report = ChartGeneration.data[0];
    //switch to corresponding tab
    $("#my-navbar li").removeClass('active');
    $("#mainNav>li:nth-child(1)").addClass('active');

    if ($chartTabs.data("my-current") !== 0) {
        createTabHeadsForPerformanceReport(report.charts, $chartTabs);
        $chartTabs.data("my-current", 0);
    }

    $("#chartTabs a[data-chart-id=\"" + id + "\"]").tab('show');
    // discard old chart
    $chart_container.html("");

    //
    var perfReport = {};
    perfReport.charts = {};
    for (var i = 0; i < report.charts.length; ++i) {
        var item = report.charts[i];
        perfReport.charts[i] = item;
        if (item.key)
            perfReport.charts[item.key] = item;
    }

    var chart = perfReport.charts[id];
    if (!chart) {
        return;
    }
    var painter = Perfcharts.Painter.create(chart);
//    $("<div id='tooltip'></div>").css({
//    			position : "absolute",
//    			display : "none",
//    			border : "1px solid #fdd",
//    			padding : "2px",
//    			"background-color" : "#fee",
//    			opacity : 0.80
//    		}).appendTo($chart_container);
    painter.paint($chart_container, chart);
});

app_router.on('route:switchToHost', function( host ){
    $("#my-navbar li").removeClass('active');
    $("#mainNav>li:nth-child(2)").addClass('active');
    var report = ChartGeneration.data[host];
    var perfReport = {};
    perfReport.charts = {};
    for (var i = 1; i < report.charts.length; ++i) {
        var item = report.charts[i];
        var chartID = item.key ? item.key : i;
        perfReport.charts[chartID] = item;
    }
    var $chartTabs = $('#chartTabs');
    // create chart tabs
    createTabHeadsForMonitoringReport(host, report.charts, $chartTabs);
    $chartTabs.data("my-current", host);
    var firstChart = report.charts.length > 0 ? report.charts[0] : null;
    if (firstChart) {
            var id = firstChart.key ? firstChart.key : 0;
            location.hash += "/" + (id);
    }

});

app_router.on('route:showSystemResourceChart', function( host, id ){
    var report = ChartGeneration.data[host];
    //switch to corresponding tab
    $("#my-navbar li").removeClass('active');
    $("#mainNav>li:nth-child(2)").addClass('active');
    if ($chartTabs.data("my-current") !== host) {
        createTabHeadsForMonitoringReport(host, report.charts, $chartTabs);
        $chartTabs.data("my-current", host);
    }
    $("#chartTabs a[data-chart-id=\"" + id + "\"]").tab('show');
    // clear old chart
    $("#chart-container").html("");
    var perfReport = {};
    perfReport.charts = {};
    for (var i = 0; i < ChartGeneration.data[host].charts.length; ++i) {
        var item = ChartGeneration.data[host].charts[i];
        perfReport.charts[i] = item;
        if (item.key)
            perfReport.charts[item.key] = item;
    }

    var chart = perfReport.charts[id];
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

//$('#chartTabs a').click(function (e) {
//  $(this).tab("show");
//})
});
})(jQuery);
