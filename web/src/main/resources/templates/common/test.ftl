<#import "macros/common.ftl" as common>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/css/styles.css">
    <title>Заголовок для отображения в названии вкладки</title>
  </head>
  <body>
    <header>
        Верхняя часть страницы: логотип, контакты
      <nav class="redtext">
        Меню (навигация)
        <@common.testMacros number=number />
        <#if (number > 3) >!!!!<#else></#if>
      </nav>
    </header>
    <main>
      Основное содержимое страницы
    </main>
    <footer>
      Подвал
    </footer>
  </body>
</html>