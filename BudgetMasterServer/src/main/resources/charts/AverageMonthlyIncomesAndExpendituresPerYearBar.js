/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.
moment.locale(localizedLocale);

var years = [];
var transactionsPerYear = [];


transactionData = transactionData.reverse();

// group transactions by years
for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    // create new sums if year is not already in list
    var date = moment(transaction.date).startOf('year').format('YYYY');

    if(!years.includes(date))
    {
        years.push(date);
        transactionsPerYear.push([]);
    }

    var index = years.indexOf(date);

    transactionsPerYear[index].push(transaction);
}


var incomeAveragesPerYear = [];
var incomeAveragesHoverTexts = [];
var expenditureAveragesPerYear = [];
var expenditureAveragesHoverTexts = [];

for(let i = 0; i < years.length; i++)
{
    var year = years[i];
    var transactionsOfYear = transactionsPerYear[i];

    var incomeSum = 0;
    var expenditureSum = 0;

    for(let j = 0; j < transactionsOfYear.length; j++)
    {
        var currentTransaction = transactionsOfYear[j];
        var month = moment(currentTransaction.date).startOf('month').format('M') - 1;

        var amount = currentTransaction.amount;
        if(amount > 0)
        {
            incomeSum = incomeSum + amount;
        }
        else
        {
            expenditureSum = expenditureSum + Math.abs(amount);
        }
    }

    var currentIncomeAverage = (incomeSum / 100) / 12;
    incomeAveragesPerYear.push(currentIncomeAverage);
    incomeAveragesHoverTexts.push(prepareHoverText(currentIncomeAverage))

    var currentExpenditureAverage = (expenditureSum / 100) / 12;
    expenditureAveragesPerYear.push(currentExpenditureAverage);
    expenditureAveragesHoverTexts.push(prepareHoverText(currentExpenditureAverage))
}

// Prepare your chart settings here (mandatory)
var plotlyData = [
    {
        x: years,
        y: incomeAveragesPerYear,
        type: 'bar',
        name: localizedData['traceName1'],
        hoverinfo: 'text',
        hovertext: incomeAveragesHoverTexts,
    },
    {
        x: years,
        y: expenditureAveragesPerYear,
        type: 'bar',
        name: localizedData['traceName2'],
        hoverinfo: 'text',
        hovertext: expenditureAveragesHoverTexts,
    }
];

// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: formatChartTitle(localizedTitle, localizedDateRange),
    },
    yaxis: {
        title: localizedData['axisY'] + ' ' + localizedCurrency,
        rangemode: 'tozero',
        tickformat: '.2f',
        showline: true
    },
    xaxis: {

    }
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


function prepareHoverText(value)
{
    return value.toFixed(1) + ' ' + localizedCurrency;
}