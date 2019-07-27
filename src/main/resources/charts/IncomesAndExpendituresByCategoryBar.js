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
var incomes = [];
var expenditures = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var categoryName = transaction.category.name;
    // create new category if not already in dict
    if(!categoryNames.includes(categoryName))
    {
        categoryNames.push(categoryName);
        colors.push(transaction.category.color);
        incomes.push(0);
        expenditures.push(0);
    }

    // determine index of categoryName in list because the transactions are not ordered by category and some categories
    // will be missing either for income or expenditures
    var index = categoryNames.indexOf(categoryName);

    // add to income or expenditure sum
    var amount = transaction.amount;
    if(amount > 0)
    {
        incomes[index] = incomes[index] + amount;
    }
    else
    {
        expenditures[index] = expenditures[index] + Math.abs(amount);
    }
}

// calculate total sums
var totalIncomes = 0;
var totalExpenditures = 0;

incomes.forEach(function(value)
{
    totalIncomes += value;
});

expenditures.forEach(function(value)
{
    totalExpenditures += value;
});


// Prepare your chart settings here (mandatory)
var plotlyData = [];

for(var j = 0; j < categoryNames.length; j++)
{
    var currentName = categoryNames[j];

    var percentageIncome = (100 / totalIncomes) * incomes[j];
    var percentageExpenditure = (100 / totalExpenditures) * expenditures[j];

    var textIncome = prepareHoverText(currentName, percentageIncome, incomes[j]);
    var textExpenditure = prepareHoverText(currentName, percentageExpenditure, expenditures[j]);

    // add border if category color is white
    var borderWidth = 0;
    if(colors[j] === '#FFFFFF')
    {
        borderWidth = 1;
    }

    plotlyData.push({
        x: [percentageExpenditure, percentageIncome],
        y: [localizedData['label1'], localizedData['label2']],
        orientation: 'h',
        type: 'bar',
        hoverinfo: 'text',
        text: [textExpenditure, textIncome],
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
        showgrid: false,
        showticklabels: false
    },
    yaxis: {
        tickangle: 90,
        tickfont: {
            family: 'sans-serif',
            size: 14,
            color: 'black'
        },
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
Plotly.newPlot('chart-canvas', plotlyData, plotlyLayout, plotlyConfig);


function prepareHoverText(categoryName, percentage, value)
{
    value = value / 100;
    return categoryName + ' ' + percentage.toFixed(1) + '% (' + value.toFixed(1) + ' ' + localizedCurrency + ')';
}