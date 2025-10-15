let params = new URLSearchParams(document.location.search);
let catalogTable = document.createElement('table');
let pageNavigatorTable = document.createElement('table');
pageNavigatorTable.setAttribute("align", "center");
pageNavigatorTable.setAttribute("class", "table_of_page_navigator");
let headers = null;
catalogTable.setAttribute("align", "center");
catalogTable.setAttribute("class", "table_of_entities");
fetch("/raw/catalog?"+
"genre="+params.get("genre")+
"&year="+ params.get("year")+
"&country="+params.get("country")+
"&search="+params.get("search")+
"&sort="+params.get("sort")+
"&page=" + params.get("page") +
"&decade="+params.get("decade"))
       .then(response => {
            headers = response.headers;
            console.log("EntitiesPerPage = " + headers.get("EntitiesPerPage"));
            console.log("EntitiesPerRow = " + headers.get("EntitiesPerRow"));
            console.log("ResultSetSize = " + headers.get("ResultSetSize"));
           if (!response.ok) {
               throw new Error(`HTTP error! status: ${response.status}`);
           }
           return response.json(); // Преобразование ответа в JSON
      })
      .then(data => {
          console.log(data); // Работаем с данными
          console.log("Amount of entities recieved from server = " + data.length);
          var row = catalogTable.insertRow();
          let i = 0;
          for (const entity of data){
               if(i == headers.get("EntitiesPerRow")){
                   var row = catalogTable.insertRow();
                   i = 0;
               }
               i++
               let cell = row.insertCell();
               cell.innerHTML =
               "<a href=/movie/" + entity.WebMapping +"><img src=internal/poster/" + entity.WebMapping + " alt="+entity.TitleRussian + " width=120 height=180 /></a>" +
               "<p><a href=/movie/"+entity.WebMapping+ ">" + entity.TitleRussian + "</a></p>" +
               "<p>" + entity.countriesAsString+ " / " + entity.Year + " г.</p> <p>" + entity.Duration + " мин. / " + entity.genresAsString + "</p>"
               + "<p>Режиссер: " + entity.directorsAsString + "</p>"
           }//
          const paragraph = document.createElement('p');
          paragraph.textContent = "Всего фильмов по данному запросу: " + headers.get("ResultSetSize");
          document.getElementById("Amount_of_entities_presented").appendChild(paragraph);
          var pageNavigatorRow = pageNavigatorTable.insertRow();
          console.log(params);
          for(let j = 1; j<((headers.get("ResultSetSize"))/(headers.get("EntitiesPerPage")))+1; j++){
                let cell = pageNavigatorRow.insertCell();
                cell.innerHTML = "<a href=/catalog?page=" + j +
                "&genre="+params.get("genre") +
                "&country="+params.get("country") +
                "&search="+params.get("search")+
                "&sort="+params.get("sort")+
                "&decade="+params.get("decade") + ">" + j + "</a>";
          }
      })
      .catch(error => {
            console.error('Ошибка при получении данных:', error);
      });
        document.getElementById("Table_of_entities").appendChild(catalogTable);
        document.getElementById("Page_Navigator_Block").appendChild(pageNavigatorTable);
