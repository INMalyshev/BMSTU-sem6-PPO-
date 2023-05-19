<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>
  <@common.headMacros />
  <body>

    <@common.logoMacros />

    <#if withError >
        <@common.showError legend=legend message=message />
    </#if>
    
    <fieldset>
        <legend>Введи id пользователя</legend>
        <form name="enterUserIdForm" action="/login" method="POST">
            <@common.formInput id="userId" name="userId" label="id пользователя"/> <br/>
            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>