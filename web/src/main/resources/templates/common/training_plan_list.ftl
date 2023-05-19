<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>

  <@common.headMacros />
  <body>
    <@common.logoMacros />
    <@common.aboutUserMacros profile=profile />

    <fieldset>
        <legend>Опрерации</legend>
        <div>
            <a href="/user/${profile.getUser().getID()?c}/training_plan">Создать план тренировки</a> <br>
            <a href="/">На главную</a>
        </div>
    </fieldset>

    <@common.showTrainingPlans traininPlanList=trainingPlans />
  </body>
</html>