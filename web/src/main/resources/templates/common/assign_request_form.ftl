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
        <legend>Введите информацию о плане подхода</legend>
        <form name="form" action="/request/${id?c}/assign" method="POST">

            <select name="field1">
                <option disabled>Выбери id плана тренировки</option>
                <#list plans as plan>
                    <option value=${plan.getID()?c}>${plan.getID()}</option>
                </#list>
            </select>

            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>