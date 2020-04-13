Mousetrap.bind('n', function()
{
    if(areHotKeysEnabled())
    {
        window.location.href = rootURL + '/transactions/newTransaction/normal';
    }
});

Mousetrap.bind('r', function()
{
    if(areHotKeysEnabled())
    {
        window.location.href = rootURL + '/transactions/newTransaction/repeating';
    }
});

Mousetrap.bind('t', function()
{
    if(areHotKeysEnabled())
    {
        window.location.href = rootURL + '/transactions/newTransaction/transfer';
    }
});

Mousetrap.bind('v', function()
{
    if(areHotKeysEnabled())
    {
        window.location.href = rootURL + '/templates/select';
    }
});

Mousetrap.bind('f', function()
{
    if(areHotKeysEnabled())
    {
        window.location.href = rootURL + '/transactions#modalFilter';
    }
});

Mousetrap.bind('s', function(e)
{
    if(areHotKeysEnabled())
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


function areHotKeysEnabled()
{
    return !isSearchFocused() && !isCategorySelectFocused();
}


function isSearchFocused()
{
    let searchElement = document.getElementById('search');
    return document.activeElement === searchElement;
}

function isCategorySelectFocused()
{
    let activeElement = document.activeElement;
    return activeElement.id.includes('select-options');
}
