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
        <legend>Введите информацию о запросе</legend>
        <form name="form" action="/user/${id?c}/request/create" method="POST">

            <label for="selection">id тренера</label>
            <select id="selection" name="field1">
                <option disabled>Выберите id тренера</option>
                <#list trainers as trainer>
                    <option value=${trainer.getID()?c}>${trainer.getID()}</option>
                </#list>
            </select> <br/>

            <@common.formInput id="field2" name="field2" label="Сообщение тренеру"/> <br/>

            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>