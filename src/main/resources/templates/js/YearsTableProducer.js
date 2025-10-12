{
let params = new URLSearchParams(document.location.search);
let decadestable = document.createElement('table');
        decadestable.setAttribute("align", "center");
        decadestable.setAttribute("class", "table_of_years");

        fetch("/raw/decades?"+
                      "genre="+params.get("genre")+
                      "&year="+ params.get("year")+
                      "&country="+params.get("country")+
                      "&search="+params.get("search")+
                      "&sort="+params.get("sort")+
                      "&page=" + params.get("page") +
                      "&decade="+params.get("decade")).then(response => {
            if (!response.ok) {throw new Error(`HTTP error! status: ${response.status}`);}
            return response.json(); // Преобразование ответа в JSON
       })
       .then(data => {
            var row = decadestable.insertRow();
            let alldecadesCellReference = row.insertCell();
            alldecadesCellReference.innerHTML = "<p><a href=?decade=all>Все</a><p/>"
            let i = 1;
             for (const entity of data){
                if (i % 3 == 0){
                    var row = decadestable.insertRow();
                }
                let cell = row.insertCell();
                cell.innerHTML =
                "<p><a href=?decade=" + entity + "&genre="+params.get("genre") +
                                                                 "&country="+params.get("country") +
                                                                 "&search="+params.get("search")+
                                                                 "&sort="+params.get("sort")+
                                                                 "&decade="+params.get("decade") + ">" + entity + "-е"+"</a></p>"
                i++
            }
       })
       .catch(error => {
             console.error('Ошибка при получении данных:', error);
       });
     document.getElementById("Filter_by_year").appendChild(decadestable);
     }