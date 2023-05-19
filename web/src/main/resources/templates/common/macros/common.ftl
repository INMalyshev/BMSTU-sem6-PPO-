<#macro testMacros number>
    <b>Number : ${number}</b>
</#macro>

<#macro headMacros pageTitle="TrainingExchange">
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/css/styles.css">
    <title>${pageTitle}</title>
</head>
</#macro>

<#macro showUserMacro user>
<fieldset>
    <div>id   : ${user.getID()}</div>
    <div>Роль : ${user.getRole().getTitle()}</div>
    <div>Имя  : ${user.getName()}</div>
</fieldset>
</#macro>

<#macro formInput id name label type="text" value="">
    <label for="${id}">${label}</label>
    <input type="${type}" id="${id}" name="${name}" value="${value}">
</#macro>

<#macro showError legend="" message="">
    <div class="redtext">
        <fieldset>
            <legend>${legend}</legend>
            ${message}
        </fieldset>
    </div>
</#macro>

<#macro logoMacros>
    <h3 class = "redtext">
        <a href="/">TrainingExchange</a>
    </h3>
</#macro>

<#macro aboutUserMacros profile>
    <div>
        <#if profile.isAuthorized()>
            <fieldset>
                <legend>Пользователь</legend>
                <@showUserMacro user=profile.getUser() />
                <fieldset>
                    <a href="/user/${profile.getUser().getID()?c}">Изменить</a> <br>
                    <a href="/logout">Выйти</a>
                </fieldset>
            </fieldset>
        <#else>
            <fieldset>
                <legend>Гость</legend>
                <a href="/login">Авторизоваться</a> <br>
                <a href="/signin">Создать нового пользователя</a>
            </fieldset>
        </#if>
    </div>
</#macro>

<#macro showIndexOptionsMacro profile>
    <#if profile.isAuthorized()>
        <#if profile.getUser().getRole().getTitle() == "Авторизованный пользователь">
            <fieldset>
                <legend>Тренировки</legend>
                <a href="/user/${profile.getUser().getID()?c}/trainings">Тренировки</a> <br>
                <a href="/user/${profile.getUser().getID()?c}/requests">Активные запросы</a> <br>
            </fieldset>
        <#elseif profile.getUser().getRole().getTitle() == "Тренер">
            <fieldset>
                <legend>Консоль управления</legend>
                <a href="/user/${profile.getUser().getID()?c}/training_plans">Планы тренироок</a> <br>
                <a href="/user/${profile.getUser().getID()?c}/requests">Ожидающие ответв запросы</a> <br>
            </fieldset>
        <#else></#if>
    <#else></#if>
</#macro>

<#macro showTrainingPlans traininPlanList>
    <fieldset>
        <legend>Планы тренировок</legend>
        <#list traininPlanList as tp>
            <@showTrainingPlan trainingPlan=tp />
        </#list>
    </fieldset>
</#macro>

<#macro showTrainingPlan trainingPlan>
    <fieldset>
        <legend>План тренировки</legend>
        <div>
            <div>ID       : ${trainingPlan.getID()}</div>
            <a href="/training_plan/${trainingPlan.getID()?c}">Подробнее</a> <br>
        </div>
    </fieldset>
</#macro>

<#macro showApproachPlans approachPlanList>
    <fieldset>
        <legend>Планы подходов</legend>
        <#list approachPlanList as ap>
            <@showApproachPlan approachPlan=ap />
        </#list>
    </fieldset>
</#macro>

<#macro showApproachPlan approachPlan>
    <fieldset>
        <legend>План подхода</legend>
        <div>
            <div>ID                   : ${approachPlan.getID()}</div>
            <div>Тип                  : ${approachPlan.getType().getTitle()}</div>
            <div>Ожидаемое количество : ${approachPlan.getExpectedAmount()}</div>
            <a href="/approach_plan/${approachPlan.getID()?c}/delete">Удалить</a> <br>
        </div>
    </fieldset>
</#macro>

<#macro showTrainings trainingList>
    <fieldset>
        <legend>Тренировки</legend>
        <#list trainingList as t>
            <@showTraining training=t />
        </#list>
    </fieldset>
</#macro>

<#macro showTraining training>
    <fieldset>
        <legend>Тренировка</legend>
        <div>
            <div>ID          : ${training.getID()}</div>
            <div>Завершена   : <#if training.GetCompleted()>Да!<#else>Нет :(</#if></div>
            <a href="/training/${training.getID()?c}">Подробнее</a> <br>
        </div>
    </fieldset>
</#macro>

<#macro showApproachs approachList>
    <fieldset>
        <legend>Подходоы</legend>
        <#list approachList as a>
            <@showApproach approach=a />
        </#list>
    </fieldset>
</#macro>

<#macro showApproach approach>
    <fieldset>
        <legend>Подход</legend>
        <div>
            <div>ID                     : ${approach.getID()}</div>
            <div>Тип                    : ${approach.getType().getTitle()}</div>
            <#if (approach.getExpectedAmount() >= 0)>
                <div>Ожидаемое количество   : ${approach.getExpectedAmount()}</div>
            <#else></#if>
            <#if (approach.getAmount() >= 0)>
                <div>Фактическое количество : ${approach.getAmount()}</div>
            <#else></#if>
            <#if (approach.getAmount() <= 0)>
                <a href="/approach/${approach.getID()?c}">Указать фактическое количество</a> <br>
            <#else></#if>
            <a href="/approach/${approach.getID()?c}/delete">Удалить</a> <br>
        </div>
    </fieldset>
</#macro>

<#macro showRequests requests profile>
    <fieldset>
        <legend>
        <#if (profile.getUser().getRole().getTitle() == "Тренер")>
            Ожидающие ответ запросы
        <#else>
            Ваши активные запросы
        </#if>
        </legend>
        <#list requests as request>
            <@showRequest request=request profile=profile/>
        </#list>
    </fieldset>
</#macro>

<#macro showRequest request profile>
    <fieldset>
        <legend>Запрос</legend>
        <div>
            <#if (profile.getUser().getRole().getTitle() == "Тренер")>
                <div>ID : ${request.getID()}</div>
                <div>ID пользователя : ${request.getUserFromId()}</div>
                <div>Комментарий : ${request.getMessage()}</div>
                <a href="/request/${request.getID()?c}/assign">Назначить тренировку</a> <br>
            <#else>
                <div>ID : ${request.getID()}</div>
                <div>ID тренера : ${request.getUserToId()}</div>
                <div>Комментарий : ${request.getMessage()}</div>
                <a href="/request/${request.getID()?c}/delete">Удалить</a> <br>
            </#if>
        </div>
    </fieldset>
</#macro>
