/* This list will be dynamically filled with all the transactions between
 * the start and and date you select on the "Show Chart" page
 * and filtered according to your specified filter.
 * An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
 * https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
 */
var transactionData = [];

// Note: All variables starting with "localized" are only available inside default charts.

// objects for easier handling of categories with corresponding sums
class CategorySum
{
    constructor(categoryName, color)
    {
        this.categoryName = categoryName;
        this.color = color;
        this.amount = 0;
    }

    add(amount)
    {
        this.amount += amount;
    }
}
CategorySum.prototype.toString = function(){return this.categoryName + " , " + this.amount;};

class CategorySumHandler
{
    constructor()
    {
        this.sums = [];
    }

    getByCategoryName(categoryName)
    {
        return this.sums.find(obj => {
            return obj.categoryName === categoryName
        });
    }

    add(categorySum)
    {
        this.sums.push(categorySum);
    }

    getTotalSum()
    {
        var total = 0;
        this.sums.forEach(function(sum)
        {
            total += sum.amount;
        });
        return total;
    }

    static sortNumber(a, b)
    {
        return a.amount - b.amount;
    }

    getSumsSorted()
    {
        var sumsSorted = this.sums.filter(function(item){
            return item.amount !== 0;
        });

        return sumsSorted.sort(CategorySumHandler.sortNumber);
    }
}

var incomeHandler = new CategorySumHandler();
var expenditureHandler = new CategorySumHandler();

for(var i = 0; i < transactionData.length; i++)
{
    var transaction = transactionData[i];

    var categoryName = transaction.category.name;
    // create new category if not already in dict
    if(incomeHandler.getByCategoryName(categoryName) === undefined)
    {
        incomeHandler.add(new CategorySum(categoryName, transaction.category.color));
    }

    if(expenditureHandler.getByCategoryName(categoryName) === undefined)
    {
        expenditureHandler.add(new CategorySum(categoryName, transaction.category.color));
    }

    var amount = transaction.amount;
    if(amount > 0)
    {
        incomeHandler.getByCategoryName(categoryName).add(amount);
    }
    else
    {
        expenditureHandler.getByCategoryName(categoryName).add(Math.abs(amount));
    }
}

// Prepare your chart settings here (mandatory)
var plotlyData = addSeries(incomeHandler, 0);
plotlyData = plotlyData.concat(addSeries(expenditureHandler, 1));

// Add your Plotly layout settings here (optional)
var plotlyLayout = {
    title: {
        text: localizedTitle
    },
    hovermode: 'closest', // show hover popup only for hovered item
    grid: {rows: 1, columns: 2},
    annotations: [
        {
            font: {
                size: 20
            },
            xref: "paper",
            yref: "paper",
            xanchor: "center",
            yanchor: "top",
            showarrow: false,
            text: localizedData['label2'],
            x: 0.25,
            y: 0
        },
        {
            font: {
                size: 20
            },
            xref: "paper",
            yref: "paper",
            xanchor: "center",
            yanchor: "top",
            showarrow: false,
            text: localizedData['label1'],
            x: 0.75,
            y: 0
        }
    ],
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

function addSeries(sumHandler, column)
{
    var plotlyData = [];

    var total = sumHandler.getTotalSum();
    var sums = sumHandler.getSumsSorted();

    var values = sums.map(item => item.amount);
    var colors = sums.map(item => item.color);
    var labels = sums.map(item => item.categoryName);
    var hoverTexts = sums.map((item, index) => `
${labels[index]}<br>
${(values[index]/100).toFixed(1) + ' ' + localizedCurrency}<br>
${(values[index] / total * 100).toFixed(1)}%
`);

    var borders = sums.map(item => {
        if(item.color === '#FFFFFF')
        {
            return 1;
        }
        return 0;
    });

    plotlyData.push({
        values: values,
        labels: labels,
        type: 'pie',
        text: hoverTexts,
        hoverinfo: 'text',
        textinfo: 'none',
        marker: {
            colors: colors,
            line: {
                color: '#212121',
                width: borders
            }
        },
        domain: {
            row: 0,
            column: column,
        },
    });

    return plotlyData;
}