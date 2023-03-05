/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.

transactionData = transactionData.reverse();

moment.locale('de');

const NAME = 0;
const COLOR = 1;
const INCOME = 2;
const EXPENDITURE = 3;


var categories = new Map();

for(var i = 0; i < transactionData.length; i++)
{
    var currentTransaction = transactionData[i];
    if(!categories.has(currentTransaction.category.name))
    {
        categories.set(currentTransaction.category.name, currentTransaction.category.color);
    }
}

var sortedCategories = new Map([...categories.entries()].sort());

var dates = [];
var values = [];

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var date = moment(transaction.date).startOf('month').format('MMM YY');
    if(!dates.includes(date))
    {
        dates.push(date);
        values.push([
            Array.from(sortedCategories.keys()), // NAME
            Array.from(sortedCategories.values()), // COLOR
            new Array(sortedCategories.size).fill(0), // INCOME
            new Array(sortedCategories.size).fill(0)  // EXPENDITURE
        ]);
    }

    // determine index of category name in list
    var lastIndex = values.length - 1;

    var categoryName = transaction.category.name;
    // create new category if not already in dict
    if(!values[lastIndex][NAME].includes(categoryName))
    {
        values[lastIndex][NAME].push(categoryName);
        values[lastIndex][COLOR].push(transaction.category.color);
        values[lastIndex][INCOME].push(0);
        values[lastIndex][EXPENDITURE].push(0);
    }

    // determine index of category in current last values
    var index = values[lastIndex][NAME].indexOf(categoryName);

    // add to income or expenditure sum
    var amount = transaction.amount;
    if(amount > 0)
    {
        values[lastIndex][INCOME][index] = values[lastIndex][INCOME][index] + amount;
    }
    else
    {
        values[lastIndex][EXPENDITURE][index] = values[lastIndex][EXPENDITURE][index] + Math.abs(amount);
    }
}

var totalIncomeSums = [];
var totalExpenditureSums = [];

// calculate total sums for all months
for(var i = 0; i < dates.length; i++)
{
    var totalIncomes = 0;
    var totalExpenditures = 0;

    values[i][INCOME].forEach(function(value)
    {
        totalIncomes += value;
    });

    values[i][EXPENDITURE].forEach(function(value)
    {
        totalExpenditures += value;
    });

    totalIncomeSums.push(totalIncomes);
    totalExpenditureSums.push(totalExpenditures);
}

// Prepare your chart settings here (mandatory)
var plotlyData = [];
var plotlyLayout = {
    title: {
        text: formatChartTitle(localizedTitle, localizedDateRange),
    },
    barmode: "stack",
    hovermode: 'closest', // show hover popup only for hovered item
    yaxis: {
        rangemode: 'tozero',
        tickformat: '.0f',
        ticksuffix: localizedCurrency,
        showline: true
    }
};

// create one stacked bar for incomes and one for expenditures for every month and group them by month
for(var i = 0; i < dates.length; i++)
{
    for(var j = 0; j < values[i][NAME].length; j++)
    {
        var currentValues = values[i];
        var currentName = currentValues[NAME][j];

        var currentIncomeValue = currentValues[INCOME][j];
        var percentageIncome = (100 / totalIncomeSums[i]) * currentIncomeValue;
        var textIncome = prepareHoverText(currentName, percentageIncome, currentIncomeValue);

        var currentExpenditureValue = currentValues[EXPENDITURE][j];
        var percentageExpenditure = (100 / totalExpenditureSums[i]) * currentExpenditureValue;
        var textExpenditure = prepareHoverText(currentName, percentageExpenditure, currentExpenditureValue);

        // add border if category color is white
        var borderWidth = 0;
        if(currentValues[COLOR][j].toUpperCase().startsWith('#FFFFFF'))
        {
            borderWidth = 1;
        }

        plotlyData.push({
            x: [localizedData['label2'], localizedData['label1']],
            y: [currentIncomeValue / 100.0, currentExpenditureValue / 100.0],
            type: 'bar',
            hoverinfo: 'text',
            hovertext: [textIncome, textExpenditure],
            name: currentName,
            xaxis: 'x' + (i + 1),  // for grouping incomes and expenditure bar by month
            barmode: 'stack',
            showlegend: i === 0,
            legendgroup: currentName,
            marker: {
                color: currentValues[COLOR][j],  // use the category's color
                line: {
                    color: '#212121',
                    width: borderWidth
                }
            }
        });
    }

    // axis number inside layout uses a different counting in comparison to xaxis definition in plotlyDate
    var axisNumber = i + 1;
    if(i === 0)
    {
        axisNumber = '';
    }

    // calculate subplot start and end position (relative between 0 and 1)
    var width = 1 / dates.length;
    var start = i * width;
    var end = (i + 1) * width;

    plotlyLayout['xaxis' + axisNumber] = {
        domain: [start, end],
        anchor: 'x' + axisNumber,
        title: dates[i],
    }
}

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


REGEX_CATGEORY_NAME = new RegExp("(.*)\\s\\d+.\\d%");

var plotContainer = document.getElementById('containerID');
plotContainer.on('plotly_click', function(data)
{
    if(data.event.shiftKey !== true)
    {
        return;
    }

    let index = data.points.length - 1;
    let hoverText = data.points[index].hovertext;
    let match = hoverText.match(REGEX_CATGEORY_NAME);
    if(match === null)
    {
        console.error('could not extract category name from: "' + hoverText + '"');
        return;
    }

    getAndShowMatchingTransactions(null, match[1]);
});

function prepareHoverText(categoryName, percentage, value)
{
    value = value / 100;
    return categoryName + ' ' + percentage.toFixed(1) + '% (' + value.toFixed(1) + ' ' + localizedCurrency + ')';
}