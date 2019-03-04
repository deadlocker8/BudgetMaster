<#macro scripts>
<#import "/spring.ftl" as s>
<script src="<@s.url '/webjars/jquery/3.3.1/jquery.min.js'/>"></script>
<script src="<@s.url '/webjars/materializecss/1.0.0/js/materialize.min.js'/>"></script>
<script>
    rootURL = "<@s.url ''/>"
</script>
<script src="<@s.url '/js/main.js'/>"></script>
<script>
    accountPlaceholderName = "${locale.getString("account.all")}";
</script>
</#macro>