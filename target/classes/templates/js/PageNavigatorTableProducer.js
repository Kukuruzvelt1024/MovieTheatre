{
let nparams = new URLSearchParams(document.location.search);
let countryRequired = nparams.get("country");
let yearRequired = nparams.get("year");
let genreRequired = nparams.get("genre");
let searchRequest = nparams.get("search");
let sortType = nparams.get("sort");
let pageNavigatorTable = document.createElement('table')
pageNavigatorTable.setAttribute("align", "center");
pageNavigatorTable.setAttribute("class", "table_of_page_navigator");
fetch("/raw/catalog?genre="+genreRequired+"&year="+yearRequired+"&country="+countryRequired+"&search="+searchRequest+"&sort="+sortType)
       .then(response => {
           if (!response.ok) {
               throw new Error(`HTTP error! status: ${response.status}`);
           }
           return response.json(); // Преобразование ответа в JSON
      })
      .then(data => {
          console.log(data); // Работаем с данными
          console.log("Length = " + data.length);
          var pageNavigatorRow = pageNavigatorTable.insertRow();
           for(let j = 1; j<((data.length)/9)+1; j++){
                let cell = pageNavigatorRow.insertCell();
                cell.innerHTML =
                "<a href=/catalog?page=" + j + ">"+j + "</a>";
           }
      })
      .catch(error => {
            console.error('Ошибка при получении данных:', error);
      });
        document.getElementById("Page_Navigator_Block").appendChild(pageNavigatorTable);
        }