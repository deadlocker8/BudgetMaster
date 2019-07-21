Mousetrap.bind('n', function()
{
    if(!isSearchFocused())
    {
        window.location.href = rootURL + '/transactions/newTransaction/normal';
    }
});

Mousetrap.bind('r', function()
{
    if(!isSearchFocused())
    {
        window.location.href = rootURL + '/transactions/newTransaction/repeating';
    }
});

Mousetrap.bind('t', function()
{
    if(!isSearchFocused())
    {
        window.location.href = rootURL + '/transactions/newTransaction/transfer';
    }
});

Mousetrap.bind('f', function()
{
    if(!isSearchFocused())
    {
        window.location.href = rootURL + '/transactions#modalFilter';
    }
});

Mousetrap.bind('s', function(e)
{
    if(!isSearchFocused())
    {
        document.getElementById('search').focus();
        e.preventDefault();
    }
});

Mousetrap.bind('esc', function()
{
    if(isSearchFocused())
    {
        document.getElementById('nav-logo-container').focus();
    }
});

function isSearchFocused()
{
    var searchElement = document.getElementById('search');
    return document.activeElement === searchElement;
}
