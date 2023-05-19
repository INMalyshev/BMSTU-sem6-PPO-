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
            <#if (profile.getUser().getRole().getTitle() == "Авторизованный пользователь")>
                <div><a href="/user/${profile.getUser().getID()?c}/request/create">Запросить тренировку</a></div>
            <#else></#if>
            <div><a href="/">На главную</a></div>
        </div>
    </fieldset>

    <@common.showRequests requests=requests profile=profile />
  </body>
</html>