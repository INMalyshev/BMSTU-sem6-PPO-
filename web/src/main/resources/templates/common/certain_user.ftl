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
        <legend>Данные о пользователе ${user.getID()}</legend>
        <form name="CertainUserForm" action="/user/${user.getID()?c}" method="POST">
            <@common.formInput id="name" name="name" label="Имя" value="${user.getName()}" /> <br>
            Роль : ${user.getRole().getTitle()} <br>
            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>