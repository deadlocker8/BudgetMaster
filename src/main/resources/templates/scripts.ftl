<#macro scripts>
<#import "/spring.ftl" as s>
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="<@s.url '/materialize-0.100.2/js/materialize.min.js'/>"></script>
<script>
    rootURL = "<@s.url ''/>"
</script>
<script src="<@s.url '/js/main.js'/>"></script>
<script>
    accountPlaceholderName = "${locale.getString("account.all")}";
</script>
</#macro>