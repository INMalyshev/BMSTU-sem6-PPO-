<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>

  <@common.headMacros />
  <body>
    <@common.logoMacros />
    <@common.aboutUserMacros profile=profile />
    
    <fieldset>
        <legend>Опрерации над тренировкой ${training.getID()}</legend>
        <div>
            <a href="/user/${profile.getUser().getID()?c}/trainings">К списку тренировок</a> <br>
            <#if (training.GetCompleted() == false)>
                <a href="/training/${training.getID()?c}/finish">Завершить тренировоку</a> <br>
                <a href="/training/${training.getID()?c}/approach">Добавить подход</a> <br>
            <#else></#if>
            <a href="/training/${training.getID()?c}/delete">Удалить тренировку</a>
        </div>
    </fieldset>

    <@common.showApproachs approachList=approachs />

  </body>
</html>