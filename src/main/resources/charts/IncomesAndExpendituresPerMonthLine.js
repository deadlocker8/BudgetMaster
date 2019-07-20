/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.
moment.locale(localizedLocale);

var dates = [];
var incomes = [];
var expenditures = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    // create new sums if month is not already in list
    var date = moment(transaction.date).startOf('month').format('MMMM YY');
    if(!dates.includes(date))
    {
        dates.push(date);
        incomes.push(0);
        expenditures.push(0);
    }

    // add to income or expenditure sum
    var amount = transaction.amount;
    if(amount > 0)
    {
        var lastIndex = incomes.length - 1;
        incomes[lastIndex] = incomes[lastIndex] + amount;
    } else
    {
        var lastIndex = expenditures.length - 1;
        expenditures[lastIndex] = expenditures[lastIndex] + Math.abs(amount);
    }
}

// convert all income sums to decimal
incomes.forEach(function(value, index)
{
    this[index] = value / 100;
}, incomes);

// convert all expenditure sums to decimal
expenditures.forEach(function(value, index)
{
    this[index] = value / 100;
}, expenditures);

// Prepare your chart settings here (mandatory)
var plotlyData = [
    {
        x: dates,
        y: incomes,
        type: 'line',
        name: localizedData['traceName1']
    },
    {
        x: dates,
        y: expenditures,
        type: 'line',
        name: localizedData['traceName2']
    }
];

// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: localizedTitle
    },
    yaxis: {
        title: localizedData['axisY'] + localizedCurrency,
        rangemode: 'tozero',
        tickformat: '.2f',
        showline: true
    },
    xaxis: {
        tickformat: '%d.%m.%y'
    }
};

// Add your Plotly configuration settings here (optional)
var plotlyConfig = {
    showSendToCloud: false,
    displaylogo: false,
    showLink: false,
    responsive: true
};

// Don't touch this line
Plotly.newPlot('chart-canvas', plotlyData, plotlyLayout, plotlyConfig);