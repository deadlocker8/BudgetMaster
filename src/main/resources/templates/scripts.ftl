<#macro scripts>
<#import "/spring.ftl" as s>
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="<@s.url '/materialize-v1.0.0/js/materialize.min.js'/>"></script>
<script>
    rootURL = "<@s.url ''/>"
</script>
<script src="<@s.url '/js/main.js'/>"></script>
<script>
    accountPlaceholderName = "${locale.getString("account.all")}";
</script>
</#macro>