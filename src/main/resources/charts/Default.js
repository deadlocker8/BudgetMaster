/* This list will be dynamically filled with all the transactions between
* the start and and date you select on the "Show Chart" page
* and filtered according to your specified filter.
* An example entry for this list and tutorial about how to create custom charts ca be found in the BudgetMaster wiki:
* https://github.com/deadlocker8/BudgetMaster/wiki/How-to-create-custom-charts
*/
var transactionData = [];

// Prepare your chart settings here (mandatory)
var plotlyData = [{
    x: [],
    y: [],
    type: 'bar'
}];

// Add your Plotly layout settings here (optional)
var plotlyLayout = {};

// Add your Plotly configuration settings here (optional)
var plotlyConfig = {
    showSendToCloud: false,
    displaylogo: false,
    showLink: false,
    responsive: true
};

// Don't touch this line
Plotly.newPlot('chart-canvas', plotlyData, plotlyLayout, plotlyConfig);
