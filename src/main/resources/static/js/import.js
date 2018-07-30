$( document ).ready(function() {
    // prevent form submit on enter
    $(document).on("keypress", 'form', function (e) {
        var code = e.keyCode || e.which;
        if (code === 13) {
            e.preventDefault();
            return false;
        }
    });
});

function validateForm()
{
    // handle account matches
    var accountSources = $('.account-source');
    var accountDestinations = $('select.account-destination');
    var parent = document.getElementById("hidden-account-matches");

    for(var i = 0; i < accountSources.length; i++)
    {
        var input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("name", "accountMatches[" + i + "].accountSource.name");
        input.setAttribute("value", accountSources[i].innerText);
        parent.appendChild(input);

        var inputDestination = document.createElement("input");
        inputDestination.setAttribute("type", "hidden");
        inputDestination.setAttribute("name", "accountMatches[" + i + "].accountDestination.name");
        inputDestination.setAttribute("value", accountDestinations[i].value);
        parent.appendChild(inputDestination);
    }

    return true;
}