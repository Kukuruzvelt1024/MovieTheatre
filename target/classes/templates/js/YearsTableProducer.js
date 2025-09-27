let yearstable = document.createElement('table');
        yearstable.setAttribute("align", "left");
        yearstable.setAttribute("class", "table_of_years");
        fetch("/raw/years").then(response => {
            if (!response.ok) {throw new Error(`HTTP error! status: ${response.status}`);}
            return response.json(); // Преобразование ответа в JSON
       })
       .then(data => {
            var row = yearstable.insertRow();
            let allYearCellReference = row.insertCell();
            allYearCellReference.innerHTML = "<p><a href=?year=all>Все</a><p/>"
             for (const entity of data){
		 var row = yearstable.insertRow();
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?year=" + entity + ">" + entity + "</a></p>"
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_year").appendChild(yearstable);