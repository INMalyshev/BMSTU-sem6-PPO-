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
        <form name="form" action="/training_plan/${trainingPlanId?c}/approach_plan" method="POST">

            <p><select name="formString">
                <option disabled>Выберите тип упражнения</option>
                <#list exerciseTypes as et>
                    <option value=${et.getTitle()}>${et.getTitle()}</option>
                </#list>
            </select></p>

            <@common.formInput id="userId" name="formNumber" label="Планируемое количество повторений"/> <br/>

            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>