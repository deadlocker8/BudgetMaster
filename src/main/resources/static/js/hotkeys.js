Mousetrap.bind('n', function () {
    window.location.href = rootURL + '/transactions/newTransaction/normal';
});

Mousetrap.bind('r', function () {
    window.location.href = rootURL + '/transactions/newTransaction/repeating';
});

Mousetrap.bind('t', function () {
    window.location.href = rootURL + '/transactions/newTransaction/transfer';
});

Mousetrap.bind('f', function () {
    window.location.href = rootURL + '/transactions#modalFilter';
});

Mousetrap.bind('s', function () {
    document.getElementById('search').focus();
});

Mousetrap.bind('esc', function () {
    var searchElement = document.getElementById('search');
    var isSearchFocused = (document.activeElement === searchElement);
    if (isSearchFocused) {
        document.getElementById('nav-logo-container').focus();
    }
});
