let countriestable = document.createElement('table');
        countriestable.setAttribute("align", "center");
        countriestable.setAttribute("class", "table_of_countries");
        fetch("/raw/countries").then(response => {
            if (!response.ok) {throw new Error(`HTTP error! status: ${response.status}`);}
            return response.json(); // Преобразование ответа в JSON
       })
       .then(data => {
            var row = countriestable.insertRow();
            let allCountriesCellReference = row.insertCell();
            allCountriesCellReference.innerHTML = "<p><a href=?country=all>Все</a><p/>"
             for (const entity of data){
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?country=" + entity + ">" + entity + "</a></p>"
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_country").appendChild(countriestable);