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
var values = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var categoryName = transaction.category.name;
    // create new category if not already in dict
    if(!categoryNames.includes(categoryName))
    {
        categoryNames.push(categoryName);
        colors.push(transaction.category.color);
        values.push([]);
    }

    // determine index of categoryName in list because the transactions are not ordered by category and some categories
    // will be missing either for income or expenditures
    var index = categoryNames.indexOf(categoryName);

    // add to income or expenditure sum
    var amount = transaction.amount;
    values[index].push(Math.abs(amount));
}


// Prepare your chart settings here (mandatory)
var plotlyData = [];

for(var j = 0; j < categoryNames.length; j++)
{
    var currentName = categoryNames[j];

    var sumOfValues = 0;
    values[j].forEach(function(value)
    {
        sumOfValues += value;
    });

    var averageValue = (sumOfValues / values[j].length) / 100;

    var hoverText = prepareHoverText(currentName, averageValue);

    // add border if category color is white
    var borderWidth = 0;
    if(colors[j] === '#FFFFFF')
    {
        borderWidth = 1;
    }

    plotlyData.push({
        y: [averageValue],
        x: [currentName],
        orientation: 'v',
        type: 'bar',
        hoverinfo: 'text',
        hovertext: [hoverText],
        name: currentName,
        marker: {
            color: colors[j],  // use the category's color
            line: {
                color: '#212121',
                width: borderWidth
            }
        }
    });
}


// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: localizedTitle
    },
    xaxis: {
    },
    yaxis: {
        title: localizedData['label1'] + ' ' + localizedCurrency,
        rangemode: 'tozero',
        tickformat: '.2f',
        showline: true
    },
    barmode: 'stack',
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


function prepareHoverText(categoryName, value)
{
    return categoryName + ' ' + value.toFixed(1) + ' ' + localizedCurrency;
}