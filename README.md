# SearchApp

## Parametros para compilar el Proyecto

En realidad no existe ninguna complicacion para compilar las key ya estan incorporadas bajar la rama Master con el siguiente comando
git clone https://github.com/JhonatanRojasSuesca/SearchApp.git


# Breve descripción de la responsabilidad de cada capa propuesta.

## Arquitectura

Se agrego la arquitectura " MVVM con  Clean architecture " donde se intento tener separado las capas entre (data,di,domain, viewModel)

### Data
Se agrego toda la logica para recibir los datos del Api como de la base de datos y tambien las consultas a la base de datos

### di

Se agrego toda la logica e iniciacion de la App con Koin  para el tema de inyeccion de dependencias  donde se encuentran los modules y la inicializacion de la base de datos y clientes de retrofit

## domain

Estan los casos de uso como GetProductDetailUC ,ManagementProductLocalCartUC, SearchProductUC donde fueron los mediadores para el api o base de datos pára conectarse con el viewModel y donde se debe ubicar toda la logica de negocio 

## ViewModel

Donde esta ligado a los fragments con los StateFlow y liveData para el manejo de los datos y determinar que estado enviar o actualizar el live data y asi mostrar resultados al ojo del usuario

## Implementacion

### Base de datos

Se implemento con la base de datos Room donde se inicia en Koin y el Dao estan en el repositorio para su implementacion

### manejo de hilos Se implemento con Flow

### manejo de red se implemento Retrofit

### load de imagenes se implemetno glide

### Se implemento Navigation para el paso entre pantallas

## screnShot App

![Screenshot](https://i.postimg.cc/cL2B2zRs/Screenshot-20211016-214856-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/HxC4s7vR/Screenshot-20211016-214908-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/brmQTVYf/Screenshot-20211016-214915-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/prWfKMHB/Screenshot-20211016-214920-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/9f9YhBby/Screenshot-20211016-214937-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/j5Q6WdG5/Screenshot-20211016-214953-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/YqSN978r/Screenshot-20211016-215000-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/9FNdwkNG/Screenshot-20211016-215003-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/7P9Mwh3t/Screenshot-20211016-215009-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/T3LrVcZB/Screenshot-20211016-215013-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/0N7D3y5f/Screenshot-20211016-215019-com-jhonatanrojas-searchapp.jpg)
![Screenshot](https://i.postimg.cc/VkxXDCwv/Screenshot-20211016-215025-com-jhonatanrojas-searchapp.jpg)





## ScrennShot Inspector Layout

![Screenshot](https://i.postimg.cc/Wzz7QcH1/inpect-1.png)
![Screenshot](https://i.postimg.cc/Y2DRQjvG/inspect-2.png)
![Screenshot](https://i.postimg.cc/T1scxNzq/inspect-3.png)
