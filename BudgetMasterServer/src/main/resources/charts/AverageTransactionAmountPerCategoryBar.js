/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.

var categoryNames = [];
var colors = [];
var incomesPerCategory = [];
var expendituresPerCategory = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var categoryName = transaction.category.name;
    // create new category if not already in dict
    if(!categoryNames.includes(categoryName))
    {
        categoryNames.push(categoryName);
        colors.push(transaction.category.color);
        incomesPerCategory.push([]);
        expendituresPerCategory.push([]);
    }

    // determine index of categoryName in list because the transactions are not ordered by category and some categories
    // will be missing either for income or expenditures
    var index = categoryNames.indexOf(categoryName);

    // add to income or expenditure sum
    var amount = transaction.amount;
    if(amount > 0)
    {
        incomesPerCategory[index].push(amount);
        expendituresPerCategory[index].push(0);
    }
    else
    {
        incomesPerCategory[index].push(0);
        expendituresPerCategory[index].push(amount);
    }
}


// Prepare your chart settings here (mandatory)
var plotlyData = [];

for(var j = 0; j < categoryNames.length; j++)
{
    var currentName = categoryNames[j];

    var incomeAverage = calculateAverage(incomesPerCategory[j]);
    var expenditureAverage = calculateAverage(expendituresPerCategory[j]);

    addDPlotlyData(plotlyData, incomeAverage, currentName, colors[j]);
    addDPlotlyData(plotlyData, expenditureAverage, currentName, colors[j], false);
}


// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: formatChartTitle(localizedTitle, localizedDateRange),
    },
    xaxis: {
    },
    yaxis: {
        title: localizedData['label1'] + ' ' + localizedCurrency,
        rangemode: 'tozero',
        tickformat: '.2f',
        showline: true
    },
    barmode: 'relative',
    hovermode: 'closest' // show hover popup only for hovered item
};

// Add your Plotly configuration settings here (optional)
var plotlyConfig = {
    showSendToCloud: false,
    displaylogo: false,
    showLink: false,
    responsive: true,
    displayModeBar: true,
    toImageButtonOptions: {
        format: 'png',
        filename: 'BudgetMaster_chart_export',
        height: 1080,
        width: 1920,
    }
};

// Don't touch this line
Plotly.newPlot("containerID", plotlyData, plotlyLayout, plotlyConfig);


function calculateAverage(values)
{
    var sum = 0;
    values.forEach(function(value)
    {
        sum += value;
    });
    return (sum / values.length) / 100;
}

function addDPlotlyData(plotlyData, averageValue, categoryName, color, showLegend)
{
    // add border if category color is white
    var borderWidth = 0;
    if(color.toUpperCase().startsWith('#FFFFFF'))
    {
        borderWidth = 1;
    }

    plotlyData.push({
        y: [averageValue],
        x: [categoryName],
        orientation: 'v',
        type: 'bar',
        hoverinfo: 'text',
        hovertext: [prepareHoverText(categoryName, averageValue)],
        name: categoryName,
        legendgroup: categoryName,
        showlegend: showLegend,
        marker: {
            color: color,  // use the category's color
            line: {
                color: '#212121',
                width: borderWidth
            }
        }
    });
}

function prepareHoverText(categoryName, value)
{
    return categoryName + ' ' + value.toFixed(1) + ' ' + localizedCurrency;
}
