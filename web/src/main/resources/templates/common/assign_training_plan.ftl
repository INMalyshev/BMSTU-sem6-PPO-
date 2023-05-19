<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>
  <@common.headMacros />
  <body>

    <@common.logoMacros />
    <@common.aboutUserMacros profile=profile />

    <#if withError >
        <@common.showError legend=legend message=message />
    </#if>
    
    <fieldset>
        <legend>Введи id пользователя</legend>
        <form name="form" action="" method="POST">

            <label for="userId">id пользователя</label>
            <select id="userId" name="userId">
                <option disabled>Выберите id пользователя</option>
                <#list users as user>
                    <option value=${user.getID()?c}>${user.getID()}</option>
                </#list>
            </select> <br/>

            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>