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
        <legend>Введите данные пользователя</legend>
        <form name="signInForm" action="/signin" method="POST">
            <@common.formInput id="name" name="name" label="Имя"/> <br/>
            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>