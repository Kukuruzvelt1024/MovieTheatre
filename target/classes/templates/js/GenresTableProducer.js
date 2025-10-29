 {
 let params = new URLSearchParams(document.location.search);
 let genrestable = document.createElement('table');
        genrestable.setAttribute("align", "center");
        genrestable.setAttribute("class", "table_of_genres");
        fetch("/raw/catalog/genres?"+
                      "genre="+params.get("genre")+
                      "&year="+ params.get("year")+
                      "&country="+params.get("country")+
                      "&search="+params.get("search")+
                      "&sort="+params.get("sort")+
                      "&page=" + params.get("page") +
                      "&decade="+params.get("decade") +
                      "&director="+params.get("director")).then(response => {
            if (!response.ok) {throw new Error(`HTTP error! status: ${response.status}`);}
            return response.json(); // Преобразование ответа в JSON
       })
       .then(data => {
            console.log(data); // Работаем с данными
            console.log("Length=" + data.length);
            var row = genrestable.insertRow();
            let allGenresCellReference = row.insertCell();
            let i = 0;
            allGenresCellReference.innerHTML = "<p><a href=?genre=all>Все жанры</a><p/>"
             for (const entity of data){
                i++;
                if(i == 7){
                    i = 0;
                    row = genrestable.insertRow()
                }
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?genre=" + entity + "&genre="+params.get("genre") +
                                                                "&country="+params.get("country") +
                                                                "&search="+params.get("search")+
                                                                "&sort="+params.get("sort")+
                                                                "&decade="+params.get("decade") + ">" + entity + "</a></p>"
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_genre").appendChild(genrestable);
     }