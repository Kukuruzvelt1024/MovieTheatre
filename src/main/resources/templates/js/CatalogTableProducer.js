let params = new URLSearchParams(document.location.search);
let countryRequired = params.get("country");
let yearRequired = params.get("year");
let genreRequired = params.get("genre");
let searchRequest = params.get("search");
let sortType = params.get("sort");
let page = params.get("page");
if (page == null){
    page = "1";
}
console.log("Current Page = " + page);
let catalogTable = document.createElement('table');
catalogTable.setAttribute("align", "center");
catalogTable.setAttribute("class", "table_of_entities");
fetch("/raw/catalog?genre="+genreRequired+"&year="+yearRequired+"&country="+countryRequired+"&search="+searchRequest+"&sortby="+sortType+"&page=" + page)
       .then(response => {
           if (!response.ok) {
               throw new Error(`HTTP error! status: ${response.status}`);
           }
           return response.json(); // Преобразование ответа в JSON
      })
      .then(data => {
          console.log(data); // Работаем с данными
          console.log("Amount of entities = " + data.length);

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
               "<p>" + entity.Countries + " / " + entity.genresAsString + " / " + entity.Duration + " мин. / " + entity.Year + "</p>"
           }

      })
      .catch(error => {
            console.error('Ошибка при получении данных:', error);
      });
        document.getElementById("Table_of_entities").appendChild(catalogTable);
