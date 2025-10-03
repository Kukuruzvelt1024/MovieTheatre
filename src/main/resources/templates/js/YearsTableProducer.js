let decadestable = document.createElement('table');
        decadestable.setAttribute("align", "center");
        decadestable.setAttribute("class", "table_of_years");

        fetch("/raw/decades").then(response => {
            if (!response.ok) {throw new Error(`HTTP error! status: ${response.status}`);}
            return response.json(); // Преобразование ответа в JSON
       })
       .then(data => {
            var row = decadestable.insertRow();
            let alldecadesCellReference = row.insertCell();
            alldecadesCellReference.innerHTML = "<p><a href=?decade=all>Все</a><p/>"
            let i = 1;
             for (const entity of data){
                if (i == 4){
                    var row = decadestable.insertRow();
                }
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?decade=" + entity + ">" + entity + "-е"+"</a></p>"
                i++
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_year").appendChild(decadestable);