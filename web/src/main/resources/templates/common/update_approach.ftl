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
        <legend>Данные о подходе ${approach.getID()}</legend>
        <form name="form" action="/approach/${approach.getID()?c}" method="POST">
            <@common.formInput id="name" name="field1" label="Фактическое количество" value="${approach.getAmount()}" /> <br>
            Тип упражнения : ${approach.getType().getTitle()} <br>
            <input type="submit" value="Продолжить" />
        </form>
    </fieldset>

  </body>
</html>