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
        <legend>Введите информацию о подходе</legend>
        <form name="form" action="/training/${trainingId?c}/approach" method="POST">

            <p><select name="field1">
                <option disabled>Выберите тип упражнения</option>
                <#list exerciseTypes as et>
                    <option value=${et.getTitle()}>${et.getTitle()}</option>
                </#list>
            </select></p>

            <@common.formInput id="userId" name="field2" label="Фактическое количество повторений"/> <br/>

            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>