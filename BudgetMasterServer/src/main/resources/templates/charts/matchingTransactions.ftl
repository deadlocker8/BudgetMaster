<#import "/spring.ftl" as s>
<#import "../helpers/header.ftl" as header>
<#import "../search/searchMacros.ftl" as searchMacros>

<@header.globals/>

<div id="matchingTransactionsTitle" class="section center-align">
    <div class="headline-small">${matchingTransactionsTitle}</div>
</div>
<@searchMacros.renderTransactions transactions=matchingTransactions openLinksInNewTab=true/>
