if (typeof Perfcharts === "undefined")
    Perfcharts = {};

//abstract class Painter
Perfcharts.Painter = function() {
};
Perfcharts.Painter.prototype = {
    paint : function($node, data) {
    }
};
Perfcharts.Painter.create = function (chart) {
    if (chart.chartType === "TABLE")
        return new Perfcharts.TablePainer();
    if (chart.xaxisMode === "BAR_STRING")
        return new Perfcharts.BarChartPainer();
    return new Perfcharts.FlotChartPainter();
};

// class TablePainer : Painter
Perfcharts.TablePainer = function() {
};
Perfcharts.TablePainer.prototype = new Perfcharts.Painter();

Perfcharts.TablePainer.prototype.paint = function($chart, chart) {
    function createTableCell(cell) {
    		var isFloat = cell.valueType === "double" || cell.valueType === "float";
    		var isNumber = isFloat || typeof cell.value === "number"
    				|| cell.valueType === "int" || cell.valueType === "long";
    		var showText = cell.value;
    		if (isFloat) {
    			showText = cell.value !== null ? cell.value.toFixed(3)
    					: (cell.rawValue === "NaN" ? "N/A" : cell.rawValue);
    		}
    		var $td = $("<td/>").text(showText);
    		if (cell.cssClass)
    			$td.addClass(cell.cssClass);
    		if (isNumber)
    			$td.css("text-align", "right");
    		return $td;
    }

    $chart.append($("<h3 class='perfcharts-title'/>").text(chart.title)).append(
    				$("<h4 class='perfcharts-subtitle'/>").text(chart.subtitle));
    		var $table_container = $("<div class='perfcharts-table-container'>")
    				.appendTo($chart);
    		var $table = $("<table class='table table-bordered perfcharts-table'/>").appendTo(
    				$table_container);
    		if (chart.key)
    			$table.attr("data-key", chart.key);
    		if (chart.header) {
    		    var $thead = $("<thead/>").appendTo($table);
    			var $tableHeaderRow = $("<tr/>").appendTo($thead);
    			for (var i = 0; i < chart.header.length; ++i) {
    				var $th = $("<th/>").text(chart.header[i]).appendTo($tableHeaderRow);
    				if (chart.headerTooltip && chart.headerTooltip[i])
    				    $th.attr('title', chart.headerTooltip[i]);
    			}
    		}
    		if (chart.topRows) {
    			var $thead = $("<tbody class=\"tablesorter-no-sort\"/>").appendTo(
    					$table);
    			for (var i = 0; i < chart.topRows.length; ++i) {
    				var $tableRow = $("<tr/>").appendTo($thead);
    				var row = chart.topRows[i];
    				for (var j = 0; j < row.length; ++j) {
    					var cell = row[j];
    					createTableCell(row[j]).appendTo($tableRow);
    				}
    			}
    		}
    		if (chart.rows) {
    			var $tbody = $("<tbody/>").appendTo($table);
    			for (var i = 0; i < chart.rows.length; ++i) {
    				var $tableRow = $("<tr/>").appendTo($tbody);
    				var row = chart.rows[i];
    				for (var j = 0; j < row.length; ++j) {
    					var cell = row[j];
    					createTableCell(row[j]).appendTo($tableRow);
    				}
    			}
    		}
    		if (chart.bottomRows) {
    			var $tfoot = $("<tfoot class=\"tablesorter-no-sort\"/>").appendTo(
    					$table);
    			for (var i = 0; i < chart.bottomRows.length; ++i) {
    				var $tableRow = $("<tr/>").appendTo($tfoot);
    				var row = chart.bottomRows[i];
    				for (var j = 0; j < row.length; ++j) {
    					var cell = row[j];
    					createTableCell(row[j]).appendTo($tableRow);
    				}
    			}
    		}
    		$table.tablesorter({
    			cssInfoBlock : "tablesorter-no-sort"
    		}).stickyTableHeaders();
};

	function BITree(N) {
		this.N = N;
		this.data = new Array();
	}
	BITree.prototype = {
		lowbit : function(x) {
			return x & -x;
		},
		update : function(index, value) {
			index = Number.parseInt(index);
			while (index <= this.N) {
				if (!this.data[index])
					this.data[index] = value;
				else
					this.data[index] += value;
				index += this.lowbit(index);
			}
		},
		getSum : function(index) {
			index = Number.parseInt(index);
			var s = 0;
			while (index > 0) {
				s += this.data[index] ? this.data[index] : 0;
				index -= this.lowbit(index);
			}
			return s;
		},
		get : function(index) {
			return this.getSum(index) - this.getSum(index - 1);
		}
	};
    function on_cb_group_list_item_changed(e) {
        		var $this = $(e.target);
        		var state = $this.is(":checked");
        		var tag = $this.data("tag");
        		var chart = tag.chart;
        		var groupShowBIT = chart._groupShowBIT;
        		var plot = chart.plot;
        		// isGroupShowMap[tag.sid] = state;
        		groupShowBIT.update(tag.sid, state ? 1 : -1);
        		regenerateShowSeriesForBarChart(chart, plot.getOptions());
        		var xaxisOption = plot.getXAxes()[0].options;
        		xaxisOption._zoomed = false;
        		xaxisOption.min = plot.getOptions().xaxis.min;
        		xaxisOption.max = plot.getOptions().xaxis.max;
        		redraw(plot.getPlaceholder(), plot, chart, false);
        	}
    function resetZooming($placeholder) {
        var plot = $placeholder.data("plot");
        $.each(plot.getXAxes(), function(_, axis) {
            var opts = axis.options;
            if (opts._zoomed) {
                opts.min = opts._min;
                opts.max = opts._max;
                opts._zoomed = false;
            }
        });
        $.each(plot.getYAxes(), function(_, axis) {
            var opts = axis.options;
            if (opts._zoomed) {
                opts.min = opts._min;
                opts.max = opts._max;
                opts._zoomed = false;
            }
        });
        redraw($placeholder, plot, null);
    }
    function registerEvents($placeholder) {
		$placeholder.off();
		// zooming
		$placeholder.bind("plotselected", function(event, ranges) {
			var plot = $placeholder.data("plot");
			// if (true) {
			var xaxes = plot.getXAxes();
			for (var i = 0; i < xaxes.length;) {
				var opts = xaxes[i++].options;
				var r = i > 1 ? ranges["x" + i + "axis"] : ranges.xaxis;
				if (!r)
					continue;
				if (!opts._zoomed) {
					opts._min = opts.min ? opts.min : null;
					opts._max = opts.max ? opts.max : null;
					opts._zoomed = true;
				}
				opts.min = r.from;
				opts.max = r.to;
			}
			var yaxes = plot.getYAxes();
			for (var i = 0; i < yaxes.length;) {
				var opts = yaxes[i++].options;
				var r = i > 1 ? ranges["y" + i + "axis"] : ranges.yaxis;
				if (!r)
					continue;
				if (!opts._zoomed) {
					opts._min = opts.min ? opts.min : null;
					opts._max = opts.max ? opts.max : null;
					opts._zoomed = true;
				}
				opts.min = r.from;
				opts.max = r.to;
			}
			redraw($placeholder, plot, $placeholder.data("chart"));
			plot.clearSelection();
			// }
		});

		// tooltip
		$placeholder.bind("plothover", function(event, pos, item) {
			// var plot = $placeholder.data("plot");
			var chart = $placeholder.data("chart");
			if (item) {
				var x;
				switch (item.series.xaxis.options.mode) {
				case "time":
					x = new Date(item.datapoint[0]).toUTCString();
					break;
				default:
					x = item.datapoint[0];
					switch (chart.xaxisMode) {
					case "CATEGORIES":
						x = item.series.label;
						break;
					case "BAR_STRING":
						if (chart._groupId2RawXMap && chart.stringMap) {
							x = chart.stringMap[chart._groupId2RawXMap[Math
									.floor(x)]];
						}
						break;
					default:
						if (chart.xaxisTicks) {
							var tickLabel = binary_search_for_tick(
									chart.xaxisTicks, x);
							if (tickLabel)
								x = tickLabel;
						}
						break;
					}
					break;
				}

				var y = item.datapoint[1].toFixed(3);
				if (item.series._unit && item.series._unit.value)
					y += " " + item.series._unit.value;
				$(".perfchart-tooltip").html(
						"<b>" + item.series.label + "</b><br />" + x + "<br />"
								+ y).css({
					top : item.pageY + 5,
					left : item.pageX + 5
				}).fadeIn(200);
			} else {
				$(".perfchart-tooltip").hide();
			}
		});
		// reset zooming
		$placeholder.bind("dblclick", function() {
			resetZooming($placeholder);
		});
		// click to hide
		$placeholder.bind("plotclick", function(event, pos, item) {
			if (item) {
				var chart = $placeholder.data("chart");
				var activeSeries = chart.series[item.seriesIndex];
				var ita = chart._interactive = !chart._interactive;
				for (var i = 0; i < chart.series.length; ++i) {
					var series = chart.series[i];
					var hide = i != item.seriesIndex && ita;
					if (!series.lines)
						series.lines = {};
					series.lines.show = hide ? false : series.showLines;
					if (!series.bars)
						series.bars = {};
					series.bars.show = hide ? false : series.showBars;
					if (!series.points)
						series.points = {};
					series.points.show = hide ? false : series.showPoints;
					series._show = !hide;
				}
				redraw(chart.plot.getPlaceholder(), chart.plot, chart, true);
			}
		});
	}
	function labelFormatter(label, series) {
		var show = true;
		if (series._show === false)
			show = false;
		return '<a class="'
				+ (show ? 'series_label_shown' : 'series_label_hidden')
				+ '" onclick="ChartGeneration.eventHandlers.toggleSeries(this, '
				+ series._reportIndex + ", " + series._chartIndex + ", "
				+ series._index + ');">' + label + '</a>';
	}
	function yTickFormatter(num, yaxis) {
		var str, intLength, result, negative = num < 0, dotPos;
		if (yaxis.tickSize >= 1) {
			str = Math.round(num).toString();
			dotPos = str.length;
		} else if (yaxis.tickSize >= 0.1) {
			str = num.toFixed(1);
			dotPos = str.length - 2;
		} else {
			str = num.toFixed(2);
			dotPos = str.length - 3;
		}
		var p = negative ? 1 : 0;
		intLength = dotPos - p;
		if (intLength <= 3)
			return str;
		result = negative ? "-" : "";
		for (var i = p; i < dotPos; ++i) {
			result += str[i];
			var bits = dotPos - 1 - i;
			if (bits && !(bits % 3))
				result += ",";
		}
		return result + str.substring(dotPos);

	}
	function draw($placeholder, $legend, chart, optionsHook) {
		if (!chart.yaxes) {
			chart.yaxes = [];
			chart.yaxesMap = [];
			for (var i = 0; i < chart.series.length; ++i) {
				var line = chart.series[i];
				// var yLabel = line._unit && line._unit.value ? chart.yLabel
				// + " / " + line._unit.value : chart.yLabel;
				var yLabel = line._unit && line._unit.value ? line._unit.value
						: chart.yLabel;
				if (!chart.yaxesMap[yLabel]) {
					chart.yaxes.push({
						position : chart.yaxes.length & 1 ? "right" : "left",
						axisLabel : yLabel
					});
					line.yaxis = chart.yaxesMap[yLabel] = chart.yaxes.length;
				} else {
					line.yaxis = chart.yaxesMap[yLabel];
				}
			}
		}
		var options = {
			yaxes : chart.yaxes,
			yaxis : {
				axisLabel : chart.yLabel,
				minTickSize : 0.01,
				tickFormatter : yTickFormatter
			},
			legend : {
				position : "nw",
				margin : [ 0, 0 ],
				noColumns : 5,
				container : $legend,
				labelFormatter : labelFormatter
			},
			selection : {
				mode : "xy"
			},
			series : {
				lines : {
					show : true
				},
				points : {
					show : true,
					radius : 1,
					lineWidth : 0,
					fill : 1,
					fillColor : false
				}
			},
			shadowSize : 0,
			crosshair : {
				mode : "xy"
			},
			grid : {
				hoverable : true,
				clickable : true
			}
		/*
		 * , pan: { interactive: true }, zoom: { interactive: true }
		 */
		};
		var rawSeries = chart._rawSeries ? chart._rawSeries : chart.series;
		if (optionsHook)
			options = optionsHook(options);
		switch (chart.xaxisMode) {
		case "TIME":
			options.xaxis = {
				mode : "time"
			};
			break;
		case "CATEGORIES":
			var map = [];
			for (var i = 0; i < chart.series.length; ++i) {
				var seriesData = chart.series[i].data[0];
				seriesData[0] = i;
				map.push(chart.series[i].label);
			}
			options.xaxis = {
				minTickSize : 1,
				tickSize : 1,
				tickLength : 0,
				tickFormatter : function(num, _) {
					var newTick = map[num];
					if (!newTick)
						return "";
					return "<div class='category_tick'>" + newTick + "</div>";
				}
			}
			options.series.bars = {
				show : true,
				align : "center",
				barWidth : 0.8,
				horizontal : false,
				lineWidth : 0,
				fill : 0.75,
				fillColor : false
			}
			options.series.points.show = false;
			break;
		case "BAR_STRING":
			var barWidth = 1.0 / (rawSeries.length + 1);
			regenerateShowSeriesForBarChart(chart, options);
			options.series.lines.show = false;
			options.series.points.show = false;
			options.xaxis = {
				min : options.xaxis.min,
				max : options.xaxis.max,
				minTickSize : 1,
				tickSize : 1,
				tickLength : 0,
				tickFormatter : function(num, _) {
					var newTick = chart.stringMap[chart._groupId2RawXMap[num]];
					if (!newTick)
						return "";
					return "<div class='category_tick'>" + newTick + "</div>";
				},
				labelWidth : 100
			}
			options.series.bars = {
				show : true,
				align : "left",
				barWidth : chart._barWidth,
				horizontal : false,
				lineWidth : 0,
				fill : 0.8,
				fillColor : false
			}
			break;
		case "INTEGER":
			options.xaxis = {
				minTickSize : 1,
				tickSize : 1
			}
			options.xaxis.tickFormatter = function(num, _) {
            	var rawTick = Math.round(num);
            	if (!chart.xaxisTicks)
            		return rawTick;
            	if (!chart.stringMap) {
            		chart.stringMap = {};
            		for (var i = 0; i < chart.xaxisTicks.length; ++i) {
            			chart.stringMap[chart.xaxisTicks[i][0]] = chart.xaxisTicks[i][1];
            		}
            	}
            	var newTick = chart.stringMap[rawTick];
                return newTick ? "<div class='category_tick'>" + newTick + "</div>" : "";
            };
			break;
		default:
			break;
		}
		// plot
		var plot = $.plot($placeholder, chart.series, options);
		$placeholder.data("plot", plot);
		$placeholder.data("chart", chart);
		chart.plot = plot;
		return plot;
	}
	function redraw($placeholder, plot, chart, ignoreGridUpdate) {
    		if (chart)
    			plot.setData(chart.series);
    		if (!ignoreGridUpdate)
    			plot.setupGrid();
    		plot.draw();
    		// fix category tick
    		// if ($placeholder.data("chart").xaxisMode === "CATEGORIES"
    		// || chart.xaxisMode === "BAR_STRING") {
    		// fixCategoryTick($placeholder);
    		// }
    	}
	function regenerateShowSeriesForBarChart(chart, options) {
    		var rawSeries = chart._rawSeries ? chart._rawSeries : chart.series;
    		var map = chart.stringMap;
    		var isGroupShowMap;
    		var groupShowBIT;
    		if (!chart._groupShowBIT) {
    			groupShowBIT = chart._groupShowBIT = new BITree(chart.stringMapSize);
    			var counter = Math.min(10, chart.stringMapSize);
    			for (var stringID = 1; stringID <= 10; stringID++) {
    				groupShowBIT.update(stringID, 1);
    			}
    		} else {
    			groupShowBIT = chart._groupShowBIT;
    		}
    		// if (!chart._isGroupShowMap) {
    		// isGroupShowMap = chart._isGroupShowMap = [];
    		// var counter = 0;
    		// for (var stringID in map) {
    		// if (++counter > 10)
    		// break;
    		// isGroupShowMap[stringID] = true;
    		// }
    		// } else {
    		// isGroupShowMap = chart._isGroupShowMap;
    		// }
    		var barWidth = 1.0 / (rawSeries.length + 1);
    		var groupWidth = 1.0 - barWidth;
    		var showSeries = [];
    		// var groupShowMap = {};
    		var rawX2GroupIdMap = {};
    		var groupId2RawXMap = {};
    		var groups = 0;
    		var nextGroupID = 0;
    		for (var i = 0; i < rawSeries.length; i++) {
    			var seriesItem = rawSeries[i];
    			if (!seriesItem._showBars)
    				continue;
    			var newSeriesItem = {
    				label : seriesItem.label,
    				_showBars : seriesItem._showBars,
    				_showPoints : seriesItem._showPoints,
    				_showLines : seriesItem._showLines,
    				color : i + 1,
    				data : []
    			};
    			// var showCount = Math.min(seriesItem.data.length, 10);
    			for (var j = 0; j < seriesItem.data.length; j++) {
    				var rawPoint = seriesItem.data[j];
    				var rawX = rawPoint[0];
    				// if (isGroupShowMap[rawX] !== true)
    				// continue;
    				if (!groupShowBIT.get(rawX))
    					continue;
    				var groupID;
    				if (rawX2GroupIdMap[rawX] === undefined) {
    					// groupID = rawX2GroupIdMap[rawX] = ++groups;
    					groupID = groupShowBIT.getSum(rawX);
    					groupId2RawXMap[groupID] = rawX;
    					rawX2GroupIdMap[rawX] = groupID;
    					++groups;
    				} else {
    					groupID = rawX2GroupIdMap[rawX];
    				}
    				var newX = groupID + barWidth * i;
    				newSeriesItem.data.push([ newX, rawPoint[1] ]);
    			}
    			showSeries.push(newSeriesItem);
    		}
    		chart._groupId2RawXMap = groupId2RawXMap;
    		chart._barWidth = barWidth;
    		chart._rawSeries = rawSeries;
    		chart.series = showSeries;
    		if (options) {
    			if (!options.xaxis)
    				options.xaxis = {};
    			options.xaxis.min = 1 - chart._barWidth;
    			options.xaxis.max = groups + 0.9;
    		}
    	}

	function prepareChart(chart) {
		if (!chart._prepared) {
			chart._prepared = true;
			// add index to each series
			for (var i = 0; i < chart.series.length; ++i) {
				var series = chart.series[i];
				series._index = i;
				series._chartIndex = chart._index;
				series._reportIndex = chart._reportIndex;
				series._showLines = series.lines && series.lines.show === false ? false
						: true;
				series._showBars = series.bars && series.bars.show === true ? true
						: false;
				series._showPoints = series.points
						&& series.points.show === true ? true : false;
				// series._show = true;
			}
		}
	}

Perfcharts.BarChartPainer = function() {
};

Perfcharts.BarChartPainer.prototype = new Perfcharts.Painter();

Perfcharts.BarChartPainer.prototype.paint = function($chart, chart) {
    prepareChart(chart);
    $chart.append($("<h3 class='chart_title'/>").text(chart.title)).append(
				$("<h4 class='chart_subtitle'/>").text(chart.subtitle));
    var $placeholder = $("<div class='placeholder'/>").appendTo($chart);
    $("<div class='x_label'/>").text(chart.xLabel).appendTo($chart);
    var $legend = $("<div class='legend'/>").appendTo($chart);
    var plot = draw($placeholder, $legend, chart);
    registerEvents($placeholder);
    // setupChartControls($("<div class='chart_controls_pad'/>").appendTo(
    // $chart), chart);
    var $group_controls = $("<div class='chart_controls_pad'/>").appendTo(
            $chart);

    var $showHildAllButtons = $("<div class='chart_bar_show_hide_all' />")
                .appendTo($group_controls);
        $("<input type='button' value='hide all' />").click(function() {
            $(".cb_bar_group_selector").prop("checked", false);
            chart.series = [];
            chart._groupShowBIT = new BITree(chart.stringMapSize);
            redraw(plot.getPlaceholder(), plot, chart, true);
        }).appendTo($showHildAllButtons);
        $("<input type='button' value='show all' />")
                .click(
                        function() {
                            $(".cb_bar_group_selector").prop("checked", true);
                            chart._groupShowBIT = new BITree(
                                    chart.stringMapSize);
                            for (var stringID = 1; stringID <= chart.stringMapSize; stringID++) {
                                chart._groupShowBIT.update(stringID, 1);
                            }
                            regenerateShowSeriesForBarChart(chart, chart.plot
                                    .getOptions());
                            var xaxisOption = plot.getXAxes()[0].options;
                            xaxisOption._zoomed = false;
                            xaxisOption.min = plot.getOptions().xaxis.min;
                            xaxisOption.max = plot.getOptions().xaxis.max;
                            redraw(plot.getPlaceholder(), plot, chart, false);
                        }).appendTo($showHildAllButtons);

    var $group_list = $(
            "<ul class='chart_group_list'/>")
            .appendTo($group_controls);

    for ( var stringID in chart.stringMap) {
        // use show/hide map int->boolean to indicate whether we should
        // display it
        var stringValue = chart.stringMap[stringID];
        var $group_list_item = $(
                "<li class='chart_bar_group_item'/>")
                .appendTo($group_list);
        var $item_content = $("<label/>").appendTo($group_list_item);
        var $cb = $(
                "<input type='checkbox' class='cb_bar_group_selector' />")
                .data("tag", {
                    sid : stringID,
                    chart : chart
                }).prop("checked", chart._groupShowBIT.get(stringID))
                .change(on_cb_group_list_item_changed).appendTo(
                        $item_content);
        $("<span/>").text(stringValue).appendTo($item_content);
    }
};

Perfcharts.FlotChartPainter = function() {
};
Perfcharts.FlotChartPainter.prototype = new Perfcharts.Painter();

	function setupChartControls($controlsPad, chart) {
		// $controlsPad
		$("<input type=\"button\" value=\"show all\"/>").click(function() {
			showOrHideAllSeries(chart, false);
		}).appendTo($controlsPad);
		$("<input type=\"button\" value=\"hide all\"/>").click(function() {
			showOrHideAllSeries(chart, true);
		}).appendTo($controlsPad);
	}
Perfcharts.FlotChartPainter.prototype.paint = function($chart, chart) {
	prepareChart(chart);
    $chart.append($("<h3 class='chart_title'/>").text(chart.title)).append(
            $("<h4 class='chart_subtitle'/>").text(chart.subtitle));
    var $placeholder = $("<div class='placeholder'/>").appendTo($chart);
    $("<div class='x_label'/>").text(chart.xLabel).appendTo($chart);
    var $legend = $("<div class='legend'/>").appendTo($chart);
    var plot = draw($placeholder, $legend, chart);
    registerEvents($placeholder);
    setupChartControls($("<div class='chart_controls_pad'/>").appendTo(
            $chart), chart);
};


Perfcharts.CompositeChartManager = function () {

};

Perfcharts.CompositeChartManager.prototype = {};

Perfcharts.CompositeChartManager.prototype.setupControlPadForCompositeChart = function ($container, compositeChart, sourceReports) {
    var $control_pad = $container.children(".chart_controls_pad");
    var $placeholder = $container.children(".placeholder");

    function on_btn_clearSelection_click() {
        var $this = $(this);
        var $placeholder = $this.data("$placeholder");
        compositeChart.series = [];
        compositeChart.yaxes = [];
        compositeChart.yaxesMap = {};
        compositeChart.xaxisTicks = null;
        $control_pad.nextAll(".cb_control_pad").prop("checked", false);
        redraw($placeholder, $placeholder.data("plot"), compositeChart);
    }

	function on_cb_control_pad_change() {
        var $this = $(this);
        var tag = $this.data("tag");
        var series = compositeChart.series;
        var yaxesMap = compositeChart.yaxesMap;
        var yaxes = compositeChart.yaxes;
        var line = tag.line;
        var yLabel = line._unit && line._unit.value ? tag.chart.yLabel + " ("
                + line._unit.value + ")" : tag.chart.yLabel;
        var useWhichYaxis = yaxesMap[yLabel]; // starts from 1 if exists
        if ($this.is(":checked")) {
            if (!useWhichYaxis) {
                yaxes.push({
                    position : yaxes.length & 1 ? "right" : "left",
                    axisLabel : yLabel,
                    _used : 0
                });
                useWhichYaxis = yaxesMap[yLabel] = yaxes.length;
            }
            ++yaxes[useWhichYaxis - 1]._used;
            series.push({
                label : tag.chart.subtitle ? tag.chart.subtitle + "-"
                        + line.label : line.label,
                data : line.data,
                yaxis : useWhichYaxis,
                color : tag.index
            });
            compositeChart.xaxisMode = tag.chart.xaxisMode;
            if (compositeChart.xaxisMode === "INTEGER") {
                compositeChart.xaxisTicks = tag.chart.xaxisTicks;
            }
        } else {
            for (var i = 0; i < series.length; ++i) {
                if (series[i].data === line.data) {
                    series.splice(i, 1);
                    break;
                }
            }
            if (--yaxes[useWhichYaxis - 1]._used <= 0) {
                for (var i = useWhichYaxis; i < yaxes.length; ++i) {
                    yaxes[i].position = yaxes[i].position == "right" ? "left"
                            : "right";
                    --yaxesMap[yaxes[i].axisLabel]
                    yaxes[i - 1] = yaxes[i];
                }
                yaxesMap[yLabel] = 0;
                yaxes.pop();
                for (var i = 0; i < series.length; ++i) {
                    if (series[i].yaxis > useWhichYaxis)
                        --series[i].yaxis;
                }
            }
        }
        draw(tag.$placeholder, tag.$legend, compositeChart, function(options) {
            options.yaxesMap = yaxesMap;
            options.yaxes = yaxes;
            return options;
        });
    }

		var reports = sourceReports;
		var $topDiv = $("<div/>").appendTo($control_pad);
		$("<button>Clear Selection</button>")
				.data("$placeholder", $placeholder).click(
						on_btn_clearSelection_click).appendTo($topDiv);
		$("<input type='text' placeholder='caption'/>").change(
				function() {
					$container.children(".chart_title")
					    .text(this.value ? this.value : "");
				}).appendTo($topDiv);
		$("<input type='text' placeholder='subtitle' />")
				.change(
						function() {
							$container.children(".chart_subtitle")
									.text(this.value ? this.value : "");
						}).appendTo($topDiv);

		var $rootList = $("<dl/>").appendTo($control_pad);
		var indexOfSeries = 0;
		for (var i = 0; i < reports.length; ++i) {
			var report = reports[i];
			$("<dt/>").text(report.title).appendTo($rootList);
			var charts = report.charts;
			for (var j = 0; j < charts.length; ++j) {
				var chart = charts[j];
				if (chart.xaxisMode === "CATEGORIES" || chart.xaxisMode === "BAR_STRING")
					continue;
				if (chart.xaxisMode === "INTEGER")
					compositeChart.xaxisMode = null;
				var $chartList = $("<dl/>");
				$("<dd/>").append($chartList).appendTo($rootList);
				$("<dt/>").text(chart.title).appendTo($chartList);
				var series = chart.series;
				if (!series)
					continue;
				for (var k = 0; k < series.length; ++k) {
					var line = series[k];
					var $checkbox = $(
							"<input type='checkbox' class='cb_control_pad'/>")
							.data("tag", {
								report : report,
								chart : chart,
								line : line,
								$placeholder : $placeholder,
								$legend : $placeholder.nextAll(".legend"),
								index : indexOfSeries++
							}).change(on_cb_control_pad_change);
					$("<dd/>").append(
							$("<label/>").text(line.label).prepend($checkbox))
							.appendTo($chartList);
				}
			}

		}
	}

Perfcharts.CompositeChartManager.prototype.setup = function($container, sourceReports, options) {

    // clear old content
    $container.html("");
    var compositeReport = { title: "", _index : -1 };
    if (!compositeReport.charts) {
        compositeReport.charts = [ {
            title : "Your Awesome Chart",
            subtitle : "",
            xaxisMode : "TIME"
        } ];
    }
    var compositeChart = compositeReport.charts[0];
    compositeChart.series = [];
    compositeChart.yaxes = [];
    compositeChart.yaxesMap = {};
    compositeChart.xaxisTicks = null;
    //drawReport($(".report"), report);

    prepareChart(compositeChart);
    $container.append($("<h3 class='chart_title'/>").text(compositeChart.title)).append(
            $("<h4 class='chart_subtitle'/>").text(compositeReport.subtitle));
    var $placeholder = $("<div class='placeholder'/>").appendTo($container);
    $("<div class='x_label'/>").text(compositeChart.xLabel).appendTo($container);
    var $legend = $("<div class='legend'/>").appendTo($container);
    var plot = draw($placeholder, $legend, compositeChart);
    registerEvents($placeholder);
    setupChartControls($("<div class='chart_controls_pad'/>").appendTo(
            $container), compositeReport);
    $(".chart").addClass("composite_chart");
    this.setupControlPadForCompositeChart($container, compositeChart, sourceReports);


}
