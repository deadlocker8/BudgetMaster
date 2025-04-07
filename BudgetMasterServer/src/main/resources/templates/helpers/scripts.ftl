<#macro scripts>
    <#import "/spring.ftl" as s>
    <script src="<@s.url '/webjars/jquery/jquery.min.js'/>"></script>
    <script src="<@s.url '/webjars/materializecss/js/materialize.min.js'/>"></script>
    <script src="<@s.url '/webjars/mousetrap/mousetrap.js'/>"></script>
    <script>
        rootURL = "<@s.url ''/>"
    </script>
    <script src="<@s.url '/js/hotkeys.js'/>"></script>
    <script src="<@s.url '/js/main.js'/>"></script>
    <script src="<@s.url '/js/customSelect.js'/>"></script>
    <script src="<@s.url '/js/fetchModalContent.js'/>"></script>

    <script>
        accountPlaceholderName = "${locale.getString("account.all")}";
    </script>
</#macro>