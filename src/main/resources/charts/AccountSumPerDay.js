/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// group transactions by date
var groups = transactionData.reverse().reduce((groups, transaction) => {
    var date = transaction.date;
    if (!groups[date]) {
        groups[date] = [];
    }
    groups[date].push(transaction);
    return groups;
}, {});

var dates = Object.keys(groups);
var previousSum = 0;
var sums = [];

// calculate sum for each date
for (var key in groups) {
    if (groups.hasOwnProperty(key)) {
        var group = groups[key];

        // extract all amount values
        var amounts = group.map(transaction => transaction.amount)

        // sum up all amounts
        var currentSum = amounts.reduce((a, b) => a + b, 0);

        // add su of current date to previous sum
        currentSum = previousSum + currentSum;

        // save current sum for next loop cycle
        previousSum = currentSum;

        // add sum to array
        sums.push(currentSum / 100);
    }
}

// Prepare your chart settings here (mandatory)
var plotlyData = [
    {
        x: dates,
        y: sums,
        type: 'line'
    }
];

// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: 'Account sum per day',
    },
    yaxis: {
        title: 'Sum in €',
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