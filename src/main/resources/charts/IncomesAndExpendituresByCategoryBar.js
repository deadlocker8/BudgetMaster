/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.

var categoryNames = [];
var incomes = [];
var expenditures = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var categoryName = transaction.category.name;
    // create new category is not already in dict
    if(!categoryNames.includes(categoryName))
    {
        categoryNames.push(categoryName);
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
    } else
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
    var percentageIncome = (100 / totalIncomes) * incomes[j];
    var percentageExpenditure = (100 / totalExpenditures) * expenditures[j];

    var textIncome = prepareHoverText(percentageIncome, incomes[j]);
    var textExpenditure = prepareHoverText(percentageExpenditure, expenditures[j]);

    plotlyData.push({
        x: [percentageExpenditure, percentageIncome],
        y: [localizedData['label1'], localizedData['label2']],
        orientation: 'h',
        type: 'bar',
        hoverinfo: 'text',
        text: [textExpenditure, textIncome],
        name: categoryNames[j]
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
        }
    },
    barmode: 'stack',
    hovermode: 'closest'
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


function prepareHoverText(percentage, value)
{
    value = value / 100;
    return percentage.toFixed(1) + '% (' + value.toFixed(1) + ' ' + localizedCurrency + ')';
}