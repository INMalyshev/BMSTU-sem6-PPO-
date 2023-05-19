<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>

  <@common.headMacros />
  <body>
    <@common.logoMacros />
    <@common.aboutUserMacros profile=profile />
    
    <fieldset>
        <legend>Опрерации над планом тренировки ${trainingPlan.getID()}</legend>
        <div>
            <a href="/training_plan/${trainingPlan.getID()?c}/approach_plan">Создать план подхода</a> <br>
            <a href="/training_plan/${trainingPlan.getID()?c}/assign">Назначить план тренировки</a> <br>
            <a href="/user/${profile.getUser().getID()?c}/training_plans">К списку тренировок</a> <br>
            <a href="/training_plan/${trainingPlan.getID()?c}/delete">Удалить план тренировки</a>
        </div>
    </fieldset>

    <@common.showApproachPlans approachPlanList=approachPlans />

  </body>
</html>