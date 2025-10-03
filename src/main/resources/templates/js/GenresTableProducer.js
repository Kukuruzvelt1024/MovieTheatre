 let genrestable = document.createElement('table');
        genrestable.setAttribute("align", "center");
        genrestable.setAttribute("class", "table_of_genres");
        fetch("/raw/genres").then(response => {
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
                if(i == 6){
                    i = 0;
                    row = genrestable.insertRow()
                }
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?genre=" + entity + ">" + entity + "</a></p>"
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_genre").appendChild(genrestable);