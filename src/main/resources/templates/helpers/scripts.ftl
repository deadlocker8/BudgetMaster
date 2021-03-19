<#macro scripts>
    <#import "/spring.ftl" as s>
    <script src="<@s.url '/webjars/jquery/3.5.1/jquery.min.js'/>"></script>
    <script src="<@s.url '/webjars/materializecss/1.0.0/js/materialize.min.js'/>"></script>
    <script src="<@s.url '/webjars/mousetrap/1.6.5/mousetrap.js'/>"></script>
    <script>
        rootURL = "<@s.url ''/>"
    </script>
    <script src="<@s.url '/js/hotkeys.js'/>"></script>
    <script src="<@s.url '/js/main.js'/>"></script>
    <script src="<@s.url '/js/customSelect.js'/>"></script>
    <script>
        accountPlaceholderName = "${locale.getString("account.all")}";
    </script>
</#macro>