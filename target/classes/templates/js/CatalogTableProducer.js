let params = new URLSearchParams(document.location.search);
let countryRequired = params.get("country");
let yearRequired = params.get("year");
let genreRequired = params.get("genre");
let searchRequest = params.get("search");
let sortType = params.get("sortby");
let page = params.get("page");
let catalogTable = document.createElement('table');
let pageNavigatorTable = document.createElement('table')
pageNavigatorTable.setAttribute("align", "center");
catalogTable.setAttribute("align", "center");
pageNavigatorTable.setAttribute("class", "table_of_page_navigator");
fetch("/raw/catalog?genre="+genreRequired+"&year="+yearRequired+"&country="+countryRequired+"&search="+searchRequest+"&sortby="+sortType+"&page=" + page)
       .then(response => {
           if (!response.ok) {
               throw new Error(`HTTP error! status: ${response.status}`);
           }
           return response.json(); // Преобразование ответа в JSON
      })
      .then(data => {
          console.log(data); // Работаем с данными
          console.log("Length=" + data.length);
          var pageNavigatorRow = pageNavigatorTable.insertRow();
          var row = catalogTable.insertRow();
          var i = 0;
          for (const entity of data){
               if(i == 3){
                   var row = catalogTable.insertRow();
                   i = 0;
               }
               i++
               let cell = row.insertCell();
               cell.innerHTML =
               "<a href=/movie/" + entity.WebMapping +"><img src=internal/poster/" + entity.WebMapping + " alt="+entity.TitleRussian + " width=120 height=180 /></a>" +
               "<p><a href=/movie/"+entity.WebMapping+ ">" + entity.TitleRussian + "</a></p>" +
               "<p>" + entity.Countries+ " / " + entity.Genres + " / " + entity.Duration + " мин. / " + entity.Year + "</p>"
           }
           for(let j = 1; j<(data.length-1)/12; j++){
                let cell = pageNavigatorRow.insertCell();
                cell.innerHTML =
                "<a href=/catalog?page=" + j + ">"+j + "</a>";
           }
      })
      .catch(error => {
            console.error('Ошибка при получении данных:', error);
      });
        document.getElementById("Table_of_entities").appendChild(catalogTable);
        document.getElementById("Page_Navigator_Block").appendChild(pageNavigatorTable);