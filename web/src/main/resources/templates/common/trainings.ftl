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
            <a href="/user/${profile.getUser().getID()?c}/training">Создать тренировку</a> <br>
            <a href="/user/${profile.getUser().getID()?c}/trainings">Все тренировки</a> <br>
            <a href="/user/${profile.getUser().getID()?c}/trainings?order=done">Выполненные тренировки</a> <br>
            <a href="/user/${profile.getUser().getID()?c}/trainings?order=planned">Запланированные тренировки</a> <br>
            <a href="/">На главную</a>
        </div>
    </fieldset>

    <@common.showTrainings trainingList=trainings />
  </body>
</html>