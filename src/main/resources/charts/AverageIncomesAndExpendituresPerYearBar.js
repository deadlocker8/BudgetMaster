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
var incomeSums = [];
var numberOfIncomes = [];
var numberOfExpenditures = [];
var expenditureSums = [];

transactionData = transactionData.reverse();

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    // create new sums if year is not already in list
    var date = moment(transaction.date).startOf('year').format('YYYY');
    if(!dates.includes(date))
    {
        dates.push(date);
        incomeSums.push(0);
        numberOfIncomes.push(0);
        expenditureSums.push(0);
        numberOfExpenditures.push(0);
    }

    // add to income or expenditure sum
    var amount = transaction.amount;
    if(amount > 0)
    {
        var lastIndex = incomeSums.length - 1;
        incomeSums[lastIndex] = incomeSums[lastIndex] + amount;
        numberOfIncomes[lastIndex] = numberOfIncomes[lastIndex] + 1;
    }
    else
    {
        var lastIndex = expenditureSums.length - 1;
        expenditureSums[lastIndex] = expenditureSums[lastIndex] + Math.abs(amount);
        numberOfExpenditures[lastIndex] = numberOfExpenditures[lastIndex] + 1;
    }
}

var incomeAverages = [];
var expenditureAverages = [];

for(let i = 0; i < dates.length; i++)
{
    var currentIncomeAverage = (incomeSums[i] / 100) / numberOfIncomes[i];
    incomeAverages.push(currentIncomeAverage);

    var currentExpenditureAverage = (expenditureSums[i] / 100) / numberOfExpenditures[i];
    expenditureAverages.push(currentExpenditureAverage);
}

// Prepare your chart settings here (mandatory)
var plotlyData = [
    {
        x: dates,
        y: incomeAverages,
        type: 'bar',
        name: localizedData['traceName1']
    },
    {
        x: dates,
        y: expenditureAverages,
        type: 'bar',
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